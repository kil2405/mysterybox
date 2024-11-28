package com.weplaylabs.mysterybox.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private String ipAddress;
	private String continentCode;
	private String continentName;
	private String countryCode;
	private String countryName;
	private String subdivisionCode;
	private String subdivisionName;
	private String city;
	private String postalCode;
	private double latitude;
	private double longitude;
}
