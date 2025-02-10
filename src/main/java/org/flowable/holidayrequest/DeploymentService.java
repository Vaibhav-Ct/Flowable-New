package org.flowable.holidayrequest;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.stereotype.Service;

@Service
public class DeploymentService {

    // Deploys the process definition to Flowable engine
    public Deployment deployProcessDefinition(ProcessEngine processEngine) {
        // Get the RepositoryService from the ProcessEngine
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // Deploy the process definition from the classpath resource
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")  // Specify the BPMN file
                .deploy();

        // Log the deployment ID
        System.out.println("Process deployed with ID: " + deployment.getId());

        return deployment;
    }
}