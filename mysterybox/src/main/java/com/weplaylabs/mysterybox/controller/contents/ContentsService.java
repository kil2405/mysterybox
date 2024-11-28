package com.weplaylabs.mysterybox.controller.contents;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weplaylabs.mysterybox.VO.RewardGoldVO;
import com.weplaylabs.mysterybox.VO.RewardItemVO;
import com.weplaylabs.mysterybox.common.ConstantVal;
import com.weplaylabs.mysterybox.common.ExFun;
import com.weplaylabs.mysterybox.common.GameResource;
import com.weplaylabs.mysterybox.common.TimeCalculation;
import com.weplaylabs.mysterybox.common.WeException;
import com.weplaylabs.mysterybox.controller.rank.RankingService;
import com.weplaylabs.mysterybox.model.game.ClickCountLog;
import com.weplaylabs.mysterybox.model.game.GoldAcqLog;
import com.weplaylabs.mysterybox.model.game.ItemAcqLog;
import com.weplaylabs.mysterybox.model.game.ItemReward;
import com.weplaylabs.mysterybox.model.game.RewardGold;
import com.weplaylabs.mysterybox.model.game.User;
import com.weplaylabs.mysterybox.model.game.UserItem;
import com.weplaylabs.mysterybox.model.resource.DailyItemAppearResource;
import com.weplaylabs.mysterybox.model.resource.ItemRewardResource;
import com.weplaylabs.mysterybox.repository.game.ClickCountLogRepository;
import com.weplaylabs.mysterybox.repository.game.GoldAcqLogRepository;
import com.weplaylabs.mysterybox.repository.game.ItemAcqLogRepository;
import com.weplaylabs.mysterybox.repository.game.ItemRewardJpaService;
import com.weplaylabs.mysterybox.repository.game.RewardGoldRepository;
import com.weplaylabs.mysterybox.repository.game.UserItemJpaService;
import com.weplaylabs.mysterybox.repository.game.UserJpaService;
import com.weplaylabs.mysterybox.request.ReqClick;
import com.weplaylabs.mysterybox.request.ReqItemEquip;
import com.weplaylabs.mysterybox.response.ResClick;
import com.weplaylabs.mysterybox.response.ResItemEquip;
import com.weplaylabs.mysterybox.response.ResRankingUsers;
import com.weplaylabs.mysterybox.response.ResRefresh;
import com.weplaylabs.mysterybox.util.MapperVO;

@Service
public class ContentsService
{
    @Autowired
    private UserJpaService userJpaService;

    @Autowired
    private ItemRewardJpaService itemRewardJpaService;

    @Autowired
    private UserItemJpaService userItemJpaService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private RewardGoldRepository rewardGoldRepository;

    @Autowired
    private ExFun exFun;

    @Autowired
    private MapperVO mapperVO;

    @Autowired
    private ItemAcqLogRepository itemAcqLogRepository;

    @Autowired
    private GoldAcqLogRepository goldAcqLogRepository;

    @Autowired
    private ClickCountLogRepository clickCountLogRepository;

    @Autowired
    private GameResource gameResource;

