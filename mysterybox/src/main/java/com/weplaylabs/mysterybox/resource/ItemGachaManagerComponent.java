package com.weplaylabs.mysterybox.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.common.ConstantVal;
import com.weplaylabs.mysterybox.model.resource.ItemGachaManagerResource;
import com.weplaylabs.mysterybox.repository.resource.ItemGachaManagerRepository;

import io.netty.util.internal.ThreadLocalRandom;

@Component
public class ItemGachaManagerComponent
{
    @Autowired
    private ItemGachaManagerRepository itemGachaManagerRepository;

    private final HashMap<Integer, List<ItemGachaManagerResource>> resource = new HashMap<>();
    private final HashMap<Integer, Integer> accrueRateResource = new HashMap<>();

    private int createKey(int season, int itemGroup)
    {
        return (season * 1000) + itemGroup;
    }

    public void LoadResource() throws Exception
    {
        resource.clear();

        List<ItemGachaManagerResource> itemGachaManagerResources = itemGachaManagerRepository.findAll();
        if(itemGachaManagerResources == null || itemGachaManagerResources.isEmpty())
        {
            System.out.println("ItemGachaManagerResource is empty");
            System.exit(0);
        }

        for(ItemGachaManagerResource itemGachaManagerResource : itemGachaManagerResources)
        {
            int key = createKey(itemGachaManagerResource.getIdSeason(), itemGachaManagerResource.getItemGroup());

            if(resource.containsKey(key))
            {
                List<ItemGachaManagerResource> value = resource.get(key);
                value.add(itemGachaManagerResource);
                accrueRateResource.put(key, accrueRateResource.get(key) + itemGachaManagerResource.getProb());
            }
            else
            {
                List<ItemGachaManagerResource> value = new ArrayList<>();
                value.add(itemGachaManagerResource);
                resource.put(key, value);
                accrueRateResource.put(key, itemGachaManagerResource.getProb());
            }
        }

        System.out.println("ItemGachaManagerResource Loading Complete");
    }

    public List<ItemGachaManagerResource> get(int season, int itemGroup)
    {
        return resource.get(createKey(season, itemGroup));
    }

    public int getAccrueRate(int season, int itemGroup)
    {
        return accrueRateResource.get(createKey(season, itemGroup));
    }

    public int getGachaPoolId(int season, int itemGroup)
    {
        int gachaPoolId = ConstantVal.DEFAULT_VALUE;

        List<ItemGachaManagerResource> itemGachaManagerResources = get(season, itemGroup);
        if(itemGachaManagerResources == null || itemGachaManagerResources.isEmpty())
            return ConstantVal.DEFAULT_VALUE;
        
        int accrueRate = getAccrueRate(season, itemGroup);
        if(accrueRate <= ConstantVal.DEFAULT_ZERO)
            return ConstantVal.DEFAULT_VALUE;
        
        int randomRate = ThreadLocalRandom.current().nextInt(accrueRate) + 1;

        int accrue = 0;
        for(ItemGachaManagerResource rs : itemGachaManagerResources)
        {
            accrue += rs.getProb();
            if(randomRate > accrue)
                continue;
            
            gachaPoolId = rs.getIdGachaPool();
            break;
        }
        return gachaPoolId;
    }
}
