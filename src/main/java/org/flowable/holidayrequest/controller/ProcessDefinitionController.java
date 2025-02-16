package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.service.DeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/process-definitions")
public class ProcessDefinitionController {

    private final DeploymentService deploymentService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> deployProcessDefinition(
            @RequestParam("name") String name,
            @RequestParam("bpmnFile") MultipartFile file,
            @RequestHeader("workspaceId") String workspaceId,
            @RequestHeader("orgId") String orgId) {

        Map<String, Object> response = deploymentService.deployProcessDefinition(name, file, workspaceId, orgId);
        return ResponseEntity.ok(response);
    }

}
