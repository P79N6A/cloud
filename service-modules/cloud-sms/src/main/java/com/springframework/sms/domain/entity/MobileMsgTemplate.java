package com.springframework.sms.domain.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author summer
 * 2018/7/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@TableName("mobile_sms")
public class MobileMsgTemplate extends Model<MobileMsgTemplate> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 组装后的模板内容JSON字符串
     */
    private String context;
    /**
     * 短信通道
     */
    private String channel;
    /**
     * 短信类型(验证码或者通知短信)
     */
    private String type;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 短信模板
     */
    private String template;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
