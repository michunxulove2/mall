package com.hower.hotel.model.entity;

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
    private String code;
    /**
     * 内容
     */
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
     * 工作类型 1 已发放工作 2 紧急工作 3 已超完成时间工作 4 已完成工作 5 未申请工作
     */
    private Integer type;
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



    public static final String ID = "id";

    public static final String CODE = "code";

    public static final String CONTENT = "content";

    public static final String AADDRESS = "address";

    public static final String PHONE = "phone";

    public static final String STATUS = "status";

    public static final String TYPE = "type";

    public static final String FINISHTIME = "finishTime";

    public static final String PRICE = "price";

    public static final String PROFITES = "profits";

    public static final String CREATETIME = "createTime";

    public static final String USERID = "userId";

    public static final String ALLOTID = "allotId";

    public static final String ALLOTPRICE = "allotPrice";

    public static final String PAYMENTSTATUS = "paymentStatus";


}
