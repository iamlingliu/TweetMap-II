from django.conf.urls import url
from . import views

app_name = 'polls'
urlpatterns = [
	url(r'^$', views.index, name='index'),

	url(r'^snshandler$', views.sns_handler, name='sns_handler'),

	url(r'^test/$', views.test, name='test'),

	url(r'^result.json$', views.result, name='result'),

	url(r'^(?P<question_id>[0-9]+)/$', views.detail, name='detail'),

	url(r'^(?P<question_id>[0-9]+)/results/$', views.results, name='results'),

	url(r'^(?P<question_id>[0-9]+)/vote/$', views.vote, name='vote'),

	url(r'^jquery-1.7.2.js$', views.jquery, name='jquery'),

	url(r'^bootstrap.min.css$', views.css, name='css'),

	url(r'^jquery.eventsource.js$', views.eventsource, name='eventsource'),

	url(r'^markerclusterer.js$', views.marker, name='marker'),

]