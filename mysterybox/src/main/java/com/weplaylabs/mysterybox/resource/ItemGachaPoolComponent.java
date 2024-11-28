package com.weplaylabs.mysterybox.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.common.ConstantVal;
import com.weplaylabs.mysterybox.model.resource.ItemGachaPoolResource;
import com.weplaylabs.mysterybox.repository.resource.ItemGachaPoolRepository;

import io.netty.util.internal.ThreadLocalRandom;

@Component
public class ItemGachaPoolComponent
{
    @Autowired
    private ItemGachaPoolRepository itemGachaPoolRepository;
    
    private final HashMap<Integer, List<ItemGachaPoolResource>> resource = new HashMap<>();
    private final HashMap<Integer, Integer> accrueRateResource = new HashMap<>();

    public void LoadResource() throws Exception
    {
        resource.clear();

        List<ItemGachaPoolResource> itemGachaPoolResources = itemGachaPoolRepository.findAll();
        if(itemGachaPoolResources == null || itemGachaPoolResources.isEmpty())
        {
            System.out.println("ItemGachaPoolResource is empty");
            System.exit(0);
        }

        for(ItemGachaPoolResource itemGachaPoolResource : itemGachaPoolResources)
        {
            if(resource.containsKey(itemGachaPoolResource.getGachapool()))
            {
                List<ItemGachaPoolResource> itemGachaPoolResourceList = resource.get(itemGachaPoolResource.getGachapool());
                itemGachaPoolResourceList.add(itemGachaPoolResource);
                resource.put(itemGachaPoolResource.getGachapool(), itemGachaPoolResourceList);
                accrueRateResource.put(itemGachaPoolResource.getGachapool(), accrueRateResource.get(itemGachaPoolResource.getGachapool()) + itemGachaPoolResource.getProb());
            }
            else
            {
                List<ItemGachaPoolResource> itemGachaPoolResourceList = new ArrayList<>();
                itemGachaPoolResourceList.add(itemGachaPoolResource);
                resource.put(itemGachaPoolResource.getGachapool(), itemGachaPoolResourceList);
                accrueRateResource.put(itemGachaPoolResource.getGachapool(), itemGachaPoolResource.getProb());
            }
        }

        System.out.println("ItemGachaPoolResource Loading Complete");
    }

    public ItemGachaPoolResource getRewardItem(int gachapool)
    {
        List<ItemGachaPoolResource> itemGachaPoolResourceList = resource.get(gachapool);
        if(itemGachaPoolResourceList == null || itemGachaPoolResourceList.isEmpty())
            return null;
        
        int accrueRate = accrueRateResource.get(gachapool);
        if(accrueRate <= ConstantVal.DEFAULT_ZERO)
            return null;
        
        int randomRate = ThreadLocalRandom.current().nextInt(accrueRate) + 1;

        int accrue = 0;
        for(ItemGachaPoolResource rs : itemGachaPoolResourceList)
        {
            accrue += rs.getProb();
            if(randomRate > accrue)
                continue;
            
            return rs;
        }
        return null;
    }
}
