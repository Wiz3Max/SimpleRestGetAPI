package com.wiz3max.simplerest.service.impl;

import com.wiz3max.simplerest.controller.model.request.EmployeeSalaryRequest;
import com.wiz3max.simplerest.dao.EmployeeSalaryDao;
import com.wiz3max.simplerest.handler.FieldFilter;
import com.wiz3max.simplerest.handler.RowFilter;
import com.wiz3max.simplerest.handler.RowSorter;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import com.wiz3max.simplerest.service.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl.SchemaType.REQ;

@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private FieldFilter<List<Map<String, Object>>> fieldFilter;

    @Autowired
    private RowFilter<List<Map<String, Object>>> rowFilter;

    @Autowired
    private RowSorter<List<Map<String, Object>>, List<String>> rowSorter;

    @Autowired
    private EmployeeSalaryDao employeeSalaryDao;

    @Override
    public List<Map<String, Object>> getEmployeeSalary(EmployeeSalaryRequest request){
        //get data
        List<Map<String, Object>> employeeSalaryMaps = employeeSalaryDao.queryEmployeeSalary();

        //filter by condition
        employeeSalaryMaps = rowFilter.filterRow(employeeSalaryMaps, request.getFilterRowCriterias());

        //filter by column
        employeeSalaryMaps = fieldFilter.filter(employeeSalaryMaps, request.getFilterColumn());

        //sort by field
        rowSorter.sort(employeeSalaryMaps, request.getSortField(), request.getSortMode(), metadataProvider.getMetadata(REQ));

        return employeeSalaryMaps;
    }

}
