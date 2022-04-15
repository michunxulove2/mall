package com.hower.hotel.model.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
public class WorkDTO1 {
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
     *
     */
    private String type;
    /**
     * 完成时间
     */
    private String finishTime;
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

}
