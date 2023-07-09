package com.wiz3max.simplerest.dto;

import com.wiz3max.simplerest.constant.ReqParameterOperator;
import lombok.Data;

@Data
public class FieldCriteria {

    private final String fieldName;
    private final ReqParameterOperator operator;
    private final Object secondOperand;

    public <T> T getSecondOperand(){
        return (T) this.secondOperand;
    }

}
