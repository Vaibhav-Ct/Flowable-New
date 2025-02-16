package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.service.ListProcessDefinitionService;
import org.flowable.holidayrequest.service.ListProcessDefinitionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/process-definitions")
public class ListProcessDefinitionController {

    private final ListProcessDefinitionService processDefinitionService;

    @GetMapping
    public List<Map<String, Object>> getAllProcessDefinitions(
            @RequestParam Optional<String> workspaceId,
            @RequestParam Optional<String> orgId) {
        return processDefinitionService.getAllProcessDefinitions(workspaceId, orgId);
    }
}
