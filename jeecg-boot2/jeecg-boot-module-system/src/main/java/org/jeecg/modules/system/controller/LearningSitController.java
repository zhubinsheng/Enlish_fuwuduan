package org.jeecg.modules.system.controller;

import java.util.*;
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
import org.jeecg.modules.system.service.ILearningSitService;
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
 * @Description: 学习情况记录表
 * @Author: jeecg-boot
 * @Date:   2019-08-29
 * @Version: V1.0
 */
@Slf4j
@Api(tags="学习情况记录表")
@RestController
@RequestMapping("/demo/learningSit")
public class LearningSitController {
	@Autowired
	private ILearningSitService learningSitService;

	 @Autowired
	 private ISYSCLASSService sYSCLASSService;
	/**
	  * 分页列表查询
	 * @param learningSit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "学习情况记录表-分页列表查询")
	@ApiOperation(value="学习情况记录表-分页列表查询", notes="学习情况记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<LearningSit>> queryPageList(LearningSit learningSit,
													@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
													HttpServletRequest req) {
		Result<IPage<LearningSit>> result = new Result<IPage<LearningSit>>();
		QueryWrapper<LearningSit> queryWrapper = QueryGenerator.initQueryWrapper(learningSit, req.getParameterMap());
		Page<LearningSit> page = new Page<LearningSit>(pageNo, pageSize);
		IPage<LearningSit> pageList = learningSitService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param learningSit
	 * @return
	 */
	@AutoLog(value = "学习情况记录表-添加")
	@ApiOperation(value="学习情况记录表-添加", notes="学习情况记录表-添加")
	@PostMapping(value = "/add")
	public Result<LearningSit> add(@RequestBody LearningSit learningSit) {
		//learningSitService.getById(learningSit.getId());
		LearningSit learningSit2 = new LearningSit();
		learningSit2.setUserid(learningSit.getUserid());
		QueryWrapper<LearningSit> queryWrapper = QueryGenerator.initQueryWrapper(learningSit2, null);
		List<LearningSit> employees = learningSitService.list(queryWrapper);

		List<LearningSit> list = employees.stream()
				.filter((LearningSit b) -> b.getClassifyId().equals(learningSit.getClassifyId()))
				.collect(Collectors.toList());

		Result<LearningSit> result = new Result<LearningSit>();
		try {
			if (list.size()==0){
				learningSitService.save(learningSit);
				result.success("添加成功！");
			}else {
				learningSit.setUuid(list.get(0).getUuid());
				int cpunt = Integer.parseInt(list.get(0).getCount());
				int cpunt2 = Integer.parseInt(learningSit.getCount());
				String str = String.valueOf(cpunt+cpunt2);
				learningSit.setCount(String.valueOf(cpunt+cpunt2));
				learningSitService.updateById(learningSit);
				result.success("更新成功！");
				/*synchronized (this) {
					new Thread(new Runnable() {
						@Override
						public void run() {*/
							SYSCLASS sYSCLASS = new SYSCLASS();
							sYSCLASS.setUserid(learningSit.getUserid());
							QueryWrapper<SYSCLASS> queryWrapper2 = QueryGenerator.initQueryWrapper(sYSCLASS, null);
							List<SYSCLASS> employees2 = sYSCLASSService.list(queryWrapper2);
							if (employees2.size()!=0){
								employees2.stream().forEach(a -> a.setAllcount(String.valueOf(cpunt2+Integer.parseInt(a.getAllcount()))));
								//Collection<SYSCLASS> sysclassCollection = new C
								Collection<SYSCLASS> list2 = new ArrayList<>();
								list2.addAll(employees2);

								boolean ok2 = sYSCLASSService.updateById(employees2.get(0));

								boolean ok =  sYSCLASSService.updateBatchById(list2);
							}

				/*		}
					}).start();
				}*/


			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		//new Thread( () -> System.out.println("In Java8, Lambda expression rocks !!") ).start();





		return result;
	}
	
	/**
	  *  编辑
	 * @param learningSit
	 * @return
	 */
	@AutoLog(value = "学习情况记录表-编辑")
	@ApiOperation(value="学习情况记录表-编辑", notes="学习情况记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<LearningSit> edit(@RequestBody LearningSit learningSit) {
		Result<LearningSit> result = new Result<LearningSit>();
		LearningSit learningSitEntity = learningSitService.getById(learningSit.getUserid());
		if(learningSitEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = learningSitService.updateById(learningSit);
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
	@AutoLog(value = "学习情况记录表-通过id删除")
	@ApiOperation(value="学习情况记录表-通过id删除", notes="学习情况记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<LearningSit> delete(@RequestParam(name="id",required=true) String id) {
		Result<LearningSit> result = new Result<LearningSit>();
		LearningSit learningSit = learningSitService.getById(id);
		if(learningSit==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = learningSitService.removeById(id);
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
	@AutoLog(value = "学习情况记录表-批量删除")
	@ApiOperation(value="学习情况记录表-批量删除", notes="学习情况记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<LearningSit> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<LearningSit> result = new Result<LearningSit>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.learningSitService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学习情况记录表-通过id查询")
	@ApiOperation(value="学习情况记录表-通过id查询", notes="学习情况记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<List<LearningSit>> queryById(@RequestParam(name="id",required=true) String id) {
		Result<List<LearningSit>> result = new Result<List<LearningSit>>();

		LearningSit learningSit = new LearningSit();
		learningSit.setUserid(id);
		QueryWrapper<LearningSit> queryWrapper = QueryGenerator.initQueryWrapper(learningSit, null);
		List<LearningSit> employees = learningSitService.list(queryWrapper);



		if(employees==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(employees);
			result.setSuccess(true);
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
      QueryWrapper<LearningSit> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              LearningSit learningSit = JSON.parseObject(deString, LearningSit.class);
              queryWrapper = QueryGenerator.initQueryWrapper(learningSit, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<LearningSit> pageList = learningSitService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "学习情况记录表列表");
      mv.addObject(NormalExcelConstants.CLASS, LearningSit.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("学习情况记录表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<LearningSit> listLearningSits = ExcelImportUtil.importExcel(file.getInputStream(), LearningSit.class, params);
              for (LearningSit learningSitExcel : listLearningSits) {
                  learningSitService.save(learningSitExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listLearningSits.size());
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