    public ResClick SendClickCount(int userId, ReqClick req) throws Exception
    {
        ResClick res = new ResClick();
        boolean cheat = false;

        User user = userJpaService.findByUserId(userId);
        if(user == null)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        if(req.count <= ConstantVal.DEFAULT_ZERO)
        {
            res.result = ConstantVal.DEFAULT_SUCCESS;
            res.totalCount = user.getClickCount();
            res.items = null;
            return res;
        }

        if(req.count > ConstantVal.CLICK_MAX_COUNT)
            cheat = true;

        long beforeCount = user.getClickCount();
        long afterCount = beforeCount + (cheat == false ? req.count : 0);

        List<ItemReward> itemRewards = itemRewardJpaService.findAllByUserId(userId);
        if(itemRewards == null || itemRewards.isEmpty())
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        
        int appearNum = ConstantVal.DEFAULT_ZERO;
        int nextRewardIndex = ConstantVal.DEFAULT_ONE;
        for(ItemReward itemReward : itemRewards)
        {
            if(itemReward.isReward())
            {
                nextRewardIndex++;
                continue;
            }
            
            if(itemReward.getCount() > afterCount)
                continue;
            
            appearNum = itemReward.getAppearNum();
            nextRewardIndex = appearNum + 1;
            break;
        }

        if(cheat == false)
        {
            if(appearNum > 0)
            {
                DailyItemAppearResource dailyItemAppearRS = gameResource.getDailyItemAppear().get(appearNum);
                if(dailyItemAppearRS == null)
                    throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
                
                RewardItemVO vo = exFun.getGachaItem(dailyItemAppearRS.getItemGroup());
                if(vo == null)
                    throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

                // 실제 아이템 지급 (아이템 테이블에 추가 해야함)
                int itemIndex = ConstantVal.DEFAULT_VALUE;
                List<UserItem> items = userItemJpaService.findAllByUserId(userId);
                for(UserItem item : items)
                {
                    if(item.getItemId() == vo.itemId)
                        itemIndex = items.indexOf(item);
                }

                if(itemIndex < 0)
                {
                    UserItem item = UserItem.builder()
                    .userId(userId)
                    .itemId(vo.itemId)
                    .count(vo.count)
                    .isEquip(false)
                    .build();
                    items.add(item);
                    itemIndex = items.size() - 1;
                }
                else
                {
                    items.get(itemIndex).setCount(items.get(itemIndex).getCount() + vo.count);
                }

                res.rewardItems = new ArrayList<>();
                res.rewardItems.add(vo);
                userItemJpaService.save(items.get(itemIndex));

                res.items = mapperVO.makeItemVO(userItemJpaService.findAllByUserId(userId));

                itemRewards.get(appearNum - 1).setReward(true);
                itemRewardJpaService.save(itemRewards.get(appearNum - 1));

                // 아이템 획득 로그 추가
                ItemAcqLog itemAcqLog = ItemAcqLog.builder()
                    .userId(userId)
                    .logDate(TimeCalculation.getCurrentUnixTime())
                    .itemId(vo.itemId)
                    .addCount(vo.count)
                    .build();
                itemAcqLogRepository.save(itemAcqLog);
            }

            // 유저 클릭수 없데이트
            user.setClickCount(afterCount);
            userJpaService.save(user);

            if(beforeCount <= ConstantVal.DEFAULT_ZERO)
            {
                UserItem item = UserItem.builder()
                .userId(userId)
                .itemId(ConstantVal.TUTORIAL_REWARD_ITEM)
                .count(1)
                .isEquip(true)
                .build();
                userItemJpaService.save(item);

                res.rewardItems = new ArrayList<>();
                res.rewardItems.add(new RewardItemVO(ConstantVal.TUTORIAL_REWARD_ITEM, 1));
                res.items = mapperVO.makeItemVO(userItemJpaService.findAllByUserId(userId));

                // 아이템 획득 로그 추가
                ItemAcqLog itemAcqLog = ItemAcqLog.builder()
                    .userId(userId)
                    .logDate(TimeCalculation.getCurrentUnixTime())
                    .itemId(ConstantVal.TUTORIAL_REWARD_ITEM)
                    .addCount(1)
                    .build();
                itemAcqLogRepository.save(itemAcqLog);
            }

            // 랭킹 처리
            rankingService.setUserRanking(user, req.count);
        }
        else
        {
            // 클릭 카운트 로그 처리 (비정상적인 클릭 수가 들어왔을때만 로그 찍도록 수정)
            // 비정상 카운트 수 (1분에 650회 클릭 이상)
            ClickCountLog clickCountLog = ClickCountLog.builder()
                .userId(userId)
                .logDate(TimeCalculation.getCurrentUnixTime())
                .clickCount(req.count)
                .totalCount(user.getClickCount())
                .build();
            
            clickCountLogRepository.save(clickCountLog);
        }

        if(nextRewardIndex > itemRewards.size())
        {
            res.nextRewardRemainCount = ConstantVal.DEFAULT_VALUE;
            res.effectPrefab = null;
        }
        else
        {
            res.nextRewardRemainCount = itemRewards.get(nextRewardIndex - 1).getCount() - afterCount;
            DailyItemAppearResource data = gameResource.getDailyItemAppear().get(nextRewardIndex);
            String effect = data.getEffectPrefab();
            res.effectPrefab = effect;
        }

        res.result = ConstantVal.DEFAULT_SUCCESS;
        res.totalCount = user.getClickCount();
        
        
        return res;
    }

