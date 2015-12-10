package com.chungang.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println(ctx.getBeansOfType(IHandler.class));
//		String[] beanNames = ctx.getBeanNamesForType(AbstractHandler.class);
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
//		System.out.println(((Client)ctx.getBeansOfType(Client.class).get("client")).getHander());
//		System.out.println(((Client)ctx.getBeansOfType(Client.class).get(key)).getHander());
	}
}
