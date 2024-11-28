package com.weplaylabs.mysterybox.controller.rank;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RankingController
{
    @RequestMapping(value = "/client/ranking/list", method = RequestMethod.POST)
    public Object Click() throws Exception {
        return null;
    }
}
