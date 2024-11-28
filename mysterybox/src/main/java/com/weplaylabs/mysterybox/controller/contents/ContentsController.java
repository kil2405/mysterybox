package com.weplaylabs.mysterybox.controller.contents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.weplaylabs.mysterybox.common.BaseSessionClass;
import com.weplaylabs.mysterybox.request.ReqClick;
import com.weplaylabs.mysterybox.request.ReqItemEquip;

@RestController
@RequestMapping("/api")
public class ContentsController
{
    @Autowired
    private BaseSessionClass baseSessionClass;

    @Autowired
    private ContentsService contentsService;

    @RequestMapping(value = "/client-secure/click-count", method = RequestMethod.POST)
    public Object Click(@RequestBody ReqClick req) throws Exception {
        return contentsService.SendClickCount(baseSessionClass.getUserId(), req);
    }

    @RequestMapping(value = "/client-secure/refresh", method = RequestMethod.POST)
    public Object Refresh() throws Exception {
        return contentsService.Refresh(baseSessionClass.getUserId());
    }

    @RequestMapping(value = "/client-secure/item-equip", method = RequestMethod.POST)
    public Object ItemEquip(@RequestBody ReqItemEquip req) throws Exception {
        return contentsService.ItemEquip(baseSessionClass.getUserId(), req);
    }
}
