package com.wegood.core.web.security.authentication.jwt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Rain
 */
@Service
public class JwtCacheService {

    /**
     * 登录的最大版本号
     */
    private static final String MAX_VERSION_KEY = "jwt:{userId}:maxVersion";

    /**
     * 当被验证的 token 的 version 小于等于本值时，被验证的 token 强制失效
     */
    private static final String FORCE_OFFLINE_VERSION_KEY = "jwt:{userId}:forceOfflineVersion";

    /**
     * 各终端最大的 version
     */
    private static final String END_ONLINE_MAX_VERSION_KEY = "jwt:{userId}:{end}:maxVersion";

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;

    public Integer increaseVersionByEnd(String userId, String end) {
        String maxVersionKey = formatMaxVersionKey(userId);

        int nextVersion = redisTemplate.opsForValue().increment(maxVersionKey, 1).intValue();
        redisTemplate.expire(maxVersionKey, JwtUtil.TOKEN_EFFECTIVE_MILLS, TimeUnit.MILLISECONDS);

        redisTemplate.opsForValue().set(formatEndOnlineMaxVersionKey(userId, end), nextVersion, JwtUtil.TOKEN_EFFECTIVE_MILLS, TimeUnit.MILLISECONDS);

        return nextVersion;
    }

    public Integer getMaxVersion(String userId) {
        return redisTemplate.opsForValue().get(formatMaxVersionKey(userId));
    }

    public Integer getForceOfflineVersion(String userId) {
        return redisTemplate.opsForValue().get(formatForceOfflineVersionKey(userId));
    }

    public Integer getVersionByEnd(String userId, String end) {
        Assert.notBlank(userId);
        Assert.notBlank(end);
        return redisTemplate.opsForValue().get(formatEndOnlineMaxVersionKey(userId, end));
    }

    /**
     * 根据终端列表获取 version
     */
    public Map<String, Integer> getVersionByEnds(String userId, List<String> ends) {
        Assert.notBlank(userId);
        Assert.notEmpty(ends);
        List<String> keys = new ArrayList<>(ends.size());
        for (String end : ends) {
            Assert.notBlank(end, "终端名称必填");
            keys.add(formatEndOnlineMaxVersionKey(userId, end));
        }
        List<Integer> version = redisTemplate.opsForValue().multiGet(keys);

        Map<String, Integer> result = CollUtil.newHashMap(ends.size());
        for (int i = 0; i < keys.size(); i++) {
            result.put(ends.get(i), version.get(i));
        }

        return result;
    }

    private String formatMaxVersionKey(String userId) {
        return MAX_VERSION_KEY.replace("{userId}", userId);
    }

    private String formatForceOfflineVersionKey(String userId) {
        return FORCE_OFFLINE_VERSION_KEY.replace("{userId}", userId);
    }

    private String formatEndOnlineMaxVersionKey(String userId, String end) {
        return END_ONLINE_MAX_VERSION_KEY.replace("{userId}", userId).replace("{end}", end);
    }

}
