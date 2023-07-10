package com.wiz3max.simplerest.controller.model.request;

import com.wiz3max.simplerest.dto.FieldCriteria;
import com.wiz3max.simplerest.handler.RowSorter;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeSalaryRequest {

    private List<FieldCriteria> filterRowCriterias;
    private List<String> filterColumn;
    private List<String> sortField;
    private RowSorter.SortDirection sortMode = RowSorter.SortDirection.ASC;

}
