package org.jeecg.modules.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.Word;
import org.jeecg.modules.system.mapper.WordMapper;
import org.jeecg.modules.system.service.IWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements IWordService {
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    @Lazy
    private RedisUtil redisUtil;
    public IPage<Word> selectWordPage(Integer pageNo,Integer pageSize, String classify){
        Page<Word> page=new Page<Word>(pageNo,pageSize);

        IPage<Word>result=wordMapper.selectPage(page,classify);
        redisUtil.set("WordCache::selectWordPage"+pageNo+pageSize+classify,result);
        return result;
    }
    public Set<String> selectClassify(){

        Set<String> result=wordMapper.selectClassify();
        redisUtil.set("WordCache::selectClassify",result);
        return result;
    }
}
