package com.wiz3max.simplerest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

//TODO: clear unused
@Data
public class EmployeeSalary {

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
    @JsonProperty("employer")
    private String employer;
    @JsonProperty("location")
    private String location;
    @JsonProperty("jobTitle")
    private String jobTitle;
    @JsonProperty("yearsAtEmployer")
    private Double yearsAtEmployer;
    @JsonProperty("yearsOfExperience")
    private Double yearsOfExperience;
    @JsonProperty("salary")
    private Double Salary;
    @JsonProperty("signingBonus")
    private Double signingBonus;
    @JsonProperty("annualBonus")
    private Double annualBonus;
    @JsonProperty("annualStockValue/bonus")
    private Double annualStockValueBonus;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("additionalComments")
    private String additionalComments;

}
