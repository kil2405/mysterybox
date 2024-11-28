package com.weplaylabs.mysterybox.resource;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.model.resource.ItemListResource;
import com.weplaylabs.mysterybox.repository.resource.ItemListRepository;

@Component
public class ItemListComponent
{
    @Autowired
    private ItemListRepository itemListRepository;

    private final HashMap<Integer, ItemListResource> resource = new HashMap<>();

    public void LoadResource() throws Exception
    {
        resource.clear();

        List<ItemListResource> itemListResources = itemListRepository.findAll();
        if(itemListResources == null || itemListResources.isEmpty())
        {
            System.out.println("ItemListResource is empty");
            System.exit(0);
        }

        for(ItemListResource itemListResource : itemListResources)
        {
            if(resource.containsKey(itemListResource.getItemId()))
            {
                System.out.println("ItemListResource is duplicated | " + itemListResource.getItemId());
                System.exit(0);
            }

            resource.put(itemListResource.getItemId(), itemListResource);
        }

        System.out.println("ItemListResource Loading Complete");
    }

    public ItemListResource get(int itemId)
    {
        return resource.get(itemId);
    }
}
