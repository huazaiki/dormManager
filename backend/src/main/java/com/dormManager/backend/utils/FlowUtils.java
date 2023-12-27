package com.dormManager.backend.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtils {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 通过检查Redis中的键是否存在，实现一次性限制检查的方法。
     * 如果键存在，表示已经进行过限制检查，返回false；
     * 否则，将键设置到Redis中，并设置过期时间，返回true，表示可以进行限制检查。
     * 此方法防止用户重复提交发送验证码的请求
     *
     * @param key       Redis中用于限制检查的键
     * @param blockTime 键的过期时间，以秒为单位
     * @return 如果限制检查通过，返回true；否则返回false
     */
    public boolean limitOnceCheck(String key, int blockTime) {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            return false;
        } else {
            // 将键设置到Redis中，并设置过期时间
            stringRedisTemplate.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
            return true;
        }
    }
}
