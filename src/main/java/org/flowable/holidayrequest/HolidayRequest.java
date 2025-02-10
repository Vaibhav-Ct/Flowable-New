package org.flowable.holidayrequest;

import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
public class HolidayRequest implements CommandLineRunner {

	@Autowired
	private DeploymentService deploymentService;

	private ProcessEngine processEngine;

	public static void main(String[] args) {
		SpringApplication.run(HolidayRequest.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Initialize the Process Engine
		ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
				.setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
				.setJdbcUsername("sa")
				.setJdbcPassword("")
				.setJdbcDriver("org.h2.Driver")
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

		// Build the Process Engine
		processEngine = cfg.buildProcessEngine();

		// Deploy the process definition
		Deployment deployment = deploymentService.deployProcessDefinition(processEngine);

		// Query the process definition
		RepositoryService repositoryService = processEngine.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deployment.getId()) // Query based on the deployment ID
				.singleResult();

		// Output the process definition name
		if (processDefinition != null) {
			System.out.println("Found process definition : " + processDefinition.getName());
		} else {
			System.out.println("No process definition found for the deployment.");
		}

		// Start the process instance using user input
		Scanner scanner = new Scanner(System.in);

		Map<String, Object> variables;

		for(int j=1;j<=3;j++) {

			// Collect process variables from the user
			System.out.println("Who are you?");
			String employee = scanner.nextLine();

			System.out.println("How many holidays do you want to request?");
			Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

			System.out.println("Why do you need them?");
			String description = scanner.nextLine();

			// Prepare variables for starting the process
			RuntimeService runtimeService = processEngine.getRuntimeService();
			variables = new HashMap<>();
			variables.put("employee", employee);
			variables.put("nrOfHolidays", nrOfHolidays);
			variables.put("description", description);

			// Start the process instance
			runtimeService.startProcessInstanceByKey("holidayRequest", variables);

			// Query tasks for the 'managers' group
			TaskService taskService = processEngine.getTaskService();
			List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
			System.out.println("You have " + tasks.size() + " tasks:");

			// Display task list and ask the manager to complete a task
			for (int i = 0; i < tasks.size(); i++) {
				System.out.println((i + 1) + ") " + tasks.get(i).getName());
			}

			System.out.println("Which task would you like to complete?");
			int taskIndex = Integer.valueOf(scanner.nextLine());
			Task task = tasks.get(taskIndex - 1);

			// Get process variables and display them to the manager
			Map<String, Object> processVariables = taskService.getVariables(task.getId());
			System.out.println(processVariables.get("employee") + " wants " +
					processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

			// Manager approval input
			boolean approved = scanner.nextLine().toLowerCase().equals("y");

			// Complete the task and pass approval variable
			variables = new HashMap<>();
			variables.put("approved", approved);
			taskService.complete(task.getId(), variables);

			System.out.println("Task completed. Approval status: " + (approved ? "Approved" : "Rejected"));
		}
	}
}


