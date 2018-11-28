package com.springframework.domain.base.configure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * TODO 未完待续，
 *
 * @author summer 2018/11/27
 */
public class MetaObjectHandlerConfigure implements MetaObjectHandler {
    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Object creater = getFieldValByName("created_by", metaObject);
        if (creater == null) {
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
        if (updater == null) {
            setFieldValByName("last_modified_by", "auto", metaObject);
        }
        setFieldValByName("last_modified_time", new Date(), metaObject);
    }
}
