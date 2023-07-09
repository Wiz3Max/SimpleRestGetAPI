package com.wiz3max.simplerest.controller.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EmployeeSalaryResponse {

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMsg")
    private String errorMsg;

    @JsonProperty("header")
    private List<String> header;

    @JsonProperty("number_record")
    private int numberRecord;

    @JsonProperty("data")
    private List<Map<String, Object>> data;

}
