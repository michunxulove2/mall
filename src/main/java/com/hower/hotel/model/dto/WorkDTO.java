package com.hower.hotel.model.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hower.hotel.framework.model.convert.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class WorkDTO  {
    /**
     * 代码
     */
    @Excel(name = "工作代码", width = 30)
    private String code;
    /**
     * 内容
     */
    @Excel(name = "工作内容", width = 30)
    private String content;
    /**
     * 地址
     */
    @Excel(name = "工作地址", width = 30)
    private String address;
    /**
     * 联系方式
     */
    @Excel(name = "联系方式", width = 30)
    private String phone;
    /**
     *
     */
    @Excel(name = "工作类型", width = 30)
    private String type;
    /**
     * 完成时间
     */
    @Excel(name = "完成时间", width = 30)
    private LocalDateTime finishTime;
}
