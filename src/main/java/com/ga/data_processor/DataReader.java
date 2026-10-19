package com.ga.data_processor;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class DataReader {
    public static List<Employee> readData(){
        try {
            FileReader fileReader = new FileReader("test_employees.csv");
            CSVReader csvReader = new CSVReaderBuilder(fileReader).build();

            List<String[]> content = csvReader.readAll();
            List<Employee> employees = new ArrayList<>();
            for(String[] row: content){
                Employee emp = new Employee();
                emp.setId(Integer.parseInt(row[0]));
                emp.setName(row[1]);
                emp.setSalary(Double.parseDouble(row[2]));
                emp.setDate(LocalDate.parse(row[3]));
                emp.setPosition(row[4]);
                emp.setProjectPercentage(Double.parseDouble(row[5]));
            employees.add(emp);
            }
            System.out.println(employees.size());
            return employees;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
