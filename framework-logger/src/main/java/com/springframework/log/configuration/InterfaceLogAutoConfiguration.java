package com.springframework.log.configuration;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedIOException;

import java.io.InputStream;
import java.net.URL;


public class InterfaceLogAutoConfiguration implements InitializingBean {

    @Autowired
    private SqlSessionFactory sessionFactory;


    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration configuration = sessionFactory.getConfiguration();
        String mapperLocation = "META-INF/mybatis/log/interfaceLogMapper.xml";
        URL url = this.getClass().getClassLoader().getResource(mapperLocation);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(mapperLocation);
        try {
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(inputStream,
                    configuration, url.toString(), configuration.getSqlFragments());
            xmlMapperBuilder.parse();
        } catch (Exception e) {
            throw new NestedIOException("Failed to parse mapping resource: '" + url + "'", e);
        } finally {
            ErrorContext.instance().reset();
        }
    }
}
