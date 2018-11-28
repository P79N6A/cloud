package com.springframework.gateway.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author summer
 * 2018/11/28
 */
@Getter
@Setter
public class CacheExpireFailEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4844848008509393773L;

    private String prefix;
    private String item;

    public CacheExpireFailEvent(Object source, String prefix, String item) {
        super(source);
        this.item = item;
        this.prefix = prefix;
    }

}
