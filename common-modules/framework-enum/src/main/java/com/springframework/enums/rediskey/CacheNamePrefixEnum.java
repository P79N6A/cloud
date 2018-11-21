package com.springframework.enums.rediskey;

import com.springframework.enums.StringValueDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author summer  Redis Key规范
 * 2018/11/21
 */
@NoArgsConstructor
@AllArgsConstructor
public enum CacheNamePrefixEnum implements StringValueDescription {

    GATEWAY("cloud-gateway", "gateway", "网管gateway缓存");

    @Getter
    private String cacheGroup;
    @Getter
    private String cacheName;
    private String description;

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        StringBuilder prefix = new StringBuilder();
        prefix.append(cacheGroup);
        prefix.append(":");
        prefix.append(cacheName);
        prefix.append(":");
        prefix.append(description);
        return prefix.toString();
    }

}
