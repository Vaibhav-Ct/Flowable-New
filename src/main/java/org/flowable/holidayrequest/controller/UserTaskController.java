package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.dto.UserTaskDTO;
import org.flowable.holidayrequest.service.UserTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/user-tasks")
public class UserTaskController {

    private final UserTaskService userTaskService;

    @GetMapping
    public ResponseEntity<List<UserTaskDTO>> getUserTasks(
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String processInstanceId) {

        List<UserTaskDTO> tasks = userTaskService.getUserTasks(assignee, processInstanceId);
        return ResponseEntity.ok(tasks);
    }
}
