package com.wiz3max.simplerest.util;

import com.wiz3max.simplerest.exception.InvalidRequestException;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wiz3max.simplerest.exception.ErrorCode.INVALID_FIELD_NAME;

@Component
public class RequestValidatorUtil {

    @Autowired
    private MetadataProvider metadataProvider;

    public void validateFieldName(String field, String requestParameterKey){
        if(!metadataProvider.getMetadata().containsKey(field)){
            throw new InvalidRequestException(field + " field in " + requestParameterKey + " is invalid", INVALID_FIELD_NAME);
        }
    }

    public void validateValueFieldNames(List<String> fields, String requestParameterKey){
        for (String field : fields) {
            validateFieldName(field, requestParameterKey);
        }
    }
}
