package com.springframework.enums.fds;

import lombok.Getter;

/**
 * @author summer
 * 2018/8/13
 */
public enum FileTypeEnum {
    /**
     * 图片
     */
    PIC(0),
    /**
     * excel
     */
    XLS(1),
    /**
     * 07 excel
     */
    XLXS(2),
    /**
     * 文本
     */
    TEXT(3);

    /**
     * 类型
     */
    @Getter
    private int value;

    FileTypeEnum(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
