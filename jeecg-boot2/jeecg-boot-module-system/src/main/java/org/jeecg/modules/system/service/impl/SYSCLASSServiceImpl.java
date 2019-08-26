package org.jeecg.modules.system.service.impl;


import org.jeecg.modules.system.entity.SYSCLASS;
import org.jeecg.modules.system.mapper.SYSCLASSMapper;
import org.jeecg.modules.system.service.ISYSCLASSService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 班级表
 * @Author: jeecg-boot
 * @Date:   2019-08-26
 * @Version: V1.0
 */
@Service
public class SYSCLASSServiceImpl extends ServiceImpl<SYSCLASSMapper, SYSCLASS> implements ISYSCLASSService {

    @Override
    public SYSCLASS queryByClasss(String classs) {
        return null;
    }
}
