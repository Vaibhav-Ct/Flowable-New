package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeleteProcessDefinitionService {

    private final RepositoryService repositoryService;


    @Transactional
    public boolean deleteDefintion(String definitionKey) {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(definitionKey)
                .list(); // Get all versions

        if (processDefinitions != null) {
            for (ProcessDefinition processDefinition : processDefinitions) {
                repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
            }
            return true; // Successfully deleted
        }
        return false; // Process definition not found
    }
}
