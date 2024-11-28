package com.weplaylabs.mysterybox.redisService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.weplaylabs.mysterybox.common.ConstantVal;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisService
{
    private final RedisTemplate<String, String> redisTemplate;

    public void set(String key, String json)
    {
        redisTemplate.opsForValue().set(key, json);
    }

    // 만료시간 설정가능(분)
    public void set(String key, String json, long expireTime)
    {
        redisTemplate.opsForValue().set(key, json, expireTime, TimeUnit.MINUTES);
    }

    // List 형태의 데이터를 redis에 저장
    public <T> void set(String key, List<T> list, Class<T[]> clazz)
    {
        Gson gson = new Gson();
        String jsonList = null;
        String json = gson.toJson(list, list.getClass());

        if (json.charAt(0) != '[')
			jsonList = "[" + json + "]";
		else
			jsonList = json;
        
        redisTemplate.opsForValue().set(key, jsonList);
    }

    public void setSortedData(String key, String value, double score)
    {
        redisTemplate.expire(key, (ConstantVal.MINUTE_OF_HOUR * ConstantVal.HOUR_OF_DAY * 90), TimeUnit.MINUTES);
        redisTemplate.opsForZSet().add(key, value, score);
    }

    public String get(String key)
    {
        return redisTemplate.opsForValue().get(key);
    }

    public <T> T get(String key, Class<T> clazz, Gson gson)
    {
        if(gson == null)
            gson = new Gson();

        String redisValue = redisTemplate.opsForValue().get(key);
        if (redisValue == null)
            return null;

        return gson.fromJson(redisValue, clazz);
    }

    public int getSortedData(String zKey, String zValue)
    {
        Double score = redisTemplate.opsForZSet().score(zKey, zValue);
        return score == null ? 0 : score.intValue();
    }

    public Set<TypedTuple<String>> getSortedData(String key, long start, long end)
    {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    public Boolean ifAbsent(String key, String value, long expireTime)
    {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.MINUTES);
    }

    public int getSortedRank(String zKey, String zVal)
    {
        Long rank = redisTemplate.opsForZSet().reverseRank(zKey, zVal);
        return rank == null ? ConstantVal.DEFAULT_VALUE : (int)(rank + 1);
    }

    public Boolean delete(String key)
    {
        return redisTemplate.delete(key);
    }
}
