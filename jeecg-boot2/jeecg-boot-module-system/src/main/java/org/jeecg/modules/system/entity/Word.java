package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="单词", description="获取单词列表")
@TableName("cetfour")
public class Word implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "单词")
    private String word;
    @ApiModelProperty(value="单词意思")
    private String meaning;
    @ApiModelProperty(value="单词类别")
    private String classify;
}
