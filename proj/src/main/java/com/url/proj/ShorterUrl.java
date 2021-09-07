package com.url.proj;

public class ShorterUrl
{
	private String shortUrl;
	private String prefix = "http://localhost:8080/";
	
	public ShorterUrl() 
	{
		this.shortUrl = prefix;
	};
	
	public ShorterUrl(String shortUrl) {
		this.shortUrl = prefix.concat(shortUrl);
	}
	
	public String getShorterUrl()
	{
		return shortUrl;
	}

}
