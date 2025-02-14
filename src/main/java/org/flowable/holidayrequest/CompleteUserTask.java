package org.flowable.holidayrequest;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

public class CompleteUserTask {
    public static void completeTask(ProcessEngine processEngine, Map<String, Object> variables) {
        TaskService taskService = processEngine.getTaskService();

        // Fetch all available tasks (Assuming single user task at a time)
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateGroup("user") // Ensures it's a user task
                .list();

        if (taskList.isEmpty()) {
            System.out.println("No active user tasks found.");
            return;
        }

        // Pick the first task and complete it
        Task task = taskList.get(0);
        System.out.println("Completing User Task: " + task.getName());

        // Complete the task with process variables
        taskService.complete(task.getId(), variables);
    }
}
