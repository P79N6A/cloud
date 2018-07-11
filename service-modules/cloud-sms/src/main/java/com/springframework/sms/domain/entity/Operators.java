package com.springframework.sms.domain.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author summer
 * 2018/7/11 运行商
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_operators")
public class Operators extends Model<Operators> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 运营商名称
     */
    private String name;
    /**
     * 单价
     */
    private Double price;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
