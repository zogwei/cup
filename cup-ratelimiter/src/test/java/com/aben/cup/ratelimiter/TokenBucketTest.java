package com.aben.cup.ratelimiter;

import java.util.concurrent.TimeUnit;

import org.isomorphism.util.TokenBucket;
import org.isomorphism.util.TokenBuckets;

public class TokenBucketTest {

	public static void main(String[] args) {
		TokenBucket bucket = TokenBuckets.builder()
				  .withCapacity(1)
				  .withFixedIntervalRefillStrategy(1, 1, TimeUnit.SECONDS)
				  .build();
		
		bucket.consume(1);
		
		
		TokenBucket bucket2 = TokenBuckets.builder()
				  .withCapacity(40960)
				  .withFixedIntervalRefillStrategy(20480, 1, TimeUnit.SECONDS)
				  .build();

		bucket2.consume(10);
	}

}
