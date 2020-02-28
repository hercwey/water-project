package com.learnbind.ai.controller.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.bean.MeterBean;
import com.learnbind.ai.constant.SessionConstant;
import com.learnbind.ai.model.ModuleProductNo;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.service.moduleproductno.ModuleProductNoService;
import com.learnbind.ai.service.user.UsersService;

import tk.mybatis.mapper.util.StringUtil;

/**
 * @author lenovo
 * 用于手机客户端登录及下载表册
 */

//@Controller
//@RequestMapping(value = "/api")

@RestController // 使用该注解后相当于每个控制器方法自动添加@ResponseBody注解
@RequestMapping("/api")
public class MobileScanController {
	private static Log log = LogFactory.getLog(MobileScanController.class);	
	
	@Autowired
	private UsersService usersService;//用户
	@Autowired
	private ModuleProductNoService moduleProductNoService;//模组号-出厂编号对照表
	
	/**
	 * @Title: login
	 * @Description: APP用户登录
	 * @param request
	 * @param model
	 * @param username
	 * @param password
	 * @return 
	 */
	@RequestMapping(value = "/user/login")	
	public ResponseEntity<String> login(HttpServletRequest request, Model model, String username, String password) {
		final String LOGIN_OK="1";      //登录正确常量
		final String LOGIN_ERROR="-1";  //登录错误常量
		
		//服务器端用户名及口令校验规则
		if(StringUtil.isEmpty(username)  || StringUtil.isEmpty(password)) {
			return new ResponseEntity<>(LOGIN_ERROR, HttpStatus.OK);
		}		
		
		log.debug("====================开始验证");		
		
		//数据库的口令是加密的口令.
		String encoderPassword = DigestUtils.md5Hex(password.toString()).toUpperCase();
		
		SysUsers user = new SysUsers();
		user.setUsername(username);
		user.setPassword(encoderPassword);
		//user.setPassword(password);
		
		List<SysUsers> userList = usersService.select(user);
		if(userList!=null && userList.size()==1){
			log.debug("====================验证正确");
			
			user = userList.get(0);
			HttpSession session = request.getSession();
			session.setAttribute(SessionConstant.USER, user);
			return new ResponseEntity<>(String.valueOf(user.getId()), HttpStatus.OK);
			
		}
		else {
			log.debug("====================验证错误");			
			//model.addAttribute("error_msg", "用户名或密码错误！");
			return new ResponseEntity<>(LOGIN_ERROR, HttpStatus.OK);
		}
		
	}
	
	/**
	 * @Title: scanResult
	 * @Description: 保存扫描结果
	 * @param request
	 * @param model
	 * @param moduleNo
	 * @param operatorId
	 * @return 
	 */
	@RequestMapping(value = "/user/scanresult")	
	public ResponseEntity<String> scanResult(HttpServletRequest request, Model model, String moduleNo, Long operatorId) {
		final String OK="OK";      //保存成功常量
		final String ERROR="ERROR";  //保存失败常量
		
		Date sysDate = new Date();//系统日期
		try {
			SysUsers user = usersService.selectByPrimaryKey(operatorId);//登录用户
			
			ModuleProductNo mpNo = new ModuleProductNo();//模组号-出厂编号对照表
			mpNo.setModuleNo(moduleNo);
			mpNo.setOperatorId(operatorId);
			mpNo.setOperatorName(user.getRealname());
			mpNo.setOperatorDate(sysDate);
			
			int rows = moduleProductNoService.insertSelective(mpNo);
			if(rows>0) {
				log.info("----------保存扫描结果成功");
				return new ResponseEntity<>(OK, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("----------保存扫描结果异常");
		return new ResponseEntity<>(ERROR, HttpStatus.OK);
		
	}

	/**
	 * @Title: main
	 * @Description: main方法
	 * @param args 
	 */
	public static void main(String[] args) {
		List<MeterBean> list = new ArrayList<MeterBean>();
		
		for(int i=10; i<20; i++) {
			MeterBean meter1 = new MeterBean();
			meter1.setAddr("0"+i);
			list.add(meter1);
			for(int j=30; j<40; j++) {
				MeterBean meter = new MeterBean();
				meter.setAddr("0"+j);
				list.add(meter);
			}
		}
		
		
		for(MeterBean bean : list) {
			System.out.print(bean.getAddr()+" ");
		}
		
		//sort(list);
		
		System.out.println(" ");
		for(MeterBean bean : list) {
			System.out.print(bean.getAddr()+" ");
		}
		
		
		String str = "/home/upload/app img/20190724/2019072419474649719058.jpg";
		System.out.println(str.substring(str.indexOf("upload")));
		
	}
	
}