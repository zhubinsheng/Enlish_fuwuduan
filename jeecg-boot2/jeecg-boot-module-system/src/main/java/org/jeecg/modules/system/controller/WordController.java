package org.jeecg.modules.system.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.Test;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.jeecg.Test.txt2String;

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
                                             @RequestParam(name="pageSize", defaultValue="30") Integer pageSize,
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
           if (pageList.getRecords().size()==0){
               pageList =wordService.selectWordPage(pageNo,pageSize, classify);
           }

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

    /**
     * 文件上传测试接口
     * @return
     */
    @RequestMapping(value="/upload",method = RequestMethod.POST)
    @ApiOperation("上传单词库")
    public Result<String> uploadFileTest(@RequestParam("uploadFile") MultipartFile file) throws Exception{
        Result<String> stringResult = new Result<>();

        File toFile = null;
        if(file.equals("")||file.getSize()<=0){
            file = null;
        }else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }


        int line = 0;
        StringBuilder resultString = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String classify = file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
        try{
            Map<String, String> map = new HashMap<String, String>();
            FileInputStream fis = new FileInputStream(toFile);
            InputStreamReader isr = new InputStreamReader(fis,"GBK");
            BufferedReader br = new BufferedReader(isr);
            String s = null;
            while((s = br.readLine())!=null){
                line++;

                if (s.endsWith("丨")){
                    resultString.append("<----------->").append(String.valueOf(line));
                    s = s + "空字符串";
                }else if (s==null||s.isEmpty()||s.equals("")){
                    resultString.append("<----------->").append(String.valueOf(line));
                    continue;
                }else if(!s.contains("丨")){
                    resultString.append("<----------->").append(String.valueOf(line));
                    continue;
                }
                String[] content = s.split("丨");

                if (content != null && content.length == 2) {
                    System.out.println(content[0]);
                    System.out.println(content[1]);
                    //map.put(content[0],content[1]);


                    if (content[0]==null||content[0].equals("")){
                        resultString.append("<----------->").append(String.valueOf(line));
                        content[0] = "空字符串";
                    }
                    if (content[1]==null){
                        resultString.append("<----------->").append(String.valueOf(line));
                        content[1] = "空字符串";
                    }
                    wordService.InsertWordPage(content[0],content[1], classify);

                }else if (content != null && content.length > 2){
                    resultString.append("<----------->").append(String.valueOf(line));
                    wordService.InsertWordPage(content[0],content[1], classify);
                }else{
                    int in = content[0].indexOf("|");
                    int last = s.length();
                    content[0] = s.substring(0,in);
                    String s2 = s.substring(in+1,last);

                    System.out.println(content[0]);
                    System.out.println(s2);

                    if (content[0]==null||content[0].equals("")){
                        resultString.append("<----------->").append(String.valueOf(line));
                        content[0] = "空字符串";
                    }
                    if (s2.isEmpty()){
                        resultString.append("<----------->").append(String.valueOf(line));
                        s2 = "空字符串";
                    }
                    wordService.InsertWordPage(content[0],s2, classify);
                }



                //write+= Test.JDBCAdd.Insert(content[0],content[1]);
                //result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        stringResult.setResult(String.valueOf(resultString));
        return stringResult;
    }



    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








}
