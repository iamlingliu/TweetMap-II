package sqs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;

public class SQSClient {
	
	AWSCredentials credentials;
	AmazonSQS sqs;
	String myQueueUrl;
	ReceiveMessageRequest receiveMessageRequest;
	
	public SQSClient (String url) {
		myQueueUrl = url;
		
		try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        sqs = new AmazonSQSClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usWest2);
        //long-polling
        receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl).withWaitTimeSeconds(10);
	}
	
	public synchronized List<String> retrieveMessage() {
		List<String> res = new ArrayList<String>();
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		int size = messages.size();
        for (Message message : messages) {
            System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
            res.add(message.getBody());
            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
        }  
        
        //Delete the retrieved message
        if (size > 0) {
        	System.out.println("Deleting a message.\n");
        	String messageReceiptHandle = messages.get(0).getReceiptHandle();
        	sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
        }
        
        return res;
	}
	
}
