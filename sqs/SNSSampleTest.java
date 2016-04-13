package sqs;

import com.amazonaws.services.sns.AmazonSNSClient;

import java.util.Map;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.DeleteTopicRequest;

public class SNSSampleTest {
	
	public static void deleteTopic(AmazonSNSClient snsClient, String topicArn) {
		//delete an SNS topic
		DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topicArn);
		snsClient.deleteTopic(deleteTopicRequest);
		//get request id for DeleteTopicRequest from SNS metadata
		System.out.println("DeleteTopicRequest - " + snsClient.getCachedResponseMetadata(deleteTopicRequest));
	}
	
	public static void pubilshToTopic(AmazonSNSClient snsClient, String topicArn) {
		//publish to an SNS topic
		String msg = "My text published to SNS topic with email endpoint";
		PublishRequest publishRequest = new PublishRequest(topicArn, msg);
		PublishResult publishResult = snsClient.publish(publishRequest);
		//print MessageId of message published to SNS topic
		System.out.println("MessageId - " + publishResult.getMessageId());
	}
	
	public static void topicSubcription(AmazonSNSClient snsClient, String topicArn) {
		//subscribe to an SNS topic
		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "http", "http://django-env.icp4w7fu4i.us-west-2.elasticbeanstalk.com/polls/snshandler");
		//subRequest.putCustomRequestHeader("CSRF_COOKIE_SECURE", "True");
		//Map<String, String> map = subRequest.getCustomRequestHeaders();
		//for (String str : map.keySet()) {
		//	System.out.println(str);
		//	System.out.println(map.get(str));
		//}
		snsClient.subscribe(subRequest);
		//etattr(request, '_dont_enforce_csrf_checks', True)
		//get request id for SubscribeRequest from SNS metadata
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
		System.out.println("Check and confirm subscription.");

	}
	
	public static void main(String[] args) {
		//create a new SNS client and set endpoint
		AmazonSNSClient snsClient = new AmazonSNSClient(new ClasspathPropertiesFileCredentialsProvider());		                           
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));

		/*
		//create a new SNS topic
		CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyNewTopic");
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		//print TopicArn
		System.out.println(createTopicResult);
		//get request id for CreateTopicRequest from SNS metadata		
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
		*/
		topicSubcription(snsClient, "arn:aws:sns:us-east-1:013157900062:MyNewTopic");
		
//		pubilshToTopic(snsClient,"arn:aws:sns:us-east-1:013157900062:MyNewTopic");
	}
	
}
