package com.wiz3max.simplerest.controller;

import com.wiz3max.simplerest.controller.model.request.EmployeeSalaryRequest;
import com.wiz3max.simplerest.controller.model.response.EmployeeSalaryResponse;
import com.wiz3max.simplerest.service.EmployeeSalaryService;
import com.wiz3max.simplerest.util.RequestExtractorUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * /job_data?requestParam1=xxxx
     * requestParameter:
     *      fields : comma-separated column name to filter data
     *      <field name>[<operator abbr.>] : filter row with field name and condition by operator abbr. If it's not specific[] mean equals(=)
     *      sort : comma-separated column name to sort orderly, the column name in sort parameter need to be subset of column name in fields parameter
     *      sort_type : sorting direction [ASC, DESC]. It needs to have sort request parameter
     *
     * operator abbr:
     *      lt : less than
     *      lte : less than or equals
     *      gt : greater than
     *      gte : greater than or equals
     *      eq : equals
     *      ne : not equals
     *
     *
     * example
     *      /job_data?salary=10000  #filter row that salary equal 10000
     *      /job_data?salary[gte]=10000  #filter row that salary greater than or equal 10000
     *      /job_data?salary=10000&&sort=name,salary&&sort_type=DESC
     *
     * --- response body ---
     * {
     *     errorCode: 000,
     *     errorMsg: "",
     *     header: ["timestamp","salary"],
     *     number_record: 123,
     *     data: [
     *          {"timestamp":"1", "salary": 123456},
     *          {"timestamp":"2", "salary": 789456}
     *     ]
     * }
     *
     *
     * @param requestParameters
     * @return
     */
    @RequestMapping("/job_data")
    public ResponseEntity<EmployeeSalaryResponse> getEmployeeSalary(@RequestParam Map<String, String> requestParameters){
        log.debug(requestParameters.entrySet());

        //TODO: improve value format response to comma-separated to reduce payload size
        //TODO: validate allow field to filter row by criteria  => job title, salary, gender
        //TODO: validate sort field must be in fields
        EmployeeSalaryRequest request = new EmployeeSalaryRequest();
        request.setFilterColumn(requestExtractorUtil.extractAndValidateReservedRequestParameter(FIELDS_REQ_PARAMETER_KEY, requestParameters));
        request.setSortField(requestExtractorUtil.extractAndValidateReservedRequestParameter(SORT_REQ_PARAMETER_KEY, requestParameters));
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
