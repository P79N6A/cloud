package com.springframework.enums.idgenerator;

import com.springframework.enums.IntValueDescription;
import com.springframework.enums.StringValueDescription;

/**
 * @author summer ID 生成类型枚举
 * 2018/8/20
 */
public enum IdTypeEnum implements IntValueDescription {
    /**
     * 支付中心枚举
     */
    PAY_CENTER(100,"PAY_CENTER");
    /**
     *
     */
    private int value;
    /**
     *
     */
    private String desc;

    IdTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public int getValue() {
        return value;
    }
}
