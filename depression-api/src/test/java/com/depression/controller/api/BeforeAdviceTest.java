package com.depression.controller.api;

import org.junit.Test;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.depression.interceptors.api.GreetingBeforeAdvice;
import com.depression.interceptors.api.NativeWaiter;
import com.depression.interceptors.api.Waiter;

public class BeforeAdviceTest {
	
	@Test
	public void before() {
		Waiter target = new NativeWaiter();
		BeforeAdvice advice = new GreetingBeforeAdvice();
		
		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(target);
		pf.addAdvice(advice);
		
		Waiter proxy = (Waiter)pf.getProxy();
		proxy.greetTo("John");
		proxy.serveTo("Tom");
	}
	
	@Test
	public void before1() {
		String path="applicationContext2.xml";
		ApplicationContext ctx= new ClassPathXmlApplicationContext(path);
		Waiter waiter = (Waiter)ctx.getBean("waiter");
		waiter.greetTo("John");
	}
}
