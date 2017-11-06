package com.depression.interceptors.api;

public class NativeWaiter implements Waiter{

	@Override
	public void greetTo(String name) {
		System.out.println("greet to "+name+" ...");
	}

	@Override
	public void serveTo(String name) {
		System.out.println("serving to "+name+" ...");
	}

}
