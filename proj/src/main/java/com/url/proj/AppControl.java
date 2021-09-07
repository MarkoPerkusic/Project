package com.url.proj;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AppControl {
	
	public HashMap<String, String> mapHashToLongUrl = new HashMap<String, String>();
	public HashMap<String, ShorterUrl> mapLongToShorterUrl = new HashMap<String, ShorterUrl>();
	public SmsSender smsSender = new SmsSender();
	
	@PostMapping("/shorten")
	public ShorterUrl shortUrl(@RequestParam String long_url)
	{   
		smsSender.addCounter();
		
		if(!mapLongToShorterUrl.containsKey(long_url))
		{
			LongerUrl longUrl = new LongerUrl(long_url);
			ShorterUrl shortUrl = new ShorterUrl(longUrl.convertToShort());
			
			if(long_url.contains("infobip.com"))
				smsSender.sendSmsInfo("URL contains infobip.com");
			
			mapHashToLongUrl.put(shortUrl.getShorterUrl(), longUrl.getLongerUrl());
			mapLongToShorterUrl.put(longUrl.getLongerUrl(), shortUrl);
			return shortUrl;
		}
		else 
		{
			return mapLongToShorterUrl.get(long_url);
		}
	}
	
	@GetMapping("/{hash:^(?!index).*}")
	public void redirectUrl(@PathVariable String hash, HttpServletResponse httpReply)
	{
		String domain = "http://localhost:8080/".concat(hash);
		
		if (mapHashToLongUrl.containsKey(domain)) 
		{
			String location = mapHashToLongUrl.get(domain);
			
			try {
				httpReply.sendRedirect(location);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
