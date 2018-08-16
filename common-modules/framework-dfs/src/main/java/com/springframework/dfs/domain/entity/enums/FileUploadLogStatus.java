package com.springframework.dfs.domain.entity.enums;

import lombok.Getter;

/**
 * @author summer
 * 2018/8/13
 */
public enum FileUploadLogStatus {

    /**
     * 有效
     */
    VALID(0),
    /**
     * 无效
     */
    INVALID(1);
    @Getter
    private int value;

    FileUploadLogStatus(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
