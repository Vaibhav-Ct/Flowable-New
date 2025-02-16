package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.service.ProcessInstanceTerminationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/process-instances")
public class ProcessInstanceTerminationController{

    private final ProcessInstanceTerminationService processInstanceService;

    @DeleteMapping("/{instanceId}")
    public ResponseEntity<String> terminateProcessInstance(@PathVariable String instanceId) {
        String status = processInstanceService.terminateProcessInstanceService(instanceId);

        switch (status) {
            case "TERMINATED":
                return ResponseEntity.noContent().build(); // 204 No Content

            case "COMPLETED":
                return ResponseEntity.badRequest().body("Process instance is already completed."); // 400 Bad Request

            default:
                return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
