//package com.springframework.demo.config;//package com.springframework.gateway.config;
//
//import com.baomidou.mybatisplus.MybatisConfiguration;
//import com.baomidou.mybatisplus.MybatisMapWrapperFactory;
//import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
//import com.baomidou.mybatisplus.incrementer.H2KeyGenerator;
//import com.baomidou.mybatisplus.incrementer.IKeyGenerator;
//import com.baomidou.mybatisplus.mapper.ISqlInjector;
//import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
//import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
//import com.baomidou.mybatisplus.plugins.parser.ISqlParser;
//import com.baomidou.mybatisplus.plugins.parser.tenant.TenantSqlParser;
//import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.type.JdbcType;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author summer
// */
//@Configuration
//@MapperScan(basePackages = "com.springframework.gateway.domain.routeconfig.mapper")
//public class MybatisPlusConfig {
//
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }
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
//}
