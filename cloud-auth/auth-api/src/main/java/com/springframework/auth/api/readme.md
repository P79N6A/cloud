接口定义参考ZoneService

````
@RequestMapping("/zoneservice")
public interface ZoneService {
 
    @RequestMapping("/create", method = RequestMethod.POST)
    Long create(@RequestBody ZoneDTO dto);
 
    @RequestMapping("/update", method = RequestMethod.POST)
    Integer update(@RequestBody ZoneDTO dto);
 
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    ZoneDTO list(@RequestBody ZoneDTO dto,
                                                 @RequestParam(value = "pageindex") int pageIndex,
                                                 @RequestParam(value = "pagesize") int pageSize);
 
    @RequestMapping("/deletebyprimarykey",  method = RequestMethod.GET)
    Integer deleteByPrimaryKey(@RequestParam(value = "id") Long id);
 
    @RequestMapping("/batchdeletebyprimarykey",  method = RequestMethod.POST)
    Integer batchDeleteByPrimaryKey(@RequestBody List<Long> ids);
    
````

注意：

为了日后将来平滑切换到HTTP/2，所有请求需要明确指定请求类型（GET、POST...）

请求参数的定义：

基本类型（比如：int、long、string等）：使用@RequestParam定义，value属性必须有

复杂对象：包括自定义的model，List、Set等集合类，都必须使用@RequestBody定义，每个请求只能有一个@RequestBody定义，如果存在多个复杂对应，则需要建单独的model来包装



#### Feign接口消费者实现
当我们需要消费一个Feign提供的HTTP接口的时候，我们直接通过HTTP调用就可以实现，但是这样远远不够，实际生产环境中，对于接口的调用还需要具备负载均衡、重试机制、服务降级、服务熔断来保障服务的高可用。所以我们不能纯粹的使用HTTP请求框架来实现Feign接口的调用，而是使用Feign提供的客户端SDK来完成编程。
     
Feign接口的消费实现
比如我们要实现对上面服务提供者提供的ZoneService进行调用，只需要做下面的步骤：

1 在消费方server模块中引入封装模块的依赖：

````
<dependency>
   <groupId>com.framework</groupId>
   <artifactId>framework-feign</artifactId>
</dependency>
````
2 在消费方server模块中引入将要调用接口的api模块，比如
````
<dependency>
   <groupId>com.framework</groupId>
   <artifactId>auth-api</artifactId>
   <version>1.0-SNAPSHOT</version>
</dependency>
````
3 在应用主类中增加@EnableFeignClients注解，开启Feign客户端的扫描，并通过@Import注解，引入封装的Feign配置FeignConfiguration
```

@EnableFeignClients(basePackages={})
@SpringCloudApplication
@MapperScan("com.xxx.mapper")
@Import({XXXConfig.class, FeignConfiguration.class})
@PropertySource("classpath:config.properties")
public class Bootstrap {
 
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
 
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
        log.info("Bootstrap started successfully");
    }
 
}
```
 在@EnableFeignClients注解中通过basePackages属性可以精确定位要初始化的Feign客户端包路径，包路径就是下面一步定义内容所在的包，推荐指定扫描，以免初始化非必要内容。

4 在消费方server模块的feign包下创建一个代理接口来产生依赖服务的本地客户端，比如：
```$xslt
@FeignClient(value = "xxx")
public interface ZoneServiceClient extends ZoneService {
 
}
```
其中xxx为ZoneService提供方的服务名（spring.application.name属性值）

5 在需要调用提供方的ZoneService的时候，我们只需要通过@Autowired注解注入就可以使用了，比如：
```$xslt
@RestController
@RequestMapping("/mst/zone")
public class ZoneController {
 
    @Autowired
    private ZoneService zoneService;
 
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public PaginationData<ZoneVO> list(
            @RequestParam(value = "pageindex") int pageIndex,
            @RequestParam(value = "pagesize") int pageSize,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "zonetype", required = false) Byte zoneType) {
        ...
        ZoneDTO responseData = zoneService.list(entity, pageIndex, pageSize);
        ...
    }
 
}
```
如上面实现的时候，zoneService.list(entity, pageIndex, pageSize); 在调用远程的HTTP服务时候，会自动的实现负载均衡等特性来进行调用，而不是简单的直接调用HTTP请求。

#####Feign接口的基础设置
客户端的饥饿设置，通过该设置，在启动的时候，会主动的创建client端调用对象，而不是等到调用的时候才去初始化，可以加速第一次依赖服务的调用，设置方法：
```$xslt
ribbon.eager-load.enabled=true
ribbon.eager-load.clients=xxx
```
其中ribbon.eager-load.clients设置依赖的服务名，多个用逗号分隔
全局的超时与重试设置，建议先按如下设置：
```$xslt
# 创建http连接的超时时间
ribbon.ConnectTimeout=500
# http连接后未返回的超时时间
ribbon.ReadTimeout=10000
ribbon.OkToRetryOnAllOperations=true
# 跨实例重试次数
ribbon.MaxAutoRetriesNextServer=0
# 当前实例重试次数
ribbon.MaxAutoRetries=0
```
默认情况下不开启重试机制，默认的请求超时未10秒
指定服务的超时与重试设置。通过在ribbon的参数前增加服务名就可以指定某个服务来覆盖全局配置，比如：
```$xslt
wms-mst-center.ribbon.ReadTimeout=5000
wms-mst-center.ribbon.MaxAutoRetriesNextServer=1
```
通过上面的设置，对xxx服务调用的接口调用超时时间覆盖为5000毫秒，超时后会换一个实例进行重试。



Feign接口的来源过滤
我们做了一些扩展，可以轻易的通过注解的方式来控制接口的访问权限。主需要做如下配置：

在application.properties中增加参数：

security.origin-filter.enabled=true
在接口定义中使用注解，比如：

@RequestMapping("/testservice")
public interface TestService {
 
