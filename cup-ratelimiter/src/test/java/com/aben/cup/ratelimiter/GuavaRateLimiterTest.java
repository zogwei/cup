package com.aben.cup.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;

public class GuavaRateLimiterTest {
	
	static final RateLimiter rateLimiter = RateLimiter.create(2.0); // rate is "2 permits per second"
	
	public static void testRate() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rateLimiter.tryAcquire()) {
			System.out.println("ok!");
		}
		else{
			System.out.println("too manny!");
		}
	}

	public static void main(String[] args) {
		for(int i = 0 ; i<3 ;i++){
			Thread t = new Thread(){
				public void run(){
					while(true){
						testRate();
					}
				}
			};
			t.start();
		}
		
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
