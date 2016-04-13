package sqs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

import com.ibm.watson.developer_cloud.alchemy.v1.*;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

public class DataManager {
	private int num_workers = 1;
	private SQSClient client = new SQSClient("xxx");
	private AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());;
	AlchemyLanguage service = new AlchemyLanguage();
	Map<String,Object> params = new HashMap<String, Object>();
	//private CountDownLatch latch;
	
	public DataManager() {
		service.setApiKey("xxx");
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
	}
	
	private class Worker implements Runnable {
		int id;
		JSONParser parser = new JSONParser();
		
		public Worker (int id) {
			this.id = id;
		}
		
		private JSONObject jsonHandler (String message) {
			JSONObject obj = null;
			try{
		         obj = (JSONObject)parser.parse(message);
		         //System.out.println(obj.get("text"));
		         //System.out.println(obj.get("coordinates"));
		       
		      }catch(ParseException pe){
				
		         System.out.println("position: " + pe.getPosition());
		         System.out.println(pe);
		      }
			return obj;
		}
		
		private void pubilshToTopic(String topicArn, String message) {
			//publish to an SNS topic
			PublishRequest publishRequest = new PublishRequest(topicArn, message);
			PublishResult publishResult = snsClient.publish(publishRequest);
			//print MessageId of message published to SNS topic
			System.out.println("MessageId - " + publishResult.getMessageId());
		}
		
		
		@Override
		public void run() {
			while(true) {
				try{
					List<String> messages = client.retrieveMessage();
					for (String str : messages) {
						System.out.println(str);
						JSONObject obj = jsonHandler(str);
						String text  = (String)obj.get("text");
						params.put(AlchemyLanguage.TEXT, text);
						DocumentSentiment sentiment =  service.getSentiment(params);
						obj.put("sentiment", sentiment.getSentiment().getType().toString());
						System.out.println("Worker id : " + id);
						System.out.println(obj);
						pubilshToTopic("xxx", obj.toString());	
					}
				}catch(Exception e) {
				
				}
			}
		}
	}
	
	private void processSQS() {
		
		for (int i = 0; i < num_workers; ++i) {
			new Thread(new Worker(i)).start();
		}

	}
	
	public static void main (String[] args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Exited!");
			}
		});
		
		DataManager dataManager = new DataManager();
		dataManager.processSQS();
	}
}
