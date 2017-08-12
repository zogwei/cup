package com.aben.cup.sample.log;

import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.LogFactory;

public class LogTest {

	private final static Log log = LogFactory.getLog(LogTest.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  
		  log.error("-----log test !-----");
	}

}
