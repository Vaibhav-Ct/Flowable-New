### Getting started with the Flowable REST API
This section shows the same example as the previous section: deploying a process definition, starting a process instance, getting a task list and completing a task. If you haven’t read that section, it might be good to skim through it to get an idea of what is done there.

This time, the Flowable REST API is used rather than the Java API. You’ll soon notice that the REST API closely matches the Java API, and knowing one automatically means that you can find your way around the other.

To get a full, detailed overview of the Flowable REST API, check out the REST API chapter.

Setting up the REST application
When you download the .zip file from the flowable.org website, the REST application can be found in the wars folder. You’ll need a servlet container, such as Tomcat, Jetty, and so on, to run the WAR file.

Note: The flowable-rest.war is based on Spring Boot, which means that it supports the Tomcat / Jetty servlet containers supported by Spring Boot.

When using Tomcat the steps are as follows:

Download and unzip the latest and greatest Tomcat zip file (choose the 'Core' distribution from the Tomcat website).

# Step-1:
Copy the flowable-rest.war file from the wars folder of the unzipped Flowable distribution to the webapps folder of the unzipped Tomcat folder.

On the command line, go to the bin folder of the Tomcat folder.

Execute './catalina run' to boot up the Tomcat server.

During the server boot up, you’ll notice some Flowable logging messages passing by. At the end, a message like 'INFO [main] org.apache.catalina.startup.Catalina.start Server startup in xyz ms' indicates that the server is ready to receive requests. Note that by default an in-memory H2 database instance is used, which means that data won’t survive a server restart.

In the following sections, we’ll use cURL to demonstrate the various REST calls. All REST calls are by default protected with basic authentication. The user 'rest-admin' with password 'test' is used in all calls.

After boot up, verify the application is running correctly by executing

# Step -2:
curl --user rest-admin:test http://localhost:8080/flowable-rest/service/management/engine
If you get back a proper json response, the REST API is up and running.

Deploying a process definition
The first step is to deploy a process definition. With the REST API, this is done by uploading a .bpmn20.xml file (or .zip file for multiple process definitions) as 'multipart/formdata':

# Step-3
curl --user rest-admin:test  -X POST -F "file=@holiday-request.bpmn20.xml" http://localhost:8080/flowable-rest/service/repository/deployments
To verify that the process definition is deployed correctly, the list of process definitions can be requested:

curl --user rest-admin:test http://localhost:8080/flowable-rest/service/repository/process-definitions
which returns a list of all process definitions currently deployed to the engine.

# Step-4
Start a process instance
Starting a process instance through the REST API is similar to doing the same through the Java API: a key is provided to identify the process definition to use along with a map of initial process variables:

curl --user rest-admin:test -H "Content-Type: application/json" -X POST -d '{ "processDefinitionKey":"holidayRequest", "variables": [ { "name":"employee", "value": "John Doe" }, { "name":"nrOfHolidays", "value": 7 }]}' http://localhost:8080/flowable-rest/service/runtime/process-instances
which returns something like

{"id":"43","url":"http://localhost:8080/flowable-rest/service/runtime/process-instances/43","businessKey":null,"suspended":false,"ended":false,"processDefinitionId":"holidayRequest:1:42","processDefinitionUrl":"http://localhost:8080/flowable-rest/service/repository/process-definitions/holidayRequest:1:42","activityId":null,"variables":[],"tenantId":"","completed":false}

# Step-5
Task list and completing a task
When the process instance is started, the first task is assigned to the 'managers' group. To get all tasks for this group, a task query can be done through the REST API:

curl --user rest-admin:test -H "Content-Type: application/json" -X POST -d '{ "candidateGroup" : "managers" }' http://localhost:8080/flowable-rest/service/query/tasks
which returns a list of all tasks for the 'managers' group

# Step-6
Such a task can now be completed using:

curl --user rest-admin:test -H "Content-Type: application/json" -X POST -d '{ "action" : "complete", "variables" : [ { "name" : "approved", "value" : true} ]  }' http://localhost:8080/flowable-rest/service/runtime/tasks/25
However, you most likely will get an error like:

{"message":"Internal server error","exception":"couldn't instantiate class org.flowable.CallExternalSystemDelegate"}

# Step-7
This means that the engine couldn’t find the CallExternalSystemDelegate class that is referenced in the service task. To solve this, the class needs to be put on the classpath of the application (which will require a restart). Create the class as described in this section, package it up as a JAR and put it in the WEB-INF/lib folder of the flowable-rest folder under the webapps folder of Tomcat.
