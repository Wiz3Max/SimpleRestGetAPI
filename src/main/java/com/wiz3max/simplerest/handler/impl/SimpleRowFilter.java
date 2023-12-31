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

import static com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl.SchemaType.REQ;

@Component
public class SimpleRowFilter implements RowFilter<List<Map<String, Object>>> {

    @Autowired
    private MetadataProvider metadataProvider;

    @Override
    public List<Map<String, Object>> filterRow(List<Map<String, Object>> input, List<FieldCriteria> fieldCriterias) {
        if (fieldCriterias == null || fieldCriterias.size() <= 0){
            return input;
        }

        ROW_LOOP:
        for (Iterator<Map<String, Object>> it = input.iterator(); it.hasNext(); ){
            Map<String, Object> row = it.next();

            boolean matchCriteria = true;
            for (FieldCriteria criteria : fieldCriterias){
                if(!matchCriteria){
                    break;
                }
                if(Objects.isNull(criteria.getSecondOperand())){
                    continue;
                }
                String fieldName = criteria.getFieldName();

                if(Objects.isNull(row.get(fieldName))){
                    it.remove();
                    continue ROW_LOOP;
                }

                switch (metadataProvider.getMetadata(REQ).get(fieldName).getType()){
                    case TIMESTAMP -> {
                        LocalDateTime firstOperand = (LocalDateTime) row.get(fieldName);
                        if(!criteria.getOperator().getOperatorFunction().test(firstOperand, criteria.getSecondOperand())){
                            matchCriteria&=false;
                        }
                    }
                    case STRING -> {
                        String firstOperand = (String) row.get(fieldName);
                        if(!criteria.getOperator().getOperatorFunction().test(firstOperand, criteria.getSecondOperand())){
                            matchCriteria&=false;
                        }
                    }
                    case DECIMAL -> {
                        Double firstOperand = (Double) row.get(fieldName);
                        if(!criteria.getOperator().getOperatorFunction().test(firstOperand, criteria.getSecondOperand())){
                            matchCriteria&=false;
                        }}
                    default -> {
                        throw new IllegalFileFormatException("TYPE not found", ErrorCode.DATA_TYPE_NOT_FOUND);
                    }
                }
            }
            if(!matchCriteria){
                it.remove();
            }
        }
        return input;
    }
}
