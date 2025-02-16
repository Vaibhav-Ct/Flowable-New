package org.flowable.holidayrequest.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@RequiredArgsConstructor
public class UserTaskDTO {
    private String taskId;
    private String name;
    private String assignee;
    private String processInstanceId;
    public UserTaskDTO(String taskId, String name, String assignee, String processInstanceId) {
        this.taskId = taskId;
        this.name = name;
        this.assignee = assignee;
        this.processInstanceId = processInstanceId;
    }
}
