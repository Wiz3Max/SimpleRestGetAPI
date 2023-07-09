package com.wiz3max.simplerest.handler.impl;

import com.wiz3max.simplerest.dto.FieldCriteria;
import com.wiz3max.simplerest.exception.ErrorCode;
import com.wiz3max.simplerest.exception.IllegalFileFormatException;
import com.wiz3max.simplerest.handler.RowFilter;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SimpleRowFilter implements RowFilter<List<Map<String, Object>>> {

    @Autowired
    private MetadataProvider metadataProvider;

    @Override
    public List<Map<String, Object>> filterRow(List<Map<String, Object>> input, Map<String, FieldCriteria> fieldCriteriaMap) {
        if (fieldCriteriaMap == null || fieldCriteriaMap.size() <= 0){
            return input;
        }

        ROW_LOOP:
        for (Iterator<Map<String, Object>> it = input.iterator(); it.hasNext(); ){
            Map<String, Object> row = it.next();
            for (Map.Entry<String, FieldCriteria> criteriaEntry : fieldCriteriaMap.entrySet()){
                FieldCriteria fieldCriteria = criteriaEntry.getValue();
                String fieldName = fieldCriteria.getFieldName();

                if(Objects.isNull(row.get(fieldName))){
                    it.remove();
                    continue ROW_LOOP;
                }

                switch (metadataProvider.getMetadata().get(fieldName).getType()){
                    case TIMESTAMP -> {
                        LocalDateTime firstOperand = (LocalDateTime) row.get(fieldName);
                        if(!fieldCriteria.getOperator().getOperatorFunction().test(firstOperand, fieldCriteria.getSecondOperand())){
                            it.remove();
                            continue ROW_LOOP;
                        }
                    }
                    case STRING -> {
                        String firstOperand = (String) row.get(fieldName);
                        if(!fieldCriteria.getOperator().getOperatorFunction().test(firstOperand, fieldCriteria.getSecondOperand())){
                            it.remove();
                            continue ROW_LOOP;
                        }
                    }
                    case DECIMAL -> {
                        Double firstOperand = (Double) row.get(fieldName);
                        if(!fieldCriteria.getOperator().getOperatorFunction().test(firstOperand, fieldCriteria.getSecondOperand())){
                            it.remove();
                            continue ROW_LOOP;
                        }}
                    default -> {
                        throw new IllegalFileFormatException("TYPE not found", ErrorCode.DATA_TYPE_NOT_FOUND);
                    }
                }
            }
        }
        return input;
    }
}
