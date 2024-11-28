package com.weplaylabs.mysterybox.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.VO.RewardItemVO;
import com.weplaylabs.mysterybox.model.resource.ItemGachaPoolResource;
import com.weplaylabs.mysterybox.util.GeoIP2;
import com.weplaylabs.mysterybox.util.Location;

import io.netty.util.internal.ThreadLocalRandom;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class ExFun
{
    @Autowired
    private GameResource gameResource;

    public int getRandInt(int startVal, int endVal)
	{
		int minVal = Math.min(startVal, endVal);
		int maxVal = Math.max(startVal, endVal);
		
		int gapVal = maxVal - minVal;
		if (gapVal < ConstantVal.IS_TRUE)
			return minVal;
		
		return ThreadLocalRandom.current().nextInt(gapVal + 1) + minVal;
	}

    public String getClientIP(HttpServletRequest servlet)
    {
        String ip = servlet.getHeader("X-FORWARDED-FOR");
        if (ip == null || ip.length() == 0) 
            ip = servlet.getHeader("Proxy-Client-IP");

        //웹로직 서버일 경우
        if (ip == null || ip.length() == 0) 
            ip = servlet.getHeader("WL-Proxy-Client-IP");
        
        if (ip == null || ip.length() == 0) 
            ip = servlet.getRemoteAddr() ;

        return ip;
    }

    public int getStringLength(String str)
    {
        char ch[] = str.toCharArray();
        int max = ch.length;
        int count = 0;

        for(int i = 0; i < max; i++) {
            // 0x80: 문자일 경우 +2
            if (ch[i] >= 0x80) {
                count++;
            }
            count++;
        }
        return count;
    }

    public String getClientRegion(String clientIp)
    {
        Location clientLocation = null;
		String clientCountryCode = "";
		
		try
		{
			clientLocation = GeoIP2.getInstance().lookup(clientIp);
			if(clientLocation != null)
				clientCountryCode = clientLocation.getCountryCode();
		} 
		catch (Exception e) 
		{
			clientCountryCode = "ZZ";
		}
		
		return clientCountryCode;
    }

    public boolean checkInputOnlyNumberAndAlphabet(String textInput)
	{
		if(textInput.length() <= 0)
			return false;
		
		char chrInput;

		for (int i = 0; i < textInput.length(); i++) {
			chrInput = textInput.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크
			if (chrInput >= 0x61 && chrInput <= 0x7A) {
			    // 영문(소문자) OK!
				continue;
			} 
			else if (chrInput >=0x41 && chrInput <= 0x5A) {
			    // 영문(대문자) OK!
				continue;
			}
			else if (chrInput >= 0x30 && chrInput <= 0x39) {
			    // 숫자 OK!
				continue;
			}
            // else if(chrInput >= '\uAC00' && chrInput <= '\uD7A3') {
            //     // 한글 OK!
            //     continue;
            // }
			else {
			    return false;   // 영문자도 아니고 한글도 아니고 숫자도 아님!
			}
		}
		return true;
	}

    public RewardItemVO getGachaItem(int itemGroup) throws Exception
    {
        int gachapool = gameResource.getItemGachaManager().getGachaPoolId(1, itemGroup);
        if(gachapool <= 0)
            throw new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
            
        ItemGachaPoolResource itemGachaPoolRS = gameResource.getItemGachaPool().getRewardItem(gachapool);
        if(itemGachaPoolRS == null)
            throw  new WeException(ConstantVal.ERROR_CDOE_FILTER_1004);
        
        RewardItemVO vo = new RewardItemVO(itemGachaPoolRS.getIdItem(), getRandInt(itemGachaPoolRS.getMin(), itemGachaPoolRS.getMax()));
        return vo;
    }
}
