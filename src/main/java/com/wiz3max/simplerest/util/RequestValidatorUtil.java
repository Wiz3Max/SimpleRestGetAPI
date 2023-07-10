package com.wiz3max.simplerest.util;

import com.wiz3max.simplerest.exception.InvalidRequestException;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wiz3max.simplerest.exception.ErrorCode.INVALID_FIELD_NAME;
import static com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl.SchemaType.REQ;

@Component
public class RequestValidatorUtil {

    @Autowired
    private MetadataProvider metadataProvider;

    public void validateFieldNamesMustBeSubsetInList(List<String> fieldsToValidate, List<String> setOfFields, String setOfFieldsReqParamKey) {
        if(fieldsToValidate == null || fieldsToValidate.size() <= 0){
            return;
        }
        if((setOfFields == null || fieldsToValidate.size() <= 0)){
            throw new InvalidRequestException(fieldsToValidate + " fields must be present in " + setOfFieldsReqParamKey, INVALID_FIELD_NAME);
        }

        for(String field : fieldsToValidate){
            if(!setOfFields.contains(field)){
                throw new InvalidRequestException(field + " field must be present in " + setOfFieldsReqParamKey, INVALID_FIELD_NAME);
            }
        }
    }

    public void validateFieldNameWithMetadata(String field, String requestParameterKey){
        if(!metadataProvider.getMetadata(REQ).containsKey(field)){
            throw new InvalidRequestException(field + " field in " + requestParameterKey + " is invalid", INVALID_FIELD_NAME);
        }
    }

    public void validateValueFieldNames(List<String> fields, String requestParameterKey){
        for (String field : fields) {
            validateFieldNameWithMetadata(field, requestParameterKey);
        }
    }


}
