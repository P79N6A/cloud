package com.springframework.gateway.event;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * @author summer
 * 2018/11/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CacheExpireFailEvent extends ApplicationEvent {

    private String prefix;
    private String item;

    public CacheExpireFailEvent(Object source, String prefix, String item) {
        super(source);
        this.item = item;
        this.prefix = prefix;
    }
}
