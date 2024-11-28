package com.weplaylabs.mysterybox.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.model.resource.DailyItemAppearResource;
import com.weplaylabs.mysterybox.repository.resource.DailyItemAppearRepository;

@Component
public class DailyItemAppearComponent
{
    @Autowired
    private DailyItemAppearRepository dailyItemAppearRepository;

    private final HashMap<Integer, DailyItemAppearResource> resource = new HashMap<>();

    public void LoadResource() throws Exception
    {
        resource.clear();

        List<DailyItemAppearResource> dailyItemAppearResources = dailyItemAppearRepository.findAll();
        if(dailyItemAppearResources == null || dailyItemAppearResources.isEmpty())
        {
            System.out.println("DailyItemAppearResource is empty");
            System.exit(0);
        }

        for(DailyItemAppearResource dailyItemAppearResource : dailyItemAppearResources)
        {
            if(resource.containsKey(dailyItemAppearResource.getAppearNum()))
            {
                System.out.println("DailyItemAppearResource is duplicated | " + dailyItemAppearResource.getAppearNum());
                System.exit(0);
            }

            resource.put(dailyItemAppearResource.getAppearNum(), dailyItemAppearResource);
        }

        System.out.println("DailyItemAppearResource Loading Complete");
    }

    public DailyItemAppearResource get(int appearNum)
    {
        return resource.get(appearNum);
    }

    public List<DailyItemAppearResource> getAll()
    {
        List<DailyItemAppearResource> data = new ArrayList<>();
        for(DailyItemAppearResource dailyItemAppearResource : resource.values())
        {
            data.add(dailyItemAppearResource);
        }
        return data;
    }
}