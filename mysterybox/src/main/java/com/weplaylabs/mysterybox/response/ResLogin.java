package com.weplaylabs.mysterybox.response;

import java.util.List;

import com.weplaylabs.mysterybox.VO.AccountVO;
import com.weplaylabs.mysterybox.VO.ItemVO;
import com.weplaylabs.mysterybox.VO.RankerVO;
import com.weplaylabs.mysterybox.VO.RewardGoldVO;

public class ResLogin extends BaseResponse {
    public AccountVO account;
    public List<ItemVO> items;
    public List<RankerVO> country;
    public List<RankerVO> personal;
    public RankerVO userRecord;
    public RewardGoldVO rewardGold;
    public String termsUrl;
}
