package com.ga.data_processor.service;

import com.ga.data_processor.model.Employee;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class DataReader {
    public List<Employee> readData(){
        try (FileReader fileReader = new FileReader("test_employees.csv");
             CSVReader csvReader = new CSVReaderBuilder(fileReader).build()) {

            List<String[]> content = csvReader.readAll();
            List<Employee> employees = new ArrayList<>();
            for(String[] row: content){
                Employee emp = new Employee();
                emp.setId(Integer.parseInt(row[0].trim()));
                emp.setName(row[1].trim());
                emp.setSalary(Double.parseDouble(row[2].trim()));
                emp.setDate(LocalDate.parse(row[3].trim()));
                emp.setPosition(row[4].trim());
                emp.setProjectPercentage(Double.parseDouble(row[5].trim()));
                employees.add(emp);
            }
            return employees;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
