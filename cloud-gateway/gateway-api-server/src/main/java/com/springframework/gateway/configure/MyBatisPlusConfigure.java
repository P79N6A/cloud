package com.springframework.gateway.configure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @author summer
 * 2018/11/27
 */
@Configuration
public class MyBatisPlusConfigure {

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            /**
             * 插入元对象字段填充（用于插入时对公共字段的填充）
             *
             * @param metaObject 元对象
             */
            @Override
            public void insertFill(MetaObject metaObject) {
                Object creater = getFieldValByName("created_by", metaObject);
                if(creater==null){
                    setFieldValByName("created_by", "auto", metaObject);
                }
                setFieldValByName("created_time", new Date(), metaObject);

            }

            /**
             * 更新元对象字段填充（用于更新时对公共字段的填充）
             *
             * @param metaObject 元对象
             */
            @Override
            public void updateFill(MetaObject metaObject) {
                Object updater = getFieldValByName("last_modified_by", metaObject);
                if(updater==null){
                    setFieldValByName("last_modified_by", "auto", metaObject);
                }
                setFieldValByName("last_modified_time", new Date(), metaObject);
            }
        };
    }
}
