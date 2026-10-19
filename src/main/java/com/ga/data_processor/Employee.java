package com.ga.data_processor;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Employee {
    private int id ;
    private  String name;
    private double salary;
    private LocalDate date;
    private String position;
    private double projectPercentage;
}
