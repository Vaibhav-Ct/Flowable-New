package org.flowable.holidayrequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserInput {
    public static Map<String, Object> getUserInput() {
        // Collect user input
        Scanner scanner = new Scanner(System.in);
        Map<String, Object> variables = new HashMap<>();

        System.out.println("Who are you?");
        String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        System.out.println("Why do you need them?");
        String description = scanner.nextLine();

        // Store variables
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);

        // Complete the user task
        System.out.println("Completing the user task...");
        return variables;
    }
}
