package com.aben.cup.sample.log;

import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.LogFactory;

public class LogTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  Log log = LogFactory.getLog(LogTest.class);
		  log.error("-----log test !-----");
	}

}
