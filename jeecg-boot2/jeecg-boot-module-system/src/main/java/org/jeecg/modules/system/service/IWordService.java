package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.Word;

import java.util.List;
import java.util.Set;

public interface IWordService extends IService<Word> {
    public List<Word> selectWordPage(Integer pageNo,Integer pageSize,String classify);
    public Set<String> selectClassify();
    public void InsertWordPage(String word, String meaning, String classify);
}