    public ResRefresh Refresh(int userId) throws Exception
    {
        ResRefresh res = new ResRefresh();

        User user = userJpaService.findByUserId(userId);
        if(user == null)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        ResRankingUsers ranking = rankingService.getUserRankingList(userId);
        if(ranking == null)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        
        res.result = ConstantVal.DEFAULT_SUCCESS;
        res.country = new ArrayList<>(ranking.country);
        res.personal = new ArrayList<>(ranking.personal);
        res.userRecord = ranking.userRecord;
        res.rewardGold = getIdleaGold(user, true);
        
        return res;
    }

    public ResItemEquip ItemEquip(int userId, ReqItemEquip req) throws Exception
    {
        ResItemEquip res = new ResItemEquip();

        List<UserItem> items = userItemJpaService.findAllByUserId(userId);
        if(items == null || items.isEmpty())
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        
        // 변경하고자 하는 아이템의 index번호를 찾는다.
        int itemIndex = ConstantVal.DEFAULT_VALUE;
        for(UserItem item : items)
        {
            if(item.getItemId() == req.itemId)
                itemIndex = items.indexOf(item);
        }

        if(itemIndex < 0)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);

        // 기존에 장착했던 아이템을 장착해제
        for(UserItem item : items)
        {
            if(item.isEquip())
            {
                item.setEquip(false);
                userItemJpaService.save(item);
            }
        }

        // 아이템 장착
        items.get(itemIndex).setEquip(true);
        userItemJpaService.save(items.get(itemIndex));

        res.result = ConstantVal.DEFAULT_SUCCESS;
        res.items = mapperVO.makeItemVO(items);

        return res;
    }

    public RewardGoldVO getIdleaGold(User user, boolean dbUpdate) throws Exception
    {
        RewardGoldVO vo = new RewardGoldVO();

        int itemCount = userItemJpaService.countByUserId(user.getUserId());
        long currentTime = TimeCalculation.getCurrentUnixTime();

        RewardGold rewardGold = rewardGoldRepository.findByUserId(user.getUserId());
        if(rewardGold == null)
        {
            rewardGold = RewardGold.builder()
            .userId(user.getUserId())
            .gold(ConstantVal.DEFAULT_ZERO)
            .rewardTime(currentTime)
            .build();
            rewardGoldRepository.save(rewardGold);
        }

        ItemRewardResource itemRewardRS = gameResource.getItemReward().get(itemCount);
        if(itemRewardRS == null)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        
        long idleTime = currentTime - rewardGold.getRewardTime();
        int calculatedRewardGold = (int)(idleTime / ConstantVal.MINUTE_OF_SECOND) * itemRewardRS.getGoldReward();

        if(calculatedRewardGold > 0)
        {
            // 유저에게 직접 골드 지급
            user.setGold(user.getGold() + calculatedRewardGold);
            if(dbUpdate)
                userJpaService.save(user);

            // 리워드 테이블에 골드 지급 시간 업데이트
            rewardGold.setGold(calculatedRewardGold);
            rewardGold.setRewardTime(currentTime);
            rewardGoldRepository.save(rewardGold);

            // 골드 획득 로그 추가
            GoldAcqLog goldAcqLog = GoldAcqLog.builder()
                .userId(user.getUserId())
                .logDate(currentTime)
                .addValue(calculatedRewardGold)
                .totalValue(user.getGold())
                .build();
            goldAcqLogRepository.save(goldAcqLog);
        }

        vo.rewardGold = calculatedRewardGold;
        vo.totalGold = user.getGold();
        vo.goldPerMinute = itemRewardRS.getGoldReward();

        return vo;
    }
}
