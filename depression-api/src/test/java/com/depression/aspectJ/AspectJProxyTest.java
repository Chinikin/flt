package com.depression.aspectJ;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import com.depression.interceptors.api.NativeWaiter;
import com.depression.interceptors.api.Waiter;

public class AspectJProxyTest {
	
	public static void main(String[] args) {
		
		Waiter target =  new NativeWaiter();
		AspectJProxyFactory factory =  new AspectJProxyFactory();
		
		factory.setTarget(target);
		factory.addAspect(PreGreetingAspect.class);
		
		Waiter proxy =  factory.getProxy();
		proxy.greetTo("John");
		proxy.serveTo("John");
	}
	
}

