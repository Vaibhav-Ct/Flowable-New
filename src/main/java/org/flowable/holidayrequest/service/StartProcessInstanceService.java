package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.holidayrequest.entity.ProcessDefinitionEntity;
import org.flowable.holidayrequest.repository.ProcessDefinitionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class StartProcessInstanceService {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;
    private final ProcessDefinitionRepository processDefinitionRepository;

    @Transactional
    public String startProcessInstance(String processDefinitionKey, Integer version, String orgId, String workspaceId, Map<String, Object> variables) {
        ProcessDefinition processDefinition;

        // Get the latest available version
        ProcessDefinition latestProcessDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

        if (latestProcessDefinition == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No process definition found for key: " + processDefinitionKey);
        }

        if (version == null) {
            // Use the latest version if not specified
            processDefinition = latestProcessDefinition;
        } else {
            // Fetch the requested version
            processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey)
                    .processDefinitionVersion(version)
                    .singleResult();

            // If the requested version does not exist, return a meaningful error
            if (processDefinition == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Requested version " + version + " does not exist for process key: "
                        + processDefinitionKey + ". Latest available version is " + latestProcessDefinition.getVersion());
            }
        }

        // Ensure the process definition matches orgId and workspaceId
        if (!processDefinitionHasAccess(processDefinition, orgId, workspaceId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Process definition " + processDefinition.getId()
                    + " does not belong to orgId: " + orgId + " and workspaceId: " + workspaceId);
        }

        // Start the process using the extracted processDefinitionId
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(processDefinition.getId()) // Using processDefinitionId
                .variables(variables)
                .start();

        return processInstance.getId(); // Return the started process instance ID
    }

    private boolean processDefinitionHasAccess(ProcessDefinition processDefinition, String orgId, String workspaceId) {
        ProcessDefinitionEntity entity = processDefinitionRepository.findByDeploymentId(processDefinition.getDeploymentId());
        if(entity==null){
            return false;
        }
        return entity.getOrgId().equals(orgId) && entity.getWorkspaceId().equals(workspaceId);
    }
}
