package com.weplaylabs.mysterybox.response;

import java.util.List;

import com.weplaylabs.mysterybox.VO.RankerVO;

public class ResRankingUsers extends BaseResponse {
    public List<RankerVO> country;
    public List<RankerVO> personal;
    public RankerVO userRecord;
}
