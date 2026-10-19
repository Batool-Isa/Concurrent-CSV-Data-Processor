package com.ga.data_processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SalaryProcesser {
    private final DataReader dataReader;

    public SalaryProcesser(DataReader dataReader) {
        this.dataReader = dataReader;
    }
    public void process() {
        // create a new thread pool with only 3 reusable threads
        ExecutorService executorService = Executors.newFixedThreadPool(3);


        List<Employee> employeeList = dataReader.readData();
        for (int i = 0; i < employeeList.size(); i++) {
            final int empCounter = i;
            executorService.submit(() -> {
                Employee emp = employeeList.get(empCounter);
                System.out.println("Employee " + emp.getId() + " is being executed by thread: " + Thread.currentThread().getName());

                try {
                    double oldSal = emp.getSalary();
                double salary = calculateRaise(emp);
                    System.out.println("emp "+emp.getId()+" , old salary "+oldSal+" new sal : "+salary);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("emp " + empCounter + " completed.");
            });
        }

        // Shutdown the executor after all tasks are completed
        executorService.shutdown();
    }




    public static double calculateRaise(Employee employee) {
        double percentage = 0;
        double total = 0;
        if (employee.getSalary() < 0.6) {
            percentage = 0;
        } else {
            LocalDate joinedDate = employee.getDate();
            LocalDate today = LocalDate.now();
            Period period = Period.between(joinedDate, today);
            int year = period.getYears();
            if (year > 1) {
                percentage += (0.02*year);
            }

            String position = employee.getPosition();
            if (position.equals("Employee")) {
               percentage +=0.1;
            } else if (position.equals("Manager")) {
                percentage +=0.2;
            } else {
                percentage +=0.5;
            }
        }
        total = employee.getSalary() + (employee.getSalary()*percentage);
        return total;
    }



}
