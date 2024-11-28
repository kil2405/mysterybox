package com.weplaylabs.mysterybox.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.model.resource.ForbiddenWordsResource;
import com.weplaylabs.mysterybox.repository.resource.ForbiddenWordsRepository;

@Component
public class ForbiddenWordsComponent
{
    @Autowired
    private ForbiddenWordsRepository forbiddenWordsRepository;

    private final HashMap<Integer, ForbiddenWordsResource> resource = new HashMap<>();
    List<String> forbiddenWordsList = new ArrayList<>();

    public void LoadResource() throws Exception
    {
        resource.clear();

        List<ForbiddenWordsResource> forbiddenWordsResources = forbiddenWordsRepository.findAll();
        if(forbiddenWordsResources == null || forbiddenWordsResources.isEmpty())
        {
            System.out.println("ForbiddenWordsResource is empty");
            System.exit(0);
        }

        for(ForbiddenWordsResource forbiddenWordsResource : forbiddenWordsResources)
        {
            if(resource.containsKey(forbiddenWordsResource.getIndex()))
            {
                System.out.println("ForbiddenWordsResource is duplicated | " + forbiddenWordsResource.getIndex());
                System.exit(0);
            }

            resource.put(forbiddenWordsResource.getIndex(), forbiddenWordsResource);
            forbiddenWordsList.add(forbiddenWordsResource.getText());
        }

        System.out.println("ForbiddenWordsResource Loading Complete");
    }

    public ForbiddenWordsResource get(int index)
    {
        return resource.get(index);
    }

    public List<String> getAllWords()
    {
        return forbiddenWordsList;
    }
}
