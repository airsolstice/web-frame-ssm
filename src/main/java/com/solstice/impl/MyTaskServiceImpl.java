package com.solstice.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.solstice.service.MyTaskService;


@Service("myTaskService")
@Lazy(value=false)
public class MyTaskServiceImpl implements MyTaskService,ApplicationContextAware{
	public void check(){
		System.out.println("check……");
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		
	}
}
