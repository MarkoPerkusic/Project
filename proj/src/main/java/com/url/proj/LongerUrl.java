package com.url.proj;

import java.util.Random;

public class LongerUrl {
	private String longUrl;
	private ShorterUrl shortUrl;
	
	public LongerUrl() {};
	
	public LongerUrl(String longUrl) {
		this.longUrl = longUrl;
	}
	
	public String getLongerUrl()
	{
		return longUrl;
	}
	
	public void setShortClass(ShorterUrl shortUrl) 
	{
		this.shortUrl = shortUrl;
	}
	
	public ShorterUrl getShortClass()
	{
		return shortUrl;
	}
	
	public String convertToShort()
	{
		String newUrl = "";
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				   + "abcdefghijklmnoprstuwxyz"
				   + "0123456789";
		
		Random rand = new Random();
		//Size of new shorter URL
		int max = 11;
		int min = 5;
		
		//Size of the new URL will have size from min to max
		int newUrlLength = rand. nextInt(max - min) + min;
		
		for (int i = 0; i < newUrlLength; i++) {
			int index = rand.nextInt(str.length());
			newUrl = newUrl + str.charAt(index);
		}
		
		//Return domain.concat(newUrl);
		return newUrl;
	}
}
