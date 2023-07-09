package com.wiz3max.simplerest.metadata;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Metadata {

    public static final String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm-ssz";

    private final String sourceField;
    private final DataType type;
    private final String datePattern;

    private final DateTimeFormatter dateTimeFormatter;

    public Metadata(String sourceField, DataType type, String datePattern){
        this.sourceField = sourceField;
        this.type = type;
        this.datePattern = datePattern;
        this.dateTimeFormatter = DateTimeFormatter
                .ofPattern(this.datePattern != null ? this.datePattern : DEFAULT_TIMESTAMP_PATTERN)
                .withResolverStyle(ResolverStyle.SMART);
    }

    public String getSourceField() {
        return sourceField;
    }

    public DataType getType() {
        return type;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

}
