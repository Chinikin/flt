package com.depression.interceptors.aop;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

public class GreetingAdvisor extends StaticMethodMatcherPointcutAdvisor{

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return "greetTo".equals(method.getName());
	}

	public ClassFilter getClassFilter(){
		return new ClassFilter(){
			public boolean matches(Class clazz){
				return Waiter.class.isAssignableFrom(clazz);
			}
		};
	}
}
