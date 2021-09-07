package com.url.proj;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.infobip.ApiClient;
import com.infobip.ApiException;
import com.infobip.Configuration;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDeliveryDay;
import com.infobip.model.SmsDeliveryTime;
import com.infobip.model.SmsDeliveryTimeWindow;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;

public class SmsSender {

	private String phoneNum = "<phone>";
	private String apiKey = "<key>";
	private String apiPref = "<pref>";
	private String apiPath = "<path>";
	private ApiClient apiClient = new ApiClient();

	private List<Integer> daysInWeek = Arrays.asList(0,0,0,0,0,0,0);

	private Integer weeklyCount = 0;
	private Integer monthlyCount = 0;
	
	public Calendar cal = Calendar.getInstance();
	public Date pastDate = cal.getTime(); //Day Month Date HH:MM:YY UTC YYYY
		
	public SmsSender()
	{
		apiClient.setApiKeyPrefix(this.apiPref);
	    apiClient.setApiKey(this.apiKey);
	    apiClient.setBasePath(this.apiPath);
	    Configuration.setDefaultApiClient(apiClient);
	}
	
	//Used when there is infobip.com URL
	public void sendSmsInfo(String text)
	{
		SendSmsApi sendSmsApi = new SendSmsApi();
	    SmsTextualMessage smsMessage = new SmsTextualMessage();
	    SmsResponse smsResponse = new SmsResponse();
	    
	    smsMessage.addDestinationsItem(new SmsDestination()
	    		.to(phoneNum)).text(text);
	    SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
	    		.messages(Collections.singletonList(smsMessage));
	    
	    try 
	    {
	         smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
	    } 
	    catch (ApiException apiException) 
	    {
	        System.out.println(smsResponse.toString());
	    }
	}
	
	//Used for data sending on a daily, weekly and monthly basis
	//For daily basis sendSmsDaily is used
	//For weekly and monthly sendSmsPeriodicaly is used
	public void sendInfo()
	{
		int position = cal.get(Calendar.DAY_OF_WEEK) - 1;
		
		//Check if the month has been changed
		if (pastDate.toString().substring(4, 8) != cal.getTime().toString().substring(4, 8))
		{
			sendSmsPeriodicaly(8, 1, 8, 2, SmsDeliveryDay.MONDAY,
					monthlyCount.toString());
			
			//Reset monthlyCounter for the next usage
			monthlyCount = 0;
			
			//Update time for a new month
			pastDate = cal.getTime();
		}
		
		//On Monday send data for previous week
		if (pastDate.toString().substring(0, 4) == "Mon")
		{
			sendSmsPeriodicaly(8, 1, 8, 2, SmsDeliveryDay.MONDAY,
					weeklyCount.toString());
			//Reset weeklyCounter for the next usage
			weeklyCount = 0;
			
			//Update time for a new week
			pastDate = cal.getTime();
		}
		
		sendSmsDaily(8, 0, 8, 1,
				daysInWeek.get(position).toString());
		//Update weeklyCounter
		weeklyCount += daysInWeek.get(position);
		
		//Reset day counter for the next usage
		daysInWeek.set(position, 0);
		
		//Update time for a new day
		pastDate = cal.getTime();
		
	}
	
	public void sendSmsDaily(int hourFrom, int minFrom, int hourTo, int minTo, String data)
	{
		SmsDeliveryTimeWindow date = new SmsDeliveryTimeWindow();
		SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest();
		
		date.from(new SmsDeliveryTime().hour(hourFrom).minute(minFrom));
		date.to(new SmsDeliveryTime().hour(hourTo).minute(minTo));
		
		SendSmsApi sendSmsApi = new SendSmsApi();
	    SmsTextualMessage smsMessage = new SmsTextualMessage();
	    SmsResponse smsResponse = new SmsResponse();
		
		smsMessage.deliveryTimeWindow(date)
		    .addDestinationsItem(new SmsDestination().to(phoneNum)).text(data);
		
		smsMessageRequest.messages(Collections.singletonList(smsMessage));
		
		try 
	    {
	         smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
	    } 
	    catch (ApiException apiException) 
	    {
	        System.out.println(smsResponse.toString());
	    }
	}
	
	public void sendSmsPeriodicaly(int hourFrom, int minFrom, int hourTo, int minTo, SmsDeliveryDay day, String data)
	{
		SmsDeliveryTimeWindow date = new SmsDeliveryTimeWindow();
		SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest();
		
		date.addDaysItem(day)
					.from(new SmsDeliveryTime().hour(hourFrom).minute(minFrom));
		
		date.to(new SmsDeliveryTime().hour(hourTo).minute(minTo));
		
		SendSmsApi sendSmsApi = new SendSmsApi();
	    SmsTextualMessage smsMessage = new SmsTextualMessage();
	    SmsResponse smsResponse = new SmsResponse();
		
		smsMessage.deliveryTimeWindow(date)
		    .addDestinationsItem(new SmsDestination().to(phoneNum)).text(data);
		
		smsMessageRequest.messages(Collections.singletonList(smsMessage));
		
		try 
	    {
	         smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
	    } 
	    catch (ApiException apiException) 
	    {
	        System.out.println(smsResponse.toString());
	    }
	}
	
	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}
	
	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void addCounter() 
	{
		int position = cal.get(Calendar.DAY_OF_WEEK) - 1;
		daysInWeek.set(position, daysInWeek.get(position) + 1);	
	}
	
}
