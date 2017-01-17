package com;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;

import com.aben.cup.log.logging.Log;
import com.aben.cup.log.logging.LogFactory;
import com.aben.cup.sample.log.LogTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisBillLockHandler implements IBatchBillLockHandler {  
	  
	private static final Log LOGGER = LogFactory.getLog(RedisBillLockHandler.class);
	
//    private static final Logger LOGGER = LoggerFactory.getLogger(RedisBillLockHandler.class);  
  
    private static final int DEFAULT_SINGLE_EXPIRE_TIME = 3;  
      
    private static final int DEFAULT_BATCH_EXPIRE_TIME = 6;  
  
    private final JedisPool jedisPool;  
      
    /** 
     * 构造 
     * @author http://blog.csdn.net/java2000_wl 
     */  
    public RedisBillLockHandler(JedisPool jedisPool) {  
        this.jedisPool = jedisPool;  
    }  
  
    /** 
     * 获取锁  如果锁可用   立即返回true，  否则返回false 
     * @author http://blog.csdn.net/java2000_wl 
     * @param billIdentify 
     * @return 
     */  
    public boolean tryLock(IBillIdentify billIdentify) {  
        return tryLock(billIdentify, 0L, null);  
    }  
  
    /** 
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false 
     * @author http://blog.csdn.net/java2000_wl 
     * @param billIdentify 
     * @param timeout 
     * @param unit 
     * @return 
     */  
    public boolean tryLock(IBillIdentify billIdentify, long timeout, TimeUnit unit) {  
        String key = (String) billIdentify.uniqueIdentify();  
        Jedis jedis = null;  
        try {  
            jedis = getResource();  
            long nano = System.nanoTime();  
            do {  
                LOGGER.debug("try lock key: " + key);  
                Long i = jedis.setnx(key, key);  
                if (i == 1) {   
                    jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);  
                    LOGGER.debug("get lock, key: " + key + " , expire in " + DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");  
                    return Boolean.TRUE;  
                } else { // 存在锁  
                    if (LOGGER.isDebugEnabled()) {  
                        String desc = jedis.get(key);  
                        LOGGER.debug("key: " + key + " locked by another business：" + desc);  
                    }  
                }  
                if (timeout == 0) {  
                    break;  
                }  
                Thread.sleep(300);  
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));  
            return Boolean.FALSE;  
        } catch (JedisConnectionException je) {  
            LOGGER.error(je.getMessage(), je);  
            returnBrokenResource(jedis);  
        } catch (Exception e) {  
            LOGGER.error(e.getMessage(), e);  
        } finally {  
            returnResource(jedis);  
        }  
        return Boolean.FALSE;  
    }  
  
    /** 
     * 如果锁空闲立即返回   获取失败 一直等待 
     * @author http://blog.csdn.net/java2000_wl 
     * @param billIdentify 
     */  
    public void lock(IBillIdentify billIdentify) {  
        String key = (String) billIdentify.uniqueIdentify();  
        Jedis jedis = null;  
        try {  
            jedis = getResource();  
            do {  
                LOGGER.debug("lock key: " + key);  
                Long i = jedis.setnx(key, key);  
                if (i == 1) {   
                    jedis.expire(key, DEFAULT_SINGLE_EXPIRE_TIME);  
                    LOGGER.debug("get lock, key: " + key + " , expire in " + DEFAULT_SINGLE_EXPIRE_TIME + " seconds.");  
                    return;  
                } else {  
                    if (LOGGER.isDebugEnabled()) {  
                        String desc = jedis.get(key);  
                        LOGGER.debug("key: " + key + " locked by another business：" + desc);  
                    }  
                }  
                Thread.sleep(300);   
            } while (true);  
        } catch (JedisConnectionException je) {  
            LOGGER.error(je.getMessage(), je);  
            returnBrokenResource(jedis);  
        } catch (Exception e) {  
            LOGGER.error(e.getMessage(), e);  
        } finally {  
            returnResource(jedis);  
        }  
    }  
  
    /** 
     * 释放锁 
     * @author http://blog.csdn.net/java2000_wl 
     * @param billIdentify 
     */  
    public void unLock(IBillIdentify billIdentify) {  
        List<IBillIdentify> list = new ArrayList<IBillIdentify>();  
        list.add(billIdentify);  
        unLock(list);  
    }  
  
    /** 
     * 批量获取锁  如果全部获取   立即返回true, 部分获取失败 返回false 
     * @author http://blog.csdn.net/java2000_wl 
     * @date 2013-7-22 下午10:27:44 
     * @param billIdentifyList 
     * @return 
     */  
    public boolean tryLock(List<IBillIdentify> billIdentifyList) {  
        return tryLock(billIdentifyList, 0L, null);  
    }  
      
    /** 
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false 
     * @author http://blog.csdn.net/java2000_wl 
     * @param billIdentifyList 
     * @param timeout 
     * @param unit 
     * @return 
     */  
    public boolean tryLock(List<IBillIdentify> billIdentifyList, long timeout, TimeUnit unit) {  
        Jedis jedis = null;  
        try {  
            List<String> needLocking = new CopyOnWriteArrayList<String>();    
            List<String> locked = new CopyOnWriteArrayList<String>();     
            jedis = getResource();  
            long nano = System.nanoTime();  
            do {  
                // 构建pipeline，批量提交  
                Pipeline pipeline = jedis.pipelined();  
                for (IBillIdentify identify : billIdentifyList) {  
                    String key = (String) identify.uniqueIdentify();  
                    needLocking.add(key);  
                    pipeline.setnx(key, key);  
                }  
                LOGGER.debug("try lock keys: " + needLocking);  
                // 提交redis执行计数  
                List<Object> results = pipeline.syncAndReturnAll();  
                for (int i = 0; i < results.size(); ++i) {  
                    Long result = (Long) results.get(i);  
                    String key = needLocking.get(i);  
                    if (result == 1) {  // setnx成功，获得锁  
                        jedis.expire(key, DEFAULT_BATCH_EXPIRE_TIME);  
                        locked.add(key);  
                    }   
                }  
                needLocking.removeAll(locked);  // 已锁定资源去除  
                  
                if (CollectionUtils.isEmpty(needLocking)) {  
                    return true;  
                } else {      
                    // 部分资源未能锁住  
                    LOGGER.debug("keys: " + needLocking + " locked by another business：");  
                }  
                  
                if (timeout == 0) {   
                    break;  
                }  
                Thread.sleep(500);    
            } while ((System.nanoTime() - nano) < unit.toNanos(timeout));  
  
            // 得不到锁，释放锁定的部分对象，并返回失败  
            if (!CollectionUtils.isEmpty(locked)) {  
                jedis.del(locked.toArray(new String[0]));  
            }  
            return false;  
        } catch (JedisConnectionException je) {  
            LOGGER.error(je.getMessage(), je);  
            returnBrokenResource(jedis);  
        } catch (Exception e) {  
            LOGGER.error(e.getMessage(), e);  
        } finally {  
            returnResource(jedis);  
        }  
        return true;  
    }  
  
    /** 
     * 批量释放锁 
     * @author http://blog.csdn.net/java2000_wl 
     * @param billIdentifyList 
     */  
    public void unLock(List<IBillIdentify> billIdentifyList) {  
        List<String> keys = new CopyOnWriteArrayList<String>();  
        for (IBillIdentify identify : billIdentifyList) {  
            String key = (String) identify.uniqueIdentify();  
            keys.add(key);  
        }  
        Jedis jedis = null;  
        try {  
            jedis = getResource();  
            jedis.del(keys.toArray(new String[0]));  
            LOGGER.debug("release lock, keys :" + keys);  
        } catch (JedisConnectionException je) {  
            LOGGER.error(je.getMessage(), je);  
            returnBrokenResource(jedis);  
        } catch (Exception e) {  
            LOGGER.error(e.getMessage(), e);  
        } finally {  
            returnResource(jedis);  
        }  
    }  
      
    /** 
     * @author http://blog.csdn.net/java2000_wl 
     * @date 2013-7-22 下午9:33:45 
     * @return 
     */  
    private Jedis getResource() {  
        return jedisPool.getResource();  
    }  
      
    /** 
     * 销毁连接 
     * @author http://blog.csdn.net/java2000_wl 
     * @param jedis 
     */  
    private void returnBrokenResource(Jedis jedis) {  
        if (jedis == null) {  
            return;  
        }  
        try {  
            //容错  
            jedisPool.returnBrokenResource(jedis);  
        } catch (Exception e) {  
            LOGGER.error(e.getMessage(), e);  
        }  
    }  
      
    /** 
     * @author http://blog.csdn.net/java2000_wl 
     * @param jedis 
     */  
    private void returnResource(Jedis jedis) {  
        if (jedis == null) {  
            return;  
        }  
        try {  
            jedisPool.returnResource(jedis);  
        } catch (Exception e) {  
            LOGGER.error(e.getMessage(), e);  
        }  
    }  