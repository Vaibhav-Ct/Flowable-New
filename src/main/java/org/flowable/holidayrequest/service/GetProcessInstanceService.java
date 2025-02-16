package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.HistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GetProcessInstanceService {

    private final HistoryService historyService;

    public Map<String, Object> getProcessInstance(String instanceId) {
        // Fetch process instance history (completed or active)
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId)
                .includeProcessVariables()
                .singleResult();

        if (historicProcessInstance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process instance not found for ID: " + instanceId);
        }

        Map<String, Object> variables = historicProcessInstance.getProcessVariables();

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("processInstanceId", historicProcessInstance.getId());
        response.put("processDefinitionKey", historicProcessInstance.getProcessDefinitionKey());
        response.put("version", historicProcessInstance.getProcessDefinitionVersion());
        response.put("variables", variables);
        response.put("startTime", historicProcessInstance.getStartTime());
        response.put("endTime", historicProcessInstance.getEndTime());

        return response;
    }
}
