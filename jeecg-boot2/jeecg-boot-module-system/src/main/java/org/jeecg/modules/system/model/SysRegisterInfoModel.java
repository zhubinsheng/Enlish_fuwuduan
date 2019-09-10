package org.jeecg.modules.system.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 注册表单
 *
 * @Author scott
 * @since  2019-01-18
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@ApiModel(value="注册对象", description="注册对象")
public class SysRegisterInfoModel {
    @ApiModelProperty(value = "班级")
    private String classId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "性别（1：男 2：女）")
    private int sex;

}