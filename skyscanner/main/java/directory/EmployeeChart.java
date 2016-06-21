package directory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * @author Vivek Mittal
 */
public class EmployeeChart {

    public static final String DELIMITER = " ";

    public static void main(String[] args) {
        EmployeeDirectory employeeDirectory = new EmployeeDirectory();

        forEvery(employeeString -> {
            final String[] split = employeeString.split(DELIMITER);
            final String managerName = split[0];

            final Employee manager = Optional.ofNullable(employeeDirectory.get(managerName))
                    .orElseGet(() -> {
                        final Employee employee = new Employee(managerName, null);
                        employeeDirectory.put(managerName, employee);
                        return employee;
                    });

            for (int j = 1; j < split.length; j++) {
                final String employeeName = split[j];
                Optional.ofNullable(employeeDirectory.get(employeeName))
                        .orElseGet(() -> {
                            final Employee employee = new Employee(employeeName, manager);
                            employeeDirectory.put(employeeName, employee);
                            return employee;
                        });
            }
        });

        employeeDirectory.forEachLevel(employees -> employees
                .stream()
                .map(Employee::getName)
                .reduce((e1, e2) -> e1 + " " + e2)
                .ifPresent(System.out::println));
    }

    public static void forEvery(Consumer<String> doWithEachInput) {
        Scanner sc = new Scanner(System.in);
        int linesToRead = sc.nextInt();

        sc.nextLine();
        for (int i = 0; i < linesToRead; i++) {
            final String inputString = sc.nextLine();
            doWithEachInput.accept(inputString);
        }
    }

    public static class EmployeeDirectory extends HashMap<String, Employee> {
        private Employee chiefEmployee;

        @Override
        public Employee put(String key, Employee value) {
            if (value.getManager() == null) {
                chiefEmployee = value;
            }
            return super.put(key, value);
        }

        public void forEachLevel(Consumer<List<Employee>> operation) {
            forEachLevel(Collections.singletonList(chiefEmployee), operation);
        }

        private void forEachLevel(List<Employee> employees, Consumer<List<Employee>> operation) {
            List<Employee> nextLevelEmployees = new ArrayList<>();
            operation.accept(employees);

            for (Employee employee : employees) {
                nextLevelEmployees.addAll(employee.getSubordinates());
            }

            if (!nextLevelEmployees.isEmpty()) {
                forEachLevel(nextLevelEmployees, operation);
            }
        }
    }

    public static class Employee {
        private final String name;
        private final Employee manager;
        private final List<Employee> subordinates = new ArrayList<>();

        public Employee(String name, Employee manager) {
            this.name = name;
            this.manager = manager;

            if (this.manager != null) {
                this.manager.addSubordinate(this);
            }
        }

        private void addSubordinate(Employee employee) {
            this.subordinates.add(employee);
        }

        public String getName() {
            return name;
        }

        public List<Employee> getSubordinates() {
            return subordinates;
        }

        public Employee getManager() {
            return manager;
        }
    }
}
