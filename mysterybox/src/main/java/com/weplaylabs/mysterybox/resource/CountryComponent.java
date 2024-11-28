package com.weplaylabs.mysterybox.resource;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.model.resource.CountryResource;
import com.weplaylabs.mysterybox.repository.resource.CountryRepository;

@Component
public class CountryComponent
{
    @Autowired
    private CountryRepository countryRepository;

    private final HashMap<String, CountryResource> resource = new HashMap<>();

    public void LoadResource() throws Exception
    {
        resource.clear();

        List<CountryResource> countries = countryRepository.findAll();
        if(countries == null || countries.isEmpty())
        {
            System.out.println("CountryResource is empty");
            System.exit(0);
        }

        for(CountryResource country : countries)
        {
            if(resource.containsKey(country.getCountryCode()))
            {
                System.out.println("CountryResource is duplicated | " + country.getCountryCode());
                System.exit(0);
            }

            resource.put(country.getCountryCode(), country);
        }

        System.out.println("CountryResource Loading Complete");
    }

    public CountryResource get(String countryCode)
    {
        return resource.get(countryCode);
    }
}
