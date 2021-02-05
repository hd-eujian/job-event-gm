package com.eujian.gm.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisTemplateService {
	private static Long SUCCESS =1L;
	@Autowired
	private RedisTemplate<String,String> redisStringTemplate;
	
	/**
	 * 采用lua 脚本 执行并发锁
	 * @param lockKey
	 * @param value
	 * @param expireSecondTime 锁时间（秒）
	 * @param retryTimes 重试次数
	 * @return
	 */
	public boolean getLock(String lockKey, String value, int expireSecondTime, int retryTimes) {
        if(expireSecondTime<=0) {
        	expireSecondTime=10;
        }
		String script = "if redis.call('setNx',KEYS[1],ARGV[1])  then " +
                "   if redis.call('get',KEYS[1])==ARGV[1] then " +
                "      return redis.call('expire',KEYS[1],ARGV[2]) " +
                "   else " +
                "      return 0 " +
                "   end " +
                "end";

        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        //对非string类型的序列化
        try {
        	int count = 0;
        	while(true) {
        		Object result = redisStringTemplate.execute(redisScript, Collections.singletonList(lockKey), value, String.valueOf(expireSecondTime));
                if(SUCCESS.equals(result)) {
                	return true;
                }else {
                	count++;
                	if (retryTimes == count) {
        				log.debug("has tried {} times , failed to acquire lock for key:{},value:{}", count, lockKey, value);
        				break;
        			} else {
        				log.warn("try to acquire lock {} times for key:{},value:{}", count, lockKey, value);
        				Thread.sleep(RandomUtils.nextInt(100,3000));
        			}
                }
        	}
        	
		} catch (Exception e) {
			log.error(" redis lock error "+e);
		}
        return false;
    }
	/**
	 * 释放锁
	 * @param lockKey
	 * @param value
	 * @return
	 */
	public boolean releaseLock(String lockKey, String value){
		try {
			String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

			RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
	        Object result = redisStringTemplate.execute(redisScript, Collections.singletonList(lockKey),value);
	        if(SUCCESS.equals(result)) {
	            return true;
	        }
		} catch (Exception e) {
			log.error(" release lock error "+e);
		}
        return false;
    }
	
	public void put(String key, String value) {
		redisStringTemplate.opsForValue().set(key, value);
	}

	public String get(String key) {
		return redisStringTemplate.opsForValue().get(key);
	}

	public void expire(String key, long timeout,TimeUnit timeUnit) {
		redisStringTemplate.expire(key, timeout, timeUnit);
	}

	public void delete(String key) {
		redisStringTemplate.delete(key);
	}
}
