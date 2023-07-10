package com.wiz3max.simplerest.controller.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=true)
public class EmployeeSalaryResponse extends BaseResponse{

    @JsonProperty("data")
    private List<Map<String, Object>> data;

}
