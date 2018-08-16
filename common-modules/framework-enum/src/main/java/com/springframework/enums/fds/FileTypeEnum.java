package com.springframework.enums.fds;

import com.springframework.enums.IntValueDescription;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author summer
 * 2018/8/13
 */
public enum FileTypeEnum implements IntValueDescription {
    /**
     * 图片
     */
    PIC(0,"图片类"),
    /**
     * excel
     */
    XLS(1,"Excel文件"),
    /**
     * 07 excel
     */
    XLXS(2,"07Excel"),
    /**
     * 文本
     */
    TEXT(3,"text");

    /**
     * 类型
     */
    private final int value;
    private final String desc;
    private static List<FileTypeEnum> VALUES = EnumUtils.getEnumList(FileTypeEnum.class);
    FileTypeEnum(int value,String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "FileTypeEnum{" +
                "value=" + value +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static FileTypeEnum getFileTypeEnum(int value) {
        return Arrays
                .stream(FileTypeEnum.values())
                .filter(o -> o.getValue() == value)
                .findFirst()
                .orElse(null);
    }
}
