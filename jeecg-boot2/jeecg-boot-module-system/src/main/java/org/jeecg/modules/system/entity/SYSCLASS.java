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
 * @Description: 班级表
 * @Author: jeecg-boot
 * @Date:   2019-08-26
 * @Version: V1.0
 */
@Data
@TableName("SYS_CLASS")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SYS_CLASS对象", description="班级表")
public class SYSCLASS {
    
	/**class*/
	@Excel(name = "class", width = 15)
    @ApiModelProperty(value = "class")
	private String classs;

	/**user id*/
	@ApiModelProperty(value = "userid")
	private String userid;


	/**user id*/
	@TableId(type = IdType.UUID)
	@ApiModelProperty(value = "uuid")
	private String uuid;


	private String allcount;
}
