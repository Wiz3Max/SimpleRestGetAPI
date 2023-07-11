package com.wiz3max.simplerest.controller;

import com.wiz3max.simplerest.controller.model.request.EmployeeSalaryRequest;
import com.wiz3max.simplerest.controller.model.response.EmployeeSalaryResponse;
import com.wiz3max.simplerest.service.EmployeeSalaryService;
import com.wiz3max.simplerest.util.RequestExtractorUtil;
import com.wiz3max.simplerest.util.RequestValidatorUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.wiz3max.simplerest.constant.AppConstant.*;

@RestController
@RequestMapping("/")
@CommonsLog
public class SimpleController {

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Autowired
    private RequestExtractorUtil requestExtractorUtil;

    @Autowired
    private RequestValidatorUtil requestValidatorUtil;


    @RequestMapping("/job_data")
    public ResponseEntity<EmployeeSalaryResponse> getEmployeeSalary(@RequestParam Map<String, String> requestParameters){
        log.debug(requestParameters.entrySet());

        //TODO: UNIT TEST
        //TODO: improve value format response to comma-separated to reduce payload size
        //init request
        EmployeeSalaryRequest request = new EmployeeSalaryRequest();
        request.setFilterColumn(requestExtractorUtil.extractAndValidateReservedRequestParameter(FIELDS_REQ_PARAMETER_KEY, requestParameters));
        request.setSortField(requestExtractorUtil.extractAndValidateReservedRequestParameter(SORT_REQ_PARAMETER_KEY, requestParameters));
        requestValidatorUtil.validateFieldNamesMustBeSubsetInList(request.getSortField(), request.getFilterColumn(), FIELDS_REQ_PARAMETER_KEY);
        request.setSortMode(requestExtractorUtil.valueOfSortDirection(requestParameters.get(SORT_TYPE_REQ_PARAMETER_KEY)));
        request.setFilterRowCriterias(requestExtractorUtil.extractAndValidateFieldCriteria(requestParameters));


        EmployeeSalaryResponse employeeSalaryResponse = new EmployeeSalaryResponse();
        //delegate to service
        List<Map<String, Object>> data = employeeSalaryService.getEmployeeSalary(request);
        employeeSalaryResponse.setData(data);
        employeeSalaryResponse.setNumberRecord(data.size());

        return data == null || data.size() <= 0 ? ResponseEntity.noContent().build() : ResponseEntity.ok(employeeSalaryResponse);
    }

}
