package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessInstanceTerminationService {

    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    public String terminateProcessInstanceService(String instanceId) {
        // Check if process instance is still running
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instanceId)
                .singleResult();

        if (processInstance != null) {
            // Process is still running → Terminate it
            runtimeService.deleteProcessInstance(instanceId, "Terminated by user request");
            return "TERMINATED";
        }

        // Check if the process instance is already completed
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .singleResult();

        if (historicProcessInstance != null) {
            return "COMPLETED";  // Process already completed
        }

        return "NOT_FOUND"; // No such process instance exists
    }
}
