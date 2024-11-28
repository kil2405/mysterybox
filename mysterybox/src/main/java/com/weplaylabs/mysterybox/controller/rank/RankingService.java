package com.weplaylabs.mysterybox.controller.rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import com.weplaylabs.mysterybox.VO.RankerVO;
import com.weplaylabs.mysterybox.common.ConstantVal;
import com.weplaylabs.mysterybox.common.GameResource;
import com.weplaylabs.mysterybox.common.TimeCalculation;
import com.weplaylabs.mysterybox.common.WeException;
import com.weplaylabs.mysterybox.model.game.GlobalRanking;
import com.weplaylabs.mysterybox.model.game.User;
import com.weplaylabs.mysterybox.model.resource.CountryResource;
import com.weplaylabs.mysterybox.redisService.RedisService;
import com.weplaylabs.mysterybox.repository.game.GlobalRankingRepository;
import com.weplaylabs.mysterybox.repository.game.UserJpaService;
import com.weplaylabs.mysterybox.response.ResRankingUsers;
import com.weplaylabs.mysterybox.util.MapperVO;

@Service
public class RankingService
{
    //RedisKey
    private final String PRIVATE_RANKING = "private_rank_score_";
    private final String GLOBAL_RANKING = "global_rank_score";

    @Autowired
    private GlobalRankingRepository globalRankingRepository;

    @Autowired
    private UserJpaService userJpaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MapperVO mapperVO;

    @Autowired
    private GameResource gameResource;

    public ResRankingUsers getUserRankingList(int userId) throws Exception
    {
        ResRankingUsers res = new ResRankingUsers();

        User user = userJpaService.findByUserId(userId);
        if(user == null)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        
        String rankKey = PRIVATE_RANKING + user.getRegion();

        int ranking = 0;
        List<RankerVO> personalList = new ArrayList<>();
        List<RankerVO> conutryList = new ArrayList<>();
        Set<TypedTuple<String>> rankList = redisService.getSortedData(rankKey, 0, 100);
        for(TypedTuple<String> value : rankList)
        {
            String valueStr = value.getValue();
            if (valueStr == null)
                continue;
            
            String[] data = valueStr.split(",");
            Double scoreValue = value.getScore();
            long score = (scoreValue != null) ? scoreValue.longValue() : 0L;

            personalList.add(mapperVO.makeRankerVO(Integer.parseInt(data[0]), data[1], score, ++ranking));
        }

        ranking = 0;
        Set<TypedTuple<String>> globalRankList = redisService.getSortedData(GLOBAL_RANKING, 0, 300);
        for(TypedTuple<String> value : globalRankList)
        {
            String valueStr = value.getValue();
            if (valueStr == null)
                continue;
            
            CountryResource country = gameResource.getCountry().get(valueStr);
            if(country == null)
                continue;
            
            Double scoreValue = value.getScore();
            long score = (scoreValue != null) ? scoreValue.longValue() : 0L;

            conutryList.add(mapperVO.makeRankerVO(country.getCountryId(), "", score, ++ranking));
        }

        RankerVO userRecord = new RankerVO();
        userRecord.id = user.getUserId();
        userRecord.name = user.getUserNickname();
        userRecord.count = user.getClickCount();
        userRecord.ranking = redisService.getSortedRank(rankKey, user.getUserId() + "," + user.getUserNickname());

        res.result = ConstantVal.DEFAULT_SUCCESS;
        res.personal = personalList;
        res.country = conutryList;
        res.userRecord = userRecord;

        return res;
    }

    public void setUserRanking(User user, long count) throws Exception
    {
        int userId = user.getUserId();
        String rankKey = PRIVATE_RANKING + user.getRegion();

        if(user.getClickCount() < ConstantVal.DEFAULT_ZERO)
            return;

        //랭킹포인트 점수 처리 변경 (점수 + 시간값)
		double curTime = 1.0d - (TimeCalculation.getCurrentUnixTime() * 0.0000000001);
		double click = user.getClickCount() + curTime;
        
        // userId, 닉네임 포함하여 개인 통합 랭킹에 저장
        String value = userId + "," + user.getUserNickname();
        redisService.setSortedData(rankKey, value, click);

        // 나라별 랭킹에 저장 (Lock을 사용하여, 속도가 느려질 수 있음 추후 테스트가 필요)
        setGlobalRanking(user.getRegion(), count);
    }

    // 동시성 제어를 위해 Lock을 걸어준다.
    private void setGlobalRanking(String region, long count) throws Exception
    {
        String lockKey = "global_lock";
        long expireMinutes = (60 * 4);
        
        while(true)
        {
            // 분산Lock을 시도
            Boolean success = redisService.ifAbsent(lockKey, "LOCK", expireMinutes);
            if(success != null && success)
            {
                // Lock을 획득한 경우
                try
                {
                    // 기존 랭킹값 얻어오기
                    GlobalRanking globalRanking = globalRankingRepository.findById(region).orElse(null);
                    if(globalRanking == null)
                    {
                        globalRanking = GlobalRanking.builder().region(region).count(0).build();
                    }

                    long updateCount = globalRanking.getCount() + count;

                    // 랭킹값 업데이트
                    globalRanking.setCount(updateCount);
                    globalRankingRepository.save(globalRanking);

                    double curTime = 1.0d - (TimeCalculation.getCurrentUnixTime() * 0.0000000001);
                    double click = updateCount + curTime;

                    redisService.setSortedData(GLOBAL_RANKING, region, click);
                    break;
                }
                catch(Exception e) {
                    redisService.delete(lockKey);
                } finally {
                    // Lock을 해제
                    redisService.delete(lockKey);
                }
            }
            else
            {
                // Lock을 획득하지 못한 경우 잠시 대기
                Thread.sleep(100);
            }
        }
    }
}
