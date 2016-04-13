package sqs;

import com.ibm.watson.developer_cloud.alchemy.v1.*;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import java.util.*;

public class AlchemyTest {
	
	public static void main (String[] args) {
		AlchemyLanguage service = new AlchemyLanguage();
		service.setApiKey("255573da8448ebf06e7862ee2cc2870eba81e383");

		Map<String,Object> params = new HashMap<String, Object>();
		params.put(AlchemyLanguage.TEXT, "IBM Watson won the Jeopardy television show hosted by Alex Trebek");
		DocumentSentiment sentiment =  service.getSentiment(params);
		System.out.println(sentiment);
		params.put(AlchemyLanguage.TEXT, "I don't feel good");
		sentiment =  service.getSentiment(params);
		System.out.println(sentiment);
		for (String s : params.keySet()) {
			System.out.println(s);
			System.out.println(params.get(s));
		}
		
	}
	
		
	}
