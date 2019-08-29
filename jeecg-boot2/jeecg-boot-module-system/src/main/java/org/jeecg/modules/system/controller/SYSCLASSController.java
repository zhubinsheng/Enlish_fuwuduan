package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.SYSCLASS;
import org.jeecg.modules.system.model.LearningSit;
import org.jeecg.modules.system.service.ISYSCLASSService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 班级表
 * @Author: jeecg-boot
 * @Date:   2019-08-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="班级表")
@RestController
@RequestMapping("/demo/sYSCLASS")
public class SYSCLASSController {
	@Autowired
	private ISYSCLASSService sYSCLASSService;
	
	/**
	  * 分页列表查询
	 * @param sYSCLASS
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "班级表-分页列表查询")
	@ApiOperation(value="班级表-分页列表查询", notes="班级表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SYSCLASS>> queryPageList(SYSCLASS sYSCLASS,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SYSCLASS>> result = new Result<IPage<SYSCLASS>>();
		QueryWrapper<SYSCLASS> queryWrapper = QueryGenerator.initQueryWrapper(sYSCLASS, req.getParameterMap());
		Page<SYSCLASS> page = new Page<SYSCLASS>(pageNo, pageSize);
		IPage<SYSCLASS> pageList = sYSCLASSService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param classs
	 * @return
	 */
	@AutoLog(value = "班级表-添加")
	@ApiOperation(value="班级表-添加", notes="班级表-添加")
	@PostMapping(value = "/add")
	public Result<SYSCLASS> add(@RequestParam(name="classs") String classs,@RequestParam(name="userID") String userID) {
		SYSCLASS sYSCLASS = new SYSCLASS();
		sYSCLASS.setClasss(classs);
		sYSCLASS.setUserid(userID);


        QueryWrapper<SYSCLASS> queryWrapper = QueryGenerator.initQueryWrapper(sYSCLASS, null);
        List<SYSCLASS> employees = sYSCLASSService.list(queryWrapper);

        List<SYSCLASS> list = employees.stream()
                .filter((SYSCLASS b) -> b.getClasss().equals(classs))
                .collect(Collectors.toList());

		Result<SYSCLASS> result = new Result<SYSCLASS>();
		try {
		    if (list.size()!=0){
                result.success("请勿重复添加");
            }else {
                sYSCLASSService.save(sYSCLASS);
                result.success("添加成功！");
            }

		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sYSCLASS
	 * @return
	 */
	@AutoLog(value = "班级表-编辑")
	@ApiOperation(value="班级表-编辑", notes="班级表-编辑")
	@PutMapping(value = "/edit")
	public Result<SYSCLASS> edit(@RequestBody SYSCLASS sYSCLASS) {
		Result<SYSCLASS> result = new Result<SYSCLASS>();
		SYSCLASS sYSCLASSEntity = sYSCLASSService.getById(sYSCLASS.getUserid());
		if(sYSCLASSEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sYSCLASSService.updateById(sYSCLASS);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班级表-通过id删除")
	@ApiOperation(value="班级表-通过id删除", notes="班级表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<SYSCLASS> delete(@RequestParam(name="id",required=true) String id) {
		Result<SYSCLASS> result = new Result<SYSCLASS>();
		SYSCLASS sYSCLASS = sYSCLASSService.getById(id);
		if(sYSCLASS==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sYSCLASSService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "班级表-批量删除")
	@ApiOperation(value="班级表-批量删除", notes="班级表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<SYSCLASS> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SYSCLASS> result = new Result<SYSCLASS>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sYSCLASSService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过classs查询
	 * @param classs
	 * @return
	 */
	@AutoLog(value = "班级表-通过classs查询")
	@ApiOperation(value="班级表-通过classs查询", notes="班级表-通过classs查询")
	@GetMapping(value = "/queryByClasss")
	public Result<List<SYSCLASS>> queryById(@RequestParam(name="classs") String classs) {
		Result<List<SYSCLASS>> result = new Result<List<SYSCLASS>>();
		SYSCLASS sYSCLASS = new SYSCLASS();
		//sYSCLASS.setClasss(classs);
		QueryWrapper<SYSCLASS> queryWrapper = QueryGenerator.initQueryWrapper(sYSCLASS, null);
		List<SYSCLASS> employees = sYSCLASSService.list(queryWrapper);
		// 使用lambda表达式过滤出结果并放到result列表里
		synchronized (classs){
		List<SYSCLASS> list = employees.stream()
				.filter((SYSCLASS b) -> b.getClasss().contains(classs))
				.collect(Collectors.toList());

			if(list.size()==0) {
				result.error500("未找到对应实体");
			}else {
				result.setResult(list);
				result.setSuccess(true);
			}
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<SYSCLASS> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SYSCLASS sYSCLASS = JSON.parseObject(deString, SYSCLASS.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sYSCLASS, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SYSCLASS> pageList = sYSCLASSService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "班级表列表");
      mv.addObject(NormalExcelConstants.CLASS, SYSCLASS.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("班级表列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<SYSCLASS> listSYSCLASSs = ExcelImportUtil.importExcel(file.getInputStream(), SYSCLASS.class, params);
              for (SYSCLASS sYSCLASSExcel : listSYSCLASSs) {
                  sYSCLASSService.save(sYSCLASSExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listSYSCLASSs.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
