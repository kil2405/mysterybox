package com.weplaylabs.mysterybox.resource;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.model.resource.ItemRewardResource;
import com.weplaylabs.mysterybox.repository.resource.ItemRewardRepository;

@Component
public class ItemRewardComponent
{
    @Autowired
    private ItemRewardRepository itemRewardRepository;

    private final HashMap<Integer, ItemRewardResource> resource = new HashMap<>();

    public void LoadResource() throws Exception
    {
        resource.clear();

        List<ItemRewardResource> itemRewardResources = itemRewardRepository.findAll();
        if(itemRewardResources == null || itemRewardResources.isEmpty())
        {
            System.out.println("ItemRewardResource is empty");
            System.exit(0);
        }

        for(ItemRewardResource itemRewardResource : itemRewardResources)
        {
            if(resource.containsKey(itemRewardResource.getCount()))
            {
                System.out.println("ItemRewardResource is duplicated | " + itemRewardResource.getCount());
                System.exit(0);
            }

            resource.put(itemRewardResource.getCount(), itemRewardResource);
        }

        System.out.println("ItemRewardResource Loading Complete");
    }

    public ItemRewardResource get(int index)
    {
        return resource.get(index);
    }
}