    @OriginService(name = {"xxx", "xxx"})
    @GetMapping("/exception3")
    String exception3();
 
}
通过@OriginService注解的name属性配置了可以允许访问的服务名，如上配置，/testservice/exception3接口只允许product-center和wms-mst-center访问。
没有使用@OriginService注解的接口将没有访问限制，所有服务都可以调用。

实现原理：

feign客户端，在调用的时候都自动增加了一个头信息：X-YH-FeignOrigin，内容为客户端服务名
feign服务端，服务提供方通过Fitler的方式实现了读取HTTP头信息，根据自己接口定义和X-YH-FeignOrigin中信息的关系判断是否过滤，该过滤器需要通过security.origin-filter.enabled参数开启。
Feign接口的消费降级与熔断设置
在使用了Feign之后，我们可以用两种不同的方式来使用Hystrix提供的服务降级和熔断功能，具体使用哪种方式完全取决于开发者的习惯和已有的编程模型，根据自己更适应的方式进行编写即可。

Feign的整合实现
配置参数：feign.hystrix.enabled=true，开启hystrix的整合支持
定义降级逻辑，在@FeignClient注解中通过使用fallback属性来指定具体的接口降级实现类，比如下面的例子：

@FeignClient(value = "xxx", fallback = ZoneServiceFallback.class)
public interface ZoneServiceClient extends ZoneService {
 
}
 
@Component
class ZoneServiceFallback implements ZoneServiceClient {
 
    @Override
    public Long create(ZoneDTO zoneDTO) {
        throw new RuntimeException("no fallback method");
    }
 
    @Override
    public ZoneDTO list(ZoneDTO zoneDTO, int i, int i1) {
        return new ZoneDTO();
    }
  
    ...
}
当create方法中抛出一个非业务异常的时候，比如TimeOutException，这时候就会进入到ZoneServiceFallback的create方法，执行降级逻辑，对于无降级逻辑的地方可以继续抛出希望对上层提供的异常信息。而list方法则以返回一个空对象作为降级逻辑，具体如何降级需要根据开发的业务需求来制定，并不是所有的函数都有对应的降级逻辑。需要注意的是，一旦进入了降级逻辑，那么这一次调用会记入熔断的计数器中，参与判断是否要熔断的依据。



熔断参数设置，熔断有相当对的参数可以配置，下面主要讲一下一些常用配置以及配置的定义规则：
全局设置，比如设置熔断的超时时间：hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=10000
其中default表示是全局设置，后面的部分execution.isolation.thread.timeoutInMilliseconds为具体的参数名。
注意：当我们需要重试的时候，那么熔断的超时时间必须大于ribbon的超时时间，不然熔断之后就不会再进行重试了

指定方法设置，在全局配置中，我们可以替换default为调用的方法标识，就能精确的指定某个方法来指定熔断参数，比如：
hystrix.command.ZoneServiceClient#list(ZoneDTO,int,int).execution.isolation.thread.timeoutInMilliseconds=5000
上面设置的红色部分就指定了ZoneServiceClient类的list方法，(ZoneDTO,int,int)是list方法的参数类型

Hystrix的所有参数可见：https://github.com/Netflix/Hystrix/wiki/Configuration
 

Hystrix的原生实现