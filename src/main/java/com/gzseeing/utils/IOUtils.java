package com.gzseeing.utils;

import java.io.Closeable;


public class IOUtils {
	public final static void close(Closeable ...closes){
		if (closes!=null&&closes.length>0){
			for (Closeable closeable : closes) {
				try {
					if (closeable!=null){
						closeable.close();
					}
				} catch (Exception e) {
 				}
			}
		}
	}
}
