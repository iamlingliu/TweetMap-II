from django.shortcuts import render
from django.http import HttpResponse
from .models import Question, Data
from django.template import loader
from django.shortcuts import render,get_object_or_404
import datetime
import urllib2
from django.utils import timezone
from django.http import FileResponse, JsonResponse
from django.views.decorators.csrf import ensure_csrf_cookie, csrf_exempt
import json
import sys
from elasticsearch import Elasticsearch, RequestsHttpConnection
import time
from pusher import Pusher


def index(request):
	
	return render(request, 'polls/index2.html', {"topic" : "Easter"})

@csrf_exempt
def sns_handler(request):
	
	messageType = request.META['HTTP_X_AMZ_SNS_MESSAGE_TYPE']
	parsed_body = json.loads(request.body)
	if messageType == "SubscriptionConfirmation":
		url = parsed_body["SubscribeURL"]
		serialized_data = urllib2.urlopen(url).read()
	elif messageType == "Notification":
		message = parsed_body["Message"]
		j_msg = json.loads(message)
		print (type(j_msg['coordinates']))
		print (j_msg['coordinates'])
		j_msg['coordinates'] = j_msg['coordinates']['coordinates']
		print(j_msg)
		message = str(json.dumps(j_msg))
		print(message)

		pusher_client = Pusher(
	  		app_id='xxx',
	  		key='xxx',
	  		secret='xxx',
	  		ssl=True
		)
		pusher_client.trigger('test_channel', 'my_event', {'message': message})
		
		es = Elasticsearch(
  			[
  			'xxx'
			  ],
			  use_ssl=True,
			  verify_certs=True,
			  connection_class = RequestsHttpConnection
		)
		es.create(index="tweets", doc_type="tweet", body=j_msg)
  			
	return HttpResponse('', status=200)

def test(request):
	print 'Ah'
	response = JsonResponse({"id": 1,
							"text": "I am from Twitter",
							"time" : "",
							"lon": 45,
							"lat": -75})
	return response

def result(request):
	print 'ha'
	response = FileResponse(open('polls/result.json','rb'))
	return response

def detail(request, question_id):
	question = get_object_or_404(Question, pk=question_id)
	return render(request, 'polls/detail.html', {'question': question})

def results(request, question_id):
	print "oh"
	response = "You are looking at the results of question %s."
	return HttpResponse(response % question_id)

def vote(request, question_id):
	return HttpResponse("You are voting on question %s." %question_id)

def jquery(request):
	return FileResponse(open('polls/jquery-1.7.2.js','rb'))

def css(request):
	return FileResponse(open('polls/bootstrap.min.css','rb'))

def eventsource(request):
	return FileResponse(open('polls/jquery.eventsource.js','rb'))

def marker(request):
	return FileResponse(open('polls/markerclusterer.js','rb'))

def pusher(request):
	pusher_client = pusher.Pusher(
	  app_id='196270',
	  key='xxx',
	  secret='xxx',
	  ssl=True
	)
	pusher_client.trigger('test_channel', 'my_event', {'message': 'hello world'})
	return HttpResponse("",status=200)

	

