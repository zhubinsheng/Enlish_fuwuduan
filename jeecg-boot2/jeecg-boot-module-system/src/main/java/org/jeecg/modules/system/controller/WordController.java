package org.jeecg.modules.system.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.Word;
import org.jeecg.modules.system.service.IWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api")
@Api(tags="单词列表")
public class WordController {
    @Autowired
    private IWordService wordService;
    @Autowired
    @Lazy
    private RedisUtil redisUtil;
    @RequestMapping(value="/word",method = RequestMethod.POST)
    @ApiOperation("获取单词列表")
    public Result<IPage<Word>> queryPageList(Word word,
                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                             @RequestParam(name="classify",defaultValue = "gmat2500")String classify,
                                             HttpServletRequest req){
        Result<IPage<Word>> result = new Result<IPage<Word>>();
       IPage<Word>pageList=null;
        Page<Word> page = new Page<Word>(pageNo, pageSize);
       if(!redisUtil.hasKey("WordCache::selectWordPage"+pageNo+pageSize+classify)) {


           pageList =wordService.selectWordPage(pageNo,pageSize, classify);
       }else{
           System.out.println("从redis缓存取数据");
           pageList=(IPage<Word>)redisUtil.get("WordCache::selectWordPage"+pageNo+pageSize+classify);

       }
        result.setSuccess(true);
        result.setResult((IPage<Word>) pageList);
        return result;
    }
    @RequestMapping(value="/classify",method = RequestMethod.POST)
    @ApiOperation("获取单词类别列表")
    public Result<Set<String>> queryClassify(Word word){

        Result<Set<String>> result=new Result<Set<String>>();
        if(!redisUtil.hasKey("WordCache::selectClassify")) {
            result.setResult(wordService.selectClassify());
        }else {
            System.out.println("classify缓存数据获取");
            result.setResult((Set<String>)redisUtil.get("WordCache::selectClassify"));
        }
        result.setSuccess(true);
        return result;

    }

}
