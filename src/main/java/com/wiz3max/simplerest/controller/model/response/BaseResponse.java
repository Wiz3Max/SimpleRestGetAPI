package com.wiz3max.simplerest.controller.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BaseResponse {
    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorMsg")
    private String errorMsg;

    @JsonProperty("number_record")
    private int numberRecord;
}
