package org.jeecg.modules.system.model;

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
 * @Description: 学习情况记录表
 * @Author: jeecg-boot
 * @Date:   2019-08-29
 * @Version: V1.0
 */
@Data
@TableName("LEARNONG_SIT")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LEARNONG_SIT对象", description="学习情况记录表")
public class LearningSit {
    
	/**user id*/
	//@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "userid")
	private String userid;

	/**classifyId*/
	@Excel(name = "classifyId", width = 15)
    @ApiModelProperty(value = "classifyId")
	private String classifyId;

	/**count*/
	@Excel(name = "count", width = 15)
    @ApiModelProperty(value = "count")
	private String count;


	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "uuid")
	private String uuid;

}
