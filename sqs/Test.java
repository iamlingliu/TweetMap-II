package sqs;

import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Test {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		JSONParser parser = new JSONParser();
//		String s = "{\"text\":\"This #Healthcare #job might be a great fit for you: Registered Nurse Operating Room OR travel - https://t.co/U5pjhG5rFk #Boston, MA #Hiring\",\"coordinates\":\"[-71.0765188, 42.353068]\"}";
//		
//		 try{
//	         JSONObject obj = (JSONObject)parser.parse(s);
//	         System.out.println(obj.get("text"));
//	         System.out.println(obj.get("coordinates"));
//	       
//	      }catch(ParseException pe){
//			
//	         System.out.println("position: " + pe.getPosition());
//	         System.out.println(pe);
//	      }
		String s = "dinner";
		byte[] bytes = s.getBytes("UFT-8");
		System.out.println(bytes.toString());
	}
}
