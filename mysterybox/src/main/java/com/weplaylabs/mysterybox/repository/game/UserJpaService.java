package com.weplaylabs.mysterybox.repository.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.weplaylabs.mysterybox.model.game.User;
import com.weplaylabs.mysterybox.redisService.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserJpaService
{
    private final UserJpaRepository userJpaRepository;

    @Autowired
    private RedisService redisService;

    // Create, Update
    @CachePut(value = "user", key = "#userEntity.userId")
    public User save(User userEntity)
    {
        return userJpaRepository.save(userEntity);
    }
    
    @Cacheable(value = "user", key = "#userId")
    public User findByUserId(int userId)
    {
        System.out.println("캐싱 데이터가 없어 DB에서 조회합니다.");
        return userJpaRepository.findByUserId(userId);
    }

    public User findByUserUid(String userUid)
    {
        User user = userJpaRepository.findByUserUid(userUid);
        if(user == null)
            return null;

        // Redis에 저장되어있지 않다면 저장(캐싱)
        User redisUser = redisService.get("user::" + user.getUserId(), User.class, null);
        if(redisUser == null)
            user = findByUserId(user.getUserId());

        return user;
    }

    public String findByUserNickname(String userNickname)
    {
        User user = userJpaRepository.findByUserNickname(userNickname);
        if(user != null)
            return user.getUserNickname();
        
        return null;
    }
    
    public long getNowUnixTime()
    {
        return userJpaRepository.getNowUnixTime();
    }
}
