package com.weplaylabs.mysterybox.response;

import java.util.List;

import com.weplaylabs.mysterybox.VO.RankerVO;
import com.weplaylabs.mysterybox.VO.RewardGoldVO;

public class ResRefresh extends BaseResponse {
    public List<RankerVO> country;
    public List<RankerVO> personal;
    public RankerVO userRecord;
    public RewardGoldVO rewardGold;
}
