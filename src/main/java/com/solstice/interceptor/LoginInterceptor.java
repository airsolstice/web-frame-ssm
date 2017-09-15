package com.solstice.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.solstice.bean.User;

/**
 * 登陆拦截器
 * 
 * @author
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(LoginInterceptor.class);

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * return :false——不往下执行了 true——执行下一个拦截器或者处理器
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		String requestUri = request.getRequestURI();
		//uri中包含loginout
		if (requestUri.contains("loginOut")) {
			//让session失效
			request.getSession().invalidate();
			//重定向到login
			response.sendRedirect(request.getContextPath() + "/login");

			return false;

		}
		// 从session域中获取user值
		User user = (User) request.getSession().getAttribute("user");
		System.out.println(user);
		String[] notInterceptStr = {"/toRegist", "/regist", "/active","/login","/loginOut","/toforgetpwd","/forgetpwd","/retrievePwd" };
		
		for (String notIntercept : notInterceptStr) {
			if(requestUri.contains(notIntercept)){
				return true;
			}
			
		}
			if (requestUri.contains("/toLogin")) {
				// 用户已经登录
				if (null != user) {
					logger.debug("用户访问了登陆页面，但session不为空,返回index页面");
					// 重定向到index
					response.sendRedirect(request.getContextPath() + "/index");
					return false;

				} else {

					return true;

				}

			}

		if (null == user) {

			logger.debug("用户访问了其他页面，但session为空,条向登陆页面");

			response.sendRedirect(request.getContextPath() + "/toLogin");

			return false;

		}
		return true;
	}





}
