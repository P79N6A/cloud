####  在server模块中实现api模块中定义的接口，并用@RestController声明，spring mvc将接口处理为HTTP请求对外提供，比如：

````
@RestController
public class ZoneServiceImpl implements ZoneService {
 
    @Override
    public Long create(@RequestBody ZoneDTO dto) {
        ...
    }
 
    @Override
    public Integer update(@RequestBody ZoneDTO dto) {
        ...
    }
 
    @Override
    public ZoneDTO list(@RequestBody ZoneDTO dto, @RequestParam(value = "pageindex") int pageIndex, @RequestParam(value = "pagesize") int pageSize) {
        ...
    }
 
    @Override
    public Integer deleteByPrimaryKey(@RequestParam(value = "id") Long id) {
        ...
    }
 
    @Override
    public Integer batchDeleteByPrimaryKey(@RequestBody List<Long> ids) {
        ...
    }
 
}

````

注意：实现类中参数上的注解（@RequestParam、@RequestBody）必须包含

接口提供者的异常
接口抛出异常的时候，需要明确异常类型，如果是业务级别的正常异常，应继承BaseKnownException，该异常在消费方会做特殊处理，让它不触发服务降级和服务熔断。
对于异常的构造函数需要特别注意，由于json的反序列化机制特殊，对于构造函数的传参在构造函数的逻辑中不应该参与内容的拼接操作，不然会导致拼接操作经反序列化过程被重复调用，导致拼接结果异常。

