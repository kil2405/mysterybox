package com.weplaylabs.mysterybox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weplaylabs.mysterybox.common.GameResource;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/service/init/")
public class InitController
{
    private final int ColCount = 15;
	private final int RowCount = 5;

    @Value("${spring.profiles.active}")
	private String serverMode;

    @Value("${server.encryption}")
    private boolean useEncryption;

    @Autowired
    private GameResource gameResource;
    
    @PostConstruct
	public void initController() throws Exception
    {
        initServerMode(serverMode);
        initResource();
    }

    private void initServerMode(String serverMode)
    {
        StringBuilder strBuilder = new StringBuilder();

		System.out.println("====================================== SERVER MODE =======================================");

		for (int index = 0; index < ColCount; ++index) {
			strBuilder.append(serverMode);
			strBuilder.append(" ");
		}

		for (int row = 0; row < RowCount; ++row)
			System.out.println(strBuilder.toString());

        System.out.println("Server Encryption Mode : " + useEncryption);

		System.out.println("==========================================================================================");
		System.out.println("");
    }

    private void initResource() throws Exception
    {
        System.out.println("================================= SERVER LOAD RESOURCE ==================================");
		gameResource.LoadResource();
        System.out.println("==========================================================================================");
    }
}
