package com.cup.aben.lock;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonLock {

	public static void main(String[] args) throws Exception {
		Config config =new Config();
		
        RedissonClient redisson = Redisson.create(config);

        RLock lock = redisson.getLock("haogrgr");
        lock.lock();
        lock.lock(100, TimeUnit.MILLISECONDS);
        try {
            System.out.println("hagogrgr");
        }
        finally {
            lock.unlock();
        }

        redisson.shutdown();
    }
}
