package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.flowable.task.api.Task;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserTaskCompleteService {

    private final TaskService taskService;

    public void completeTask(String taskId, Map<String, Object> variables) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found for ID: " + taskId);
        }

        // Complete task with provided variables
        taskService.complete(taskId, variables);
    }
}
