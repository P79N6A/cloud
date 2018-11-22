package com.springframework.gateway.configure;//package com.springframework.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author summer
// */
//public class MybatisPlusConfig {
//
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }
//
//    /**
//     * mybatis-plus分页插件<br>
//     * 文档：http://mp.baomidou.com<br>
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        // 开启 PageHelper 的支持
//        paginationInterceptor.setLocalPage(true);
//        /*
//         * 【测试多租户】 SQL 解析处理拦截器<br>
//         * 这里固定写成住户 1 实际情况你可以从cookie读取，因此数据看不到 【 麻花藤 】 这条记录（ 注意观察 SQL ）<br>
//         */
//        List<ISqlParser> sqlParserList = new ArrayList<>();
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
//        sqlParserList.add(tenantSqlParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);
//        return paginationInterceptor;
//    }
//
//    /**
//     * 注入主键生成器
//     */
//    @Bean
//    public IKeyGenerator keyGenerator() {
//        return new H2KeyGenerator();
//    }
//
//    /**
//     * 注入sql注入器
//     */
//    @Bean
//    public ISqlInjector sqlInjector() {
//        return new LogicSqlInjector();
//    }
//}
