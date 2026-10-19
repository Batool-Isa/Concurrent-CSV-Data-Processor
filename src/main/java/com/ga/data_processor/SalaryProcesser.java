package com.ga.data_processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SalaryProcesser {
    private final DataReader dataReader;
    AtomicInteger employeeProcessedCounter = new AtomicInteger(0);
    AtomicInteger getRaisedCounter = new AtomicInteger(0);

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

                if (employeeList == null ||employeeList.isEmpty()) {
                    System.out.println("No Employees found!!!");
                    executorService.shutdown();
                    return;
                }

                try {

                    Employee emp = employeeList.get(empCounter);
                    System.out.println("Employee " + emp.getId() + " is being executed by thread: " + Thread.currentThread().getName());
                    double oldSal = emp.getSalary();
                    double salary = calculateRaise(emp);
                    employeeProcessedCounter.incrementAndGet();
                    if(salary > oldSal){
                        getRaisedCounter.incrementAndGet();
                    }
                    emp.setProcessed(true);
                    emp.setSalary(salary);
                    System.out.println("employee Id : " + emp.getId() + " | old salary  : " + oldSal + " | new salary : " + salary);
                    Thread.sleep(1000);
                    System.out.println("emp " + empCounter + " completed processing");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        // Block main thread for 1 min
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("All Employees Processed!");
        System.out.println("Total Employees Processed: " + employeeProcessedCounter.get());
        System.out.println("Total Salaries Modified: " + getRaisedCounter.get());
        System.out.println("Total Employees with no raise: " + (employeeProcessedCounter.get() - getRaisedCounter.get()));
    }


    public static double calculateRaise(Employee employee) {
        double percentage = 0;
        double total = 0;
        if (employee.getProjectPercentage() < 0.6) {
            percentage = 0;
        } else {
            LocalDate joinedDate = employee.getDate();
            LocalDate today = LocalDate.now();
            Period period = Period.between(joinedDate, today);
            int year = period.getYears();
            if (year >= 1) {
                percentage += (0.02 * year);
            }

            String position = employee.getPosition();
            if (position.equals("Employee")) {
                percentage += 0.01;
            } else if (position.equals("Manager")) {
                percentage += 0.02;
            } else if(position.equals("Director")){
                percentage += 0.05;
            }
        }
        total = employee.getSalary() + (employee.getSalary() * percentage);
        return total;
    }


}
