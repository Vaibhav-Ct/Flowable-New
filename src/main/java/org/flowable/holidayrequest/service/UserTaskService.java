package org.flowable.holidayrequest.service;

import lombok.RequiredArgsConstructor;
import org.flowable.engine.TaskService;
import org.flowable.holidayrequest.dto.UserTaskDTO;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserTaskService {

    private final TaskService taskService;

    public List<UserTaskDTO> getUserTasks(String assignee, String processInstanceId) {
        var query = taskService.createTaskQuery();

        if (assignee != null && !assignee.isEmpty()) {
            query.taskAssignee(assignee);
        }
        if (processInstanceId != null && !processInstanceId.isEmpty()) {
            query.processInstanceId(processInstanceId);
        }

        List<Task> tasks = query.list();

        return tasks.stream().map(task -> new UserTaskDTO(
                task.getId(),
                task.getName(),
                task.getAssignee(),
                task.getProcessInstanceId()
        )).collect(Collectors.toList());
    }
}
