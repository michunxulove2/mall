package com.hower.hotel.model.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hower.hotel.framework.model.convert.Convert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@TableName("t_work")
public class Work extends Convert {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private Integer id;
    /**
     * 代码
     */
    @Excel(name = "工作代码")
    private String code;
    /**
     * 内容
     */
    @Excel(name = "工作内容")
    private String content;
    /**
     * 地址
     */
    private String address;
    /**
     * 联系方式
     */
    private String phone;
    /**
     * 工作状态 1 已完成 2 进行中 3 未开始 4 管理人员点完成
     */
    private Integer status;
    /**
     *
     */
    private String type;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 利润
     */
    @Excel(name = "利润")
    private BigDecimal profits;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 创建人Id
     */
    private Integer userId;

    /**
     *
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 应包商id
     */
    private Integer allotId;

    /**
     *
     */
    @TableField(exist = false)
    private String allotName;
    /**
     * 应包商给出的价格
     */
    private BigDecimal allotPrice;
    /**
     * 应包商付款状态 1 已付款  2未付款
     */
    private Integer paymentStatus;

    private LocalDateTime allotFinishTime;


    public static final String ID = "id";

    public static final String CODE = "code";

    public static final String CONTENT = "content";

    public static final String AADDRESS = "address";

    public static final String PHONE = "phone";

    public static final String STATUS = "status";

    public static final String TYPE = "type";

    public static final String FINISH_TIME = "finish_time";

    public static final String PRICE = "price";

    public static final String PROFITES = "profits";

    public static final String CREATE_TIME = "create_time";

    public static final String USER_ID = "user_id";

    public static final String ALLOT_ID = "allot_id";

    public static final String ALLOT_PRICE = "allot_price";

    public static final String PAYMENT_STATUS = "payment_status";

    public static final String ALLOT_FINISH_TIME = "allot_finish_time";
}
