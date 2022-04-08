package com.hower.hotel.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hower.hotel.framework.model.BaseModel;

import java.time.LocalDateTime;

import com.hower.hotel.framework.model.convert.Convert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Ghower
 * @since 2021-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("customer_info")
public class CustomerInfo extends Convert {

    private static final long serialVersionUID = 1L;
    /**
     * 客户ID，也是身份证
     */
    private String id;
    /**
     * 客户会员身份
     */
    private Integer vId;

    /**
     * 客户姓名
     */
    private String name;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 性别
     */
    private String sex;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建者ID
     */
    private Integer createUid;

    /**
     * 修改者ID
     */
    private Integer updateUid;

    /**
     * 客户创建时间
     */
    private LocalDateTime createTime;

    /**
     * 客户信息更新时间
     */
    private LocalDateTime updateTime;


    public static final String V_ID = "v_id";

    public static final String NAME = "name";

    public static final String CONTACT = "contact";

    public static final String SEX = "sex";
    public static final String STATUS = "status";

    public static final String CREATE_UID = "create_uid";

    public static final String UPDATE_UID = "update_uid";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
