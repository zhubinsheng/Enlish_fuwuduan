package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.Word;

import java.util.List;
import java.util.Set;


public interface WordMapper extends BaseMapper<Word> {
    List<Word> selectPage(@Param("classify")String classify);
    Set<String> selectClassify();

    void InsertWordPage(@Param("word")String word, @Param("meaning")String meaning, @Param("classify")String classify);

}
