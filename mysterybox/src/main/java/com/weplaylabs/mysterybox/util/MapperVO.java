package com.weplaylabs.mysterybox.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.VO.AccountVO;
import com.weplaylabs.mysterybox.VO.ItemVO;
import com.weplaylabs.mysterybox.VO.RankerVO;
import com.weplaylabs.mysterybox.model.game.User;
import com.weplaylabs.mysterybox.model.game.UserItem;

@Component
public class MapperVO
{
    public AccountVO makeAccountVO(User user) throws Exception
    {
        AccountVO accountVO = new AccountVO();
        accountVO.userId = user.getUserId();
        accountVO.userUid = user.getUserUid();
        accountVO.userNickname = user.getUserNickname();
        accountVO.encryption = user.getEncryption();
        accountVO.wallet = user.getWallet();
        accountVO.region = user.getRegion();
        accountVO.isTermsAgree = user.isTermsAgree();
        accountVO.clickCount = user.getClickCount();
        accountVO.gold = user.getGold();

        return accountVO;
    }

    public List<ItemVO> makeItemVO(List<UserItem> items) throws Exception
    {
        List<ItemVO> itemVOs = new ArrayList<>();
        for(UserItem item : items)
        {
            ItemVO itemVO = new ItemVO();
            itemVO.itemId = item.getItemId();
            itemVO.count = item.getCount();
            itemVO.isEquipped = item.isEquip();
            itemVOs.add(itemVO);
        }
        return itemVOs;
    }

    public RankerVO makeRankerVO(int userId, String nickname, long count, int ranking) throws Exception
    {
        RankerVO rankerVO = new RankerVO();
        rankerVO.id = userId; // userId
        rankerVO.name = nickname;   // nickname
        rankerVO.count = count;     // clickCount
        rankerVO.ranking = ranking; // ranking

        return rankerVO;
    }
}
