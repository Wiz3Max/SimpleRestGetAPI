package com.wiz3max.simplerest.util;

import com.wiz3max.simplerest.constant.ReqParameterOperator;
import com.wiz3max.simplerest.dto.FieldCriteria;
import com.wiz3max.simplerest.exception.ErrorCode;
import com.wiz3max.simplerest.exception.IllegalFileFormatException;
import com.wiz3max.simplerest.exception.InvalidRequestException;
import com.wiz3max.simplerest.handler.RowSorter;
import com.wiz3max.simplerest.metadata.Metadata;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import com.wiz3max.simplerest.constant.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class RequestExtractorUtil {

    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.STRICT);;

    @Autowired
    private MetadataProvider metadataProvider;

    @Autowired
    private RequestValidatorUtil requestValidatorUtil;

    public RowSorter.SortDirection valueOfSortDirection(String sortDirectionStr){
        if(!StringUtils.hasText(sortDirectionStr)){
            return RowSorter.SortDirection.ASC;
        }

        RowSorter.SortDirection sortDirection;
        try{
            sortDirection = RowSorter.SortDirection.valueOfIgnoreCase(sortDirectionStr);
        } catch (IllegalArgumentException e){
            throw new InvalidRequestException("Invalid sort_type : " + sortDirectionStr, ErrorCode.INVALID_SORT_TYPE, e);
        }

        return sortDirection;
    }

    public List<String> extractAndValidateReservedRequestParameter(String reservedRequestParameterKey, Map<String, String> requestParameter){
        for(Map.Entry<String, String> entry : requestParameter.entrySet()) {
            String requestParameterKey = entry.getKey();
            String requestParameterValue = entry.getValue();

            if (!reservedRequestParameterKey.equalsIgnoreCase(requestParameterKey)) {
                continue;
            }

            if (!StringUtils.hasText(requestParameterValue)) return null;

            List<String> extractedValues = splitRequestParameterValue(requestParameterValue);
            requestValidatorUtil.validateValueFieldNames(extractedValues, reservedRequestParameterKey);
            return extractedValues;
        }
        return null;
    }


    public Map<String, FieldCriteria> extractAndValidateFieldCriteria(Map<String, String> requestParameter){
        Map<String, FieldCriteria> fieldCriteriaMap = new HashMap<>();
        for(Map.Entry<String, String> entry : requestParameter.entrySet()){
            String requestParameterKey = entry.getKey();
            String requestParameterValue = entry.getValue();
            if(AppConstant.FIELDS_REQ_PARAMETER_KEY.equalsIgnoreCase(requestParameterKey) ||
                    AppConstant.SORT_REQ_PARAMETER_KEY.equalsIgnoreCase(requestParameterKey) ||
                    AppConstant.SORT_TYPE_REQ_PARAMETER_KEY.equalsIgnoreCase(requestParameterKey)){
                continue;
            }
            FieldCriteria fieldCriteria = parseFieldCriteria(requestParameterKey, requestParameterValue);
            fieldCriteriaMap.put(fieldCriteria.getFieldName(), fieldCriteria);
        }
        return fieldCriteriaMap;
    }

    public FieldCriteria parseFieldCriteria(String requestParameterCriteriaKey, String requestParameterCriteriaValue){
        String criteriaField = requestParameterCriteriaKey;
        String operatorStr = ReqParameterOperator.DEFAULT.name();

        int bracketStartIndex = requestParameterCriteriaKey.indexOf(AppConstant.FIELD_CRITERIA_OPERATOR_OPEN_BRACKET);
        if(bracketStartIndex>= 0){
            int bracketEndIndex = requestParameterCriteriaKey.indexOf(AppConstant.FIELD_CRITERIA_OPERATOR_CLOSE_BRACKET);
            if(bracketEndIndex < 2 || bracketEndIndex < requestParameterCriteriaKey.length()-1){
                throw new InvalidRequestException("Invalid Criteria field : " + requestParameterCriteriaKey, ErrorCode.INVALID_CRITERIA_FIELD_SYNTAX);
            }

            criteriaField = requestParameterCriteriaKey.substring(0, bracketStartIndex);
            operatorStr = requestParameterCriteriaKey.substring(bracketStartIndex+1, bracketEndIndex);
        }

        requestValidatorUtil.validateFieldName(criteriaField, requestParameterCriteriaKey);
        ReqParameterOperator operator;
        try{
            operator = ReqParameterOperator.valueOf(operatorStr);
        } catch (IllegalArgumentException e){
            throw new InvalidRequestException("Criteria [" + operatorStr + "] of field " + criteriaField + "doesn't support", ErrorCode.INVALID_CRITERIA_OPERATOR, e);
        }

        Object parsedValue = this.parseValue(criteriaField, requestParameterCriteriaValue, DEFAULT_DATETIME_FORMATTER);
        return new FieldCriteria(criteriaField, operator, parsedValue);
    }

    public List<String> splitRequestParameterValue(String requestParameterValue){
        return StringUtils.commaDelimitedListToSet(requestParameterValue).stream().toList();
    }

    /**
     * parse Value according to metadata provider
     * @param columnName
     * @param value
     * @return
     */
    public Object parseValue(String columnName, String value){
        return parseValue(columnName, value, null);
    }

    /**
     * parse Value according to metadata provider with specific DateTimeFormatter for DataType.TIMESTAMP
     * @param columnName
     * @param value
     * @param dateTimeFormatter specific to override dateTimeFormatter of MetadataProvider
     * @return
     */
    public Object parseValue(String columnName, String value, DateTimeFormatter dateTimeFormatter){
        if(!StringUtils.hasText(value)){
            return null;
        }
        Metadata columnMetadata = metadataProvider.getMetadata().get(columnName);
        return switch (columnMetadata.getType()){
            case TIMESTAMP -> LocalDateTime.parse(value, dateTimeFormatter == null ? columnMetadata.getDateTimeFormatter() : dateTimeFormatter);
            case DECIMAL -> {
                try {
                    yield NumberFormat.getNumberInstance(Locale.US).parse(value).doubleValue();
                } catch (ParseException e) {
                    throw new IllegalFileFormatException("Cannot parse " + value + "of field " + columnName, ErrorCode.FILE_ILLEGAL_FORMAT, e);
                }
            }
            case STRING -> String.valueOf(value);
            default -> throw new IllegalFileFormatException("TYPE not found", ErrorCode.DATA_TYPE_NOT_FOUND);
        };
    }
}
