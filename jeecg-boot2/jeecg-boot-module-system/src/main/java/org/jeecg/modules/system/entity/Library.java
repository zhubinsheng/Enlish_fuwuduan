package org.jeecg.modules.system.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 类别表
 * @Author: jeecg-boot
 * @Date:   2019-08-27
 * @Version: V1.0
 */
@Data
@TableName("CETCLASSIFY")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CETCLASSIFY对象", description="类别表")
public class Library {
    
	/**classify*/
	@Excel(name = "classify", width = 15)
    @ApiModelProperty(value = "classify")
	private String classify;

	/**library*/
	@Excel(name = "library", width = 15)
    @ApiModelProperty(value = "library")
	private String library;

	/**
	 * id
	 */
	@TableId(type = IdType.UUID)
	private String id;
}
