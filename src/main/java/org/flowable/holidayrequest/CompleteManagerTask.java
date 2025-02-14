package org.flowable.holidayrequest;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

public class CompleteManagerTask {
    public static void completeTask(ProcessEngine processEngine, Map<String, Object> variables) {
        TaskService taskService = processEngine.getTaskService();

        // Fetch only manager tasks (Assuming they are assigned to "manager")
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateGroup("manager") // Ensures only manager's tasks
                .list();

        if (taskList.isEmpty()) {
            System.out.println("No active manager tasks found.");
            return;
        }
        // Pick the first task and complete it
        Task task = taskList.get(0);
        System.out.println("Completing Manager Task: " + task.getName());

        // Complete the task with process variables
        taskService.complete(task.getId(), variables);
    }

}
