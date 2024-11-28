package com.weplaylabs.mysterybox.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.weplaylabs.mysterybox.common.BaseSessionClass;
import com.weplaylabs.mysterybox.request.ReqCreateNickname;
import com.weplaylabs.mysterybox.request.ReqLogin;
import com.weplaylabs.mysterybox.request.ReqTermsAgree;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private BaseSessionClass baseSessionClass;

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = "/client/login", method = RequestMethod.POST)
    public Object Login(@RequestBody ReqLogin req) throws Exception {
        return userService.UserLogin(req);
    }

    @RequestMapping(value = "/client-secure/agree", method = RequestMethod.POST)
    public Object TermsAgree(@RequestBody ReqTermsAgree req) throws Exception {
        return userService.TermsAgree(baseSessionClass.getUserId(), req);
    }

    @RequestMapping(value = "/client-secure/create-nickname", method = RequestMethod.POST)
    public Object CreateNickname(@RequestBody ReqCreateNickname req) throws Exception {
        return userService.CreateNickname(baseSessionClass.getUserId(), req);
    }
}
