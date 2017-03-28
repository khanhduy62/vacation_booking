package com.mgmtp.internship_vacation_booking.dto;

import lombok.Data;

@Data
public class Employee {
    private int id;
    private String label;
    private String value;

    public Employee(int id,String label,String value){
        this.id = id;
        this.label = label;
        this.value = value;
    }
}
