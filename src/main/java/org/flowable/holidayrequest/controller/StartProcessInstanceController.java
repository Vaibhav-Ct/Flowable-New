package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.service.StartProcessInstanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/process-instances")
public class StartProcessInstanceController {

    private final StartProcessInstanceService startProcessInstanceService;

    @PostMapping
    public ResponseEntity<Map<String, String>> startProcessInstance(
            @RequestHeader("orgId") String orgId,
            @RequestHeader("workspaceId") String workspaceId,
            @RequestBody Map<String, Object> requestBody) {
        // Extract fields manually from the request body
        String processDefinitionKey = (String) requestBody.get("processDefinitionKey");
        Integer version = requestBody.containsKey("version") ? (Integer) requestBody.get("version") : null;
        Map<String, Object> variables = (Map<String, Object>) requestBody.get("variables");
        // Call the service method
        String processInstanceId = startProcessInstanceService.startProcessInstance(
                processDefinitionKey, version, orgId, workspaceId, variables
        );
        // Return response
        return ResponseEntity.ok(Map.of("processInstanceId", processInstanceId));
    }
}
