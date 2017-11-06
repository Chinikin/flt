package com.depression.controller.aop;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.depression.interceptors.aop.Seller;
import com.depression.interceptors.aop.Waiter;

public class BeforeAdviceTest {
	
	
	@Test
	public void before1() {
		String path="applicationContext2.xml";
		ApplicationContext ctx= new ClassPathXmlApplicationContext(path);
		Waiter waiter = (Waiter)ctx.getBean("waiter");
		Seller seller=(Seller)ctx.getBean("seller");
		
		waiter.greetTo("John");
		waiter.serveTo("John");
		seller.greetTo("John");
	}
}
