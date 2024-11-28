package com.weplaylabs.mysterybox.response;

import java.util.List;

import com.weplaylabs.mysterybox.VO.ItemVO;
import com.weplaylabs.mysterybox.VO.RewardItemVO;

public class ResClick extends BaseResponse {
    public long totalCount;
    public List<RewardItemVO> rewardItems;
    public List<ItemVO> items;
    public long nextRewardRemainCount;
    public String effectPrefab;
}
