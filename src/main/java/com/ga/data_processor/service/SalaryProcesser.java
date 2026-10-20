package com.ga.data_processor.service;
import com.ga.data_processor.model.Employee;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Service
public class SalaryProcesser {
    private final DataReader dataReader;
    private final Semaphore semaphore = new Semaphore(2);


    public SalaryProcesser(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public void process() {
        List<Employee> employeeList = dataReader.readData();
        if (employeeList == null || employeeList.isEmpty()) {
            System.out.println("No Employees found!!!");
            return;
        }
        ProcessingStats stats = new ProcessingStats();
        // create a new thread pool with only 5 reusable threads
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < employeeList.size(); i++) {
            final int empCounter = i;

            executorService.submit(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                    return;
                }

                try {

                    Employee emp = employeeList.get(empCounter);
                    System.out.println("Employee " + emp.getId() + " is being executed by thread: " + Thread.currentThread().getName());
                    double oldSal = emp.getSalary();
                    double salary = calculateRaise(emp);
                    stats.recordProcessed();

                    if (salary > oldSal) {
                        stats.recordRaise(salary - oldSal);
                    }
                    emp.setProcessed(true);
                    emp.setSalary(salary);
                    System.out.println("employee Id : " + emp.getId() + " | old salary  : " + oldSal + " | new salary : " + salary);
                    Thread.sleep(1000);
                    System.out.println("emp " + empCounter + " completed processing");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    System.out.println("Failed to process employee at index " + empCounter + ": " + e.getMessage());
                } finally {
                    semaphore.release();
                }
            });
        }

        executorService.shutdown();
        // Block main thread for 1 min
        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("Processing timed out, stopping remaining tasks.");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        stats.printStats();
    }


    public double calculateRaise(Employee employee) {
        double percentage = 0;
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
            if ("Employee".equals(position)) {
                percentage += 0.01;
            } else if ("Manager".equals(position)) {
                percentage += 0.02;
            } else if ("Director".equals(position)) {
                percentage += 0.05;
            }

            if (employee.getProjectPercentage() > 0.8) {
                percentage *= 1.5;
            }
        }
        return employee.getSalary() + (employee.getSalary() * percentage);

    }


}
