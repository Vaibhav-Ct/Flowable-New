package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.holidayrequest.entity.ProcessDefinitionEntity;
import org.flowable.holidayrequest.repository.ProcessDefinitionRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListProcessDefinitionService {

    private final RepositoryService repositoryService;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public List<Map<String, Object>> getAllProcessDefinitions(Optional<String> workspaceId, Optional<String> orgId) {
        return repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list()
                .stream()
                .map(pd -> {
                    Deployment deployment = repositoryService.createDeploymentQuery()
                            .deploymentId(pd.getDeploymentId())
                            .singleResult();

                    String deploymentTime = (deployment != null && deployment.getDeploymentTime() != null)
                            ? deployment.getDeploymentTime().toString()
                            : "N/A";

                    ProcessDefinitionEntity entity = processDefinitionRepository.findByDeploymentId(pd.getDeploymentId());
                    String dbWorkspaceId = entity != null ? entity.getWorkspaceId() : null;
                    String dbOrgId = entity != null ? entity.getOrgId() : null;

                    Map<String, Object> processDefinitionMap = new HashMap<>();
                    processDefinitionMap.put("processDefinitionKey", pd.getKey());
                    processDefinitionMap.put("version", pd.getVersion());
                    processDefinitionMap.put("deploymentTime", deploymentTime);
                    processDefinitionMap.put("workspaceId", dbWorkspaceId);
                    processDefinitionMap.put("orgId", dbOrgId);

                    return processDefinitionMap;
                })
                .filter(pd ->
                        (workspaceId.isEmpty() || workspaceId.get().equals(pd.get("workspaceId"))) &&  // Match workspaceId
                                (orgId.isEmpty() || orgId.get().equals(pd.get("orgId")))  // Match orgId
                )
                .map(pd -> Map.of(  // Remove workspaceId and orgId from response
                        "processDefinitionKey", pd.get("processDefinitionKey"),
                        "version", pd.get("version"),
                        "deploymentTime", pd.get("deploymentTime")
                ))
                .collect(Collectors.toList());
    }
}
