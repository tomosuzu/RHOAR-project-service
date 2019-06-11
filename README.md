# ProjectService

## Property
* projectId
* owner first name
* owner last name
* owner email address
* project title
* project description
* project status: open, in_progress, completed, cancelled

## Technology
* Vert.x runtime
* MongDB database
* Unit tests.

## Explain my design

This endpoint should reply all projects and these information.
This and below "get" endpoints are similar and should be same response structure.
If it's different, User should change handlers per endpoint. 

* get '/projects'
```$sh
$ curl http://project-service-tosuzuki-freelancer.apps.na311.openshift.opentlc.com/projects
```
* get '/project/:projectId'
```$sh
$ curl http://project-service-tosuzuki-freelancer.apps.na311.openshift.opentlc.com/projects/111111

```
* get '/projects/status/:status'
```$sh
$ curl http://project-service-tosuzuki-freelancer.apps.na311.openshift.opentlc.com/projects/status/open
```


I create it for test. So I make this public.
But If you don't want it to be public, I can hide this.

* post '/project'
sample request
```$sh
$ curl http://project-service-tosuzuki-freelancer.apps.na311.openshift.opentlc.com/project -X POST -H "Content-Type: application/json" -d '{     "projectId": "111114",     "firstName": "firstName4",     "lastName": "lastName4",     "emailAddress": "emailAdress4",     "title": "title4",     "desc": "description4",     "status": "cancelled" }'
```
