package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.service.GetProcessInstanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/process-instances")
public class GetProcessInstanceController {

    private final GetProcessInstanceService getProcessInstanceService;

    @GetMapping("/{instanceId}")
    public ResponseEntity<Map<String, Object>> getProcessInstance(@PathVariable String instanceId) {
        Map<String, Object> response = getProcessInstanceService.getProcessInstance(instanceId);
        return ResponseEntity.ok(response);
    }
}
