package org.jeecg.modules.system.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qiniu.util.Auth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.LearningSit;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.model.SysRegisterInfoModel;
import org.jeecg.modules.system.model.SysRegisterModel;
import org.jeecg.modules.system.service.ILearningSitService;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/api")
@Api(tags="用户登录/注册")
@Slf4j
public class LoginController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ILearningSitService learningSitService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysLogService logService;
	@Autowired
    private RedisUtil redisUtil;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ApiOperation("登录接口")
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		SysUser sysUser = sysUserService.getUserByName(username);
		if(sysUser==null) {
			result.error500("该用户不存在");
			sysBaseAPI.addLog("登录失败，用户名:"+username+"不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}else {
			//密码验证
			String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
			String syspassword = sysUser.getPassword();
			if(!syspassword.equals(userpassword)) {
				result.error500("用户名或密码错误");
				return result;
			}
			String accessKey = "mG1JR-ivqabWIl6-t-69ZpGz-Cg57hFttKijlyBG";
			String secretKey = "Xvxt2TKg6vbcWsfr4IlHyU0eifSFIoOg5oMoWtJ9";
			String bucket = "english";
			Auth auth = Auth.create(accessKey, secretKey);
			String upToken = auth.uploadToken(bucket);
			//System.out.println(upToken);

			//生成token
			String token = JwtUtil.sign(username, syspassword);
			redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
			 //设置超时时间
			redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);

			LearningSit learningSit = new LearningSit();
			learningSit.setUserid(sysUser.getId());
			QueryWrapper<LearningSit> queryWrapper = QueryGenerator.initQueryWrapper(learningSit, null);
			List<LearningSit> employees = learningSitService.list(queryWrapper);

			JSONObject obj = new JSONObject();
			obj.put("token", token);
			obj.put("userInfo", sysUser);
			obj.put("learning",employees);
			obj.put("upToken",upToken);
			result.setResult(obj);
			result.success("登录成功");
			sysBaseAPI.addLog("用户名: "+username+",登录成功！", CommonConstant.LOG_TYPE_1, null);
		}
		return result;
	}
	/**
	 * 注册
	 *
	 */
	@RequestMapping(value="register",method = RequestMethod.POST)
	@ApiOperation("注册接口")
	public Result<JSONObject> register(@RequestBody SysRegisterModel registerModel){
		Result<JSONObject> result = new Result<JSONObject>();
		String username = registerModel.getUsername();
		String password = registerModel.getPassword();
		SysUser sysUser = sysUserService.getUserByName(username);
		try {


			if (sysUser != null) {
				result.error500("该用户名已被注册");
				sysBaseAPI.addLog("注册失败，用户名:"+username+"已存在！", CommonConstant.LOG_TYPE_1, null);
				return result;
			} else {
				sysUser=new SysUser();
				sysUser.setUsername(username);
				String salt = oConvertUtils.randomGen(8);
				sysUser.setSalt(salt);
				String userpassword = PasswordUtil.encrypt(username, password, salt);
				sysUser.setPassword(userpassword);
				String token = JwtUtil.sign(username, userpassword);
				redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
				//设置超时时间
				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);
				sysUser.setCreateTime(new Date());
				sysUser.setStatus(1);
				sysUser.setDelFlag("0");
				System.out.println("---create_by---"+sysUser.getCreateBy());
				sysUserService.save(sysUser);
				JSONObject obj = new JSONObject();
				obj.put("token", token);
				obj.put("userInfo", sysUser);
				result.setResult(obj);
				result.success("注册成功");
				sysBaseAPI.addLog("用户名: "+username+",注册成功！", CommonConstant.REGISTER_TYPE_1, null);
				return result;
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	 * 退出登录
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public Result<Object> logout(HttpServletRequest request,HttpServletResponse response) {
		//用户退出逻辑
		Subject subject = SecurityUtils.getSubject();
		LoginUser sysUser = (LoginUser)subject.getPrincipal();
		sysBaseAPI.addLog("用户名: "+sysUser.getRealname()+",退出成功！", CommonConstant.LOG_TYPE_1, null);
		log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
	    subject.logout();

	    String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
	    //清空用户Token缓存
	    redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
	    //清空用户权限缓存：权限Perms和角色集合
	    redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_ROLE + sysUser.getUsername());
	    redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_PERMISSION + sysUser.getUsername());
		return Result.ok("退出登录成功！");
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("loginfo")
	public Result<JSONObject> loginfo() {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		//update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
		// 获取一天的开始和结束时间
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date dayStart = calendar.getTime();
        calendar.add(calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
		// 获取系统访问记录
		Long totalVisitCount = logService.findTotalVisitCount();
		obj.put("totalVisitCount", totalVisitCount);
		Long todayVisitCount = logService.findTodayVisitCount(dayStart,dayEnd);
		obj.put("todayVisitCount", todayVisitCount);
		Long todayIp = logService.findTodayIp(dayStart,dayEnd);
		//update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
		obj.put("todayIp", todayIp);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}



	/**
	 * 注册
	 *
	 */
	@RequestMapping(value="registerInfo",method = RequestMethod.POST)
	@ApiOperation("完善资料接口")
	public Result<JSONObject> registerInfo(@RequestBody SysRegisterInfoModel registerModel){
		Result<JSONObject> result = new Result<JSONObject>();
		String classId = null;
		String avatar = null;
		if(registerModel.getClassId() != null && !registerModel.getClassId().isEmpty()){
			classId = registerModel.getClassId();
		}
		if(registerModel.getAvatar() != null && !registerModel.getAvatar().isEmpty()){
			avatar = registerModel.getAvatar();
		}
		int sex = 0 ;
		sex	= registerModel.getSex();
		SysUser sysUser;
		try {
			sysUser=new SysUser();
			sysUser.setId(registerModel.getId());
			sysUser.setClass_id(classId);

			//http://px32gts87.bkt.clouddn.com/FoT8zK_VViVINVMnK3KDF85Hxegt
			sysUser.setAvatar("http://px32gts87.bkt.clouddn.com/"+avatar);
			sysUser.setSex(sex);

				/*String salt = oConvertUtils.randomGen(8);
				sysUser.setSalt(salt);
				String userpassword = PasswordUtil.encrypt(username, password, salt);
				sysUser.setPassword(userpassword);
				String token = JwtUtil.sign(username, userpassword);
				redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
				//设置超时时间
				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME/1000);
				sysUser.setCreateTime(new Date());
				sysUser.setStatus(1);
				sysUser.setDelFlag("0");
				System.out.println("---create_by---"+sysUser.getCreateBy());*/


			sysUserService.updateById(sysUser);
			JSONObject obj = new JSONObject();
			//obj.put("token", token);
			obj.put("userInfo", sysUser);
			result.setResult(obj);
			result.success("完善成功");
			//sysBaseAPI.addLog("用户名: "+username+",注册成功！", CommonConstant.REGISTER_TYPE_1, null);
			return result;

		}catch (Exception e){
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}
}
