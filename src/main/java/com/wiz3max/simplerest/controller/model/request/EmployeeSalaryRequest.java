package com.wiz3max.simplerest.controller.model.request;

import com.wiz3max.simplerest.dto.FieldCriteria;
import com.wiz3max.simplerest.handler.RowSorter;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EmployeeSalaryRequest {

    private Map<String, FieldCriteria> filterRowCriteriaMap;
    private List<String> filterColumn;
    private List<String> sortField;
    private RowSorter.SortDirection sortMode = RowSorter.SortDirection.ASC;

}
