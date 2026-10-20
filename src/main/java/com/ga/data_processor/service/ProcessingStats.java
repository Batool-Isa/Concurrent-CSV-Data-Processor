package com.ga.data_processor.service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessingStats {

    private final  AtomicInteger employeeProcessedCounter = new AtomicInteger(0);
    private final AtomicInteger raisedCounter = new AtomicInteger(0);

    private double totalIncrease = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void recordProcessed() {
        employeeProcessedCounter.incrementAndGet();
    }
    public void recordRaise(double amount) {
        raisedCounter.incrementAndGet();
        lock.lock();
        try {
            totalIncrease += amount;
        } finally {
            lock.unlock();
        }
    }

    public void printStats(){
        System.out.println("All Employees Processed!");
        System.out.println("Total Employees Processed: " + employeeProcessedCounter.get());
        System.out.println("Total Salaries Modified: " + raisedCounter.get());
        System.out.println("Total Employees with no raise: " + (employeeProcessedCounter.get() - raisedCounter.get()));
        lock.lock();
        try {
            System.out.printf("Total Salary Increase: %.2f%n", totalIncrease);
        } finally {
            lock.unlock();
        }
    }
}
