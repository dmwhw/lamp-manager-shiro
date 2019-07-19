package com.gzseeing.utils;

import org.springframework.context.ApplicationContext;

public class SpringContextUtils {

	private static ApplicationContext act;

	public static void init(ApplicationContext act2) {
		act = act2;
	}

	public static ApplicationContext getContext() {
		return act;
	}

}
