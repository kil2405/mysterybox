package com.weplaylabs.mysterybox.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weplaylabs.mysterybox.resource.CountryComponent;
import com.weplaylabs.mysterybox.resource.DailyItemAppearComponent;
import com.weplaylabs.mysterybox.resource.ErrorCodeComponent;
import com.weplaylabs.mysterybox.resource.ForbiddenWordsComponent;
import com.weplaylabs.mysterybox.resource.ItemGachaManagerComponent;
import com.weplaylabs.mysterybox.resource.ItemGachaPoolComponent;
import com.weplaylabs.mysterybox.resource.ItemListComponent;
import com.weplaylabs.mysterybox.resource.ItemRewardComponent;

@Component
public class GameResource
{
    @Autowired
    private CountryComponent countryComponent;

    @Autowired
    private DailyItemAppearComponent dailyItemAppearComponent;

    @Autowired
    private ErrorCodeComponent errorCodeComponent;

    @Autowired
    private ForbiddenWordsComponent forbiddenWordsComponent;

    @Autowired
    private ItemGachaManagerComponent itemGachaManagerComponent;

    @Autowired
    private ItemGachaPoolComponent itemGachaPoolComponent;

    @Autowired
    private ItemListComponent itemListComponent;

    @Autowired
    private ItemRewardComponent itemRewardComponent;

    public void LoadResource() throws Exception
    {
        countryComponent.LoadResource();
        dailyItemAppearComponent.LoadResource();
        errorCodeComponent.LoadResource();
        forbiddenWordsComponent.LoadResource();
        itemGachaManagerComponent.LoadResource();
        itemGachaPoolComponent.LoadResource();
        itemListComponent.LoadResource();
        itemRewardComponent.LoadResource();
    }

    public CountryComponent getCountry()
    {
        return countryComponent;
    }

    public DailyItemAppearComponent getDailyItemAppear()
    {
        return dailyItemAppearComponent;
    }

    public ErrorCodeComponent getErrorCode()
    {
        return errorCodeComponent;
    }

    public ForbiddenWordsComponent getForbiddenWords()
    {
        return forbiddenWordsComponent;
    }

    public ItemGachaManagerComponent getItemGachaManager()
    {
        return itemGachaManagerComponent;
    }

    public ItemGachaPoolComponent getItemGachaPool()
    {
        return itemGachaPoolComponent;
    }

    public ItemListComponent getItemList()
    {
        return itemListComponent;
    }

    public ItemRewardComponent getItemReward()
    {
        return itemRewardComponent;
    }
}
