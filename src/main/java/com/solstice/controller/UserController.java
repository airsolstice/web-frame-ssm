package com.solstice.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.solstice.bean.User;
import com.solstice.exception.UserException;
import com.solstice.service.UserService;
import com.solstice.utils.JavaMailUtil;
import com.solstice.utils.Utils;



@Controller
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 验证输入合法性
	 * 
	 * @param user
	 * @return 错误map
	 */
	public Map<String, String> verifyInput(User user) {

		Map<String, String> errors = new HashMap<String, String>();
		// 用户名的判断
		if (Utils.isEmpty(user.getUserName())) {
			errors.put("userName", "用户名不能为null");
		} else if (user.getUserName().trim().length() > 10
				|| user.getUserName().trim().length() < 3) {
			errors.put("userName", "用户名长度必须介于3~10之间");
		}
		// 密码的判断
		if (Utils.isEmpty(user.getPwd())) {
			errors.put("pwd", "密码不能为null");
		} else if (user.getPwd().trim().length() > 18
				|| user.getPwd().trim().length() < 6) {
			errors.put("pwd", "密码长度必须介于6~18之间");
		}
		// 邮箱的判断
		if (Utils.isEmpty(user.getEmail())) {
			errors.put("email", "邮箱不能为null");
		} else if (!Utils.isEmail(user.getEmail().trim())) {// 邮箱格式为***@**.***
			errors.put("email", "邮箱格式不正确");
		}
		if (Utils.isEmpty(user.getPhone())) {
			errors.put("phone", "手机号码不能为null");
		} else if (!Utils.isPhone(user.getPhone().trim())) {// 邮箱格式为***@**.***
			errors.put("phone", "手机号码格式不正确");
		}
		return errors;
	}

	@RequestMapping("/toRegist")
	public String toRegist() {
		return "regist";
	}

	/*
	 * 注册 获取基本信息，补全user信息(激活码) 验证输入合法性 验证用户名和邮箱是否已经注册 发送激活码到邮箱 转向登录界面
	 */
	@RequestMapping("/regist")
	public String regist(HttpServletRequest request, User user) {
		// 补全代码
		String activeCode = UUID.randomUUID().toString();
		user.setActiveCode(activeCode);
		user.setPhone("18290209755");
		Map<String, String> errors = verifyInput(user);
		if (errors.size() > 0) {
			// 保存错误信息
			request.setAttribute("errors", errors);
			// 保存用户信息
			request.setAttribute("user", user);
			return "regist";
		}
		// 调用service，验证用户名和邮箱是否已经被注册
		try {
			userService.findUserByUserName(user.getUserName());
			userService.findUserByEmail(user.getEmail());
			userService.addUser(user);
			String text ="你正在使用该邮箱进行账号激活，请点击下面的链接完成激活；如不是本人操作，请忽略"+System.getProperty("line.separator")+ "<a href=\"http://localhost:8080"+request.getContextPath()+"/active?activeCode="
					+ user.getActiveCode() + "\"></a>";
			try {
				JavaMailUtil.sendMail("账号激活", text,
						new String[] { user.getEmail() });
			} catch (Exception e) {
				// 出错处理
				e.printStackTrace();
			}
		} catch (UserException e) {
			// 保存错误信息
			request.setAttribute("error", e.getMessage());
			// 保存用户信息
			request.setAttribute("user", user);
			return "regist";
		}
		request.setAttribute("message", "请到邮箱"+user.getEmail()+"完成激活操作。如已操作请点击下面的链接去登录"+System.getProperty("line.separator")
				+"<a href=\"http://localhost:8080"+request.getContextPath()+"/toLogin>点击这里去登录</a>");
		return "message";
	}

	/*
	 * 激活账号 根据激活码查找id 将查找到的id设置其status为1(激活状态)
	 */
	@RequestMapping("/active")
	public String active(String activeCode) {
		try {
			Integer id = userService.findIdByCode(activeCode);
			userService.active(id);
		} catch (UserException e) {
			// 激活失败处理
			e.printStackTrace();
		}
		return "login";

	}

	@RequestMapping("/toLogin")
	public String toLogin() {
		return "login";
	}

	/*
	 * 登录
	 */
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request, User user) {
		Map<String, String> errors = new HashMap<String, String>();
		// 用户名的判断
		if (Utils.isEmpty(user.getUserName())) {
			errors.put("userName", "用户名不能为null");
		} else if (user.getUserName().trim().length() > 10
				|| user.getUserName().trim().length() < 3) {
			errors.put("userName", "用户名长度必须介于3~10之间");
		}
		// 密码的判断
		if (Utils.isEmpty(user.getPwd())) {
			errors.put("pwd", "密码不能为null");
		} else if (user.getPwd().trim().length() > 18
				|| user.getPwd().trim().length() < 6) {
			errors.put("pwd", "密码长度必须介于6~18之间");
		}
		if (errors.size() > 0) {
			// 保存错误信息
			request.setAttribute("errors", errors);
			// 保存用户信息
			request.setAttribute("user", user);
			return "login";
		}
		try {
			User _user = userService.login(user);
			// 保存账号信息到session域中
			request.getSession().setAttribute("user", _user);
			return "index";
		} catch (UserException e) {
			request.setAttribute("error", e.getMessage());
			request.setAttribute("user", user);
			return "login";
		}
	}

	/*
	 * 去找回密码
	 */
	@RequestMapping("/toforgetpwd")
	public String toforgetpwd() {
		return "forgetpwd";
	}

	@RequestMapping("/forgetpwd")
	public String forgetpwd(HttpServletRequest request, String email, String pwd) {
		Map<String, String> errors = new HashMap<String, String>();
		if (Utils.isEmpty(email)) {
			errors.put("email", "邮箱不能为null");
		} else if (!Utils.isEmail(email)) {
			errors.put("email", "邮箱格式不正确");
		}
		// 密码的判断
		if (Utils.isEmpty(pwd)) {
			errors.put("pwd", "密码不能为null");
		} else if (pwd.trim().length() > 18 || pwd.trim().length() < 6) {
			errors.put("pwd", "密码长度必须介于6~18之间");
		}
		if(errors.size()>0){
			request.setAttribute("errors", errors);
			request.setAttribute("email", email);
			return "forgetpwd";
		}
		String text ="你正在使用该邮箱找回密码，请点击下面的链接完成密码找回；如不是本人操作，请忽略"+System.getProperty("line.separator")+"<a href=\"http://localhost:8080"+request.getContextPath()+"/retrievePwd?email="
				+ email + "&pwd=" + Utils.MD5(pwd)+ "\"></a>";
		try {
			JavaMailUtil.sendMail("找回密码", text, new String[] { email });
		} catch (Exception e) {
			// 出错处理
			e.printStackTrace();
		}
		//发送了消息之后应该转到一个信息提示页面，提示已经提交，请到邮箱去找回密码
		request.setAttribute("message", "请到邮箱"+email+"完成密码找回操作。如已操作请点击下面的链接去登录"+System.getProperty("line.separator")+"<a href=\"http://localhost:8080"+request.getContextPath()+"/toLogin");
		return "message";
	}

	// 找回密码
	@RequestMapping("/retrievePwd")
	public String retrievePwd(HttpServletRequest request,String email,String pwd) {
		try {
			userService.updatePwd(email,pwd);
			return "login";
		} catch (UserException e) {
//			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			return "forgetpwd";
		}
	}

	// 退出
	@RequestMapping(value = "/loginOut")
	public String loginOut(HttpServletRequest request) {
		// 返回登录界面
		return "login";
	}

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

}
