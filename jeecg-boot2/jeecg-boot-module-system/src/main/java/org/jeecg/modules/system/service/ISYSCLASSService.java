package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SYSCLASS;

/**
 * @Description: 班级表
 * @Author: jeecg-boot
 * @Date:   2019-08-26
 * @Version: V1.0
 */
public interface ISYSCLASSService extends IService<SYSCLASS> {

    SYSCLASS queryByClasss(String classs);
}
