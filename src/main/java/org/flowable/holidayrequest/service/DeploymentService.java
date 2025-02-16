package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.holidayrequest.entity.ProcessDefinitionEntity;
import org.flowable.holidayrequest.repository.ProcessDefinitionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DeploymentService {

    private final ProcessEngine processEngine;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public Map<String, Object> deployProcessDefinition(String name, MultipartFile file, String workspaceId, String orgId) {
        if (name == null || name.isEmpty() || file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields: name, bpmnFile");
        }

        RepositoryService repositoryService = processEngine.getRepositoryService();

        try {
            Deployment deployment = repositoryService.createDeployment()
                    .name(name)
                    .addInputStream(file.getOriginalFilename(), file.getInputStream())
                    .deploy();

            // Save deployment metadata in DB
            ProcessDefinitionEntity entity = new ProcessDefinitionEntity();
            entity.setDeploymentId(deployment.getId());
            entity.setProcessDefinitionKey(name);
            entity.setWorkspaceId(workspaceId);
            entity.setOrgId(orgId);
            processDefinitionRepository.save(entity);

            Map<String, Object> response = new HashMap<>();
            response.put("deploymentId", deployment.getId());
            response.put("processDefinitions", repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .list()
                    .stream()
                    .map(pd -> Map.of("processDefinitionKey", pd.getKey(), "version", pd.getVersion()))
                    .toList());
            return response;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read BPMN file", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Deployment failed due to Flowable engine issue", e);
        }
    }
}
