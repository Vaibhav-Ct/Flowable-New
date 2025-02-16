package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.service.UserTaskCompleteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/user-tasks")
public class UserTaskCompleteController {

    private final UserTaskCompleteService userTaskService;

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<String> completeUserTask(
            @PathVariable String taskId,
            @RequestBody Map<String, Object> variables) {

        userTaskService.completeTask(taskId, variables);
        return ResponseEntity.ok("Task " + taskId + " completed successfully.");
    }
}
