package com.hower.hotel.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hower.hotel.framework.model.convert.Convert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_user_work")
public class UserWork extends Convert {

    private static final long serialVersionUID = 1L;

    /**
     * 应包商Id
     */
    private Integer userId;

    /**
     * 工作Id
     */
    private Integer workId;

    /**
     * 应包商 给出的价格
     */
    private BigDecimal price;

    /**
     * 付款状态 1 已付款 2 未付款
     */
    private Integer status;

    public static final String USERID = "userId";

    public static final String WORKID = "workId";

    public static final String PRICE = "price";

    public static final String STATUS = "status";

}
