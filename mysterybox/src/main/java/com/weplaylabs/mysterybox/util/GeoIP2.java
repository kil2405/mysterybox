package com.weplaylabs.mysterybox.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Value;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Subdivision;

public class GeoIP2
{
    private DatabaseReader reader;
    private final double MATCH_WAITING_PER_DISTANCE = 1;

    @Value("${spring.profiles.active}")
    private String serverMode;

    private GeoIP2()
    {
        File file = null;
		if(SystemUtils.OS_NAME.equals("Windows 10") || SystemUtils.OS_NAME.equals("Windows 11"))
			file = new File("D:/WorkSpace/MysteryBoxServer/GeoLite2-City.mmdb");
		else
            file = new File("/home/GeoLite2-City.mmdb");
			
		try { reader = new DatabaseReader.Builder(file).build(); }
		catch(IOException e) {
			e.printStackTrace();
		}
    }

    public static GeoIP2 getInstance()
	{
		return LazeHolder.INSTANCE;
	}
	
	private static class LazeHolder
	{
		private static final GeoIP2 INSTANCE = new GeoIP2();
	}

	public Location lookup(String host) throws IOException, GeoIp2Exception
	{
		InetAddress ipAddress = InetAddress.getByName(host);
		CityResponse response = null;
		synchronized (reader)
		{
			response = reader.city(ipAddress);
		}

		if (response == null)
			return null;

		Location result = new Location();
		result.setIpAddress(ipAddress.getHostAddress());
		result.setCountryCode(response.getCountry().getIsoCode());
		result.setCountryName(response.getCountry().getName());
		Subdivision subdivision = response.getMostSpecificSubdivision();
		result.setSubdivisionCode(subdivision.getIsoCode());
		result.setSubdivisionName(subdivision.getName());
		result.setCity(response.getCity().getName());
		result.setPostalCode(response.getPostal().getCode());
		result.setLatitude(response.getLocation().getLatitude());
		result.setLongitude(response.getLocation().getLongitude());
		return result;
	}

	public int getMatchWaitingBetweenLocations(Location l0, Location l1)
	{
		if (l0 == null || l1 == null)
			return 0;

		double lat = l0.getLatitude() - l1.getLatitude();
		double lon = l0.getLongitude() - l1.getLongitude();

		double d = Math.sqrt(lat * lat + lon * lon);

		return (int) (d * MATCH_WAITING_PER_DISTANCE);
	}

	/**
	 * 두 지점간의 거리 계산
	 * 
	 * @param lat1 지점 1 위도
	 * @param lon1 지점 1 경도
	 * @param lat2 지점 2 위도
	 * @param lon2 지점 2 경도
	 * @param unit 거리 표출단위
	 * @return
	 */
	public double distance(double lat1, double lon1, double lat2, double lon2, String unit)
	{

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

		dist = Math.acos(dist);
		// dist = Math.acos(Math.min(dist, 1));
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit == "kilometer")
		{
			dist = dist * 1.609344;
			if (Double.isNaN(dist))
			{
				dist = 0.0;
			}

		} else if (unit == "meter")
		{
			dist = dist * 1609.344;
		}

		return (dist);
	}

	// This function converts decimal degrees to radians
	private static double deg2rad(double deg)
	{
		return (deg * Math.PI / 180.0);
	}

	// This function converts radians to decimal degrees
	private static double rad2deg(double rad)
	{
		return (rad * 180 / Math.PI);
	}
}
