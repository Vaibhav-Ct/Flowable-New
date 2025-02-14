package org.flowable.holidayrequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

public class ManagerInput {
    public static Map<String, Object> getManagerInput(ProcessEngine processEngine) {
        // Collect user input
        TaskService taskService = processEngine.getTaskService();

        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateGroup("manager") // Ensures only manager's tasks
                .list();
        Task task = taskList.get(0);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        System.out.println(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

        System.out.println("Do you approve the holiday request? (y/n)");
        Scanner scanner = new Scanner(System.in);
        Map<String, Object> variables = new HashMap<>();
        // Manager approval input
        boolean approved = scanner.nextLine().equalsIgnoreCase("y");
        variables = new HashMap<>();
        variables.put("approved", approved);
        return variables;
    }
}
