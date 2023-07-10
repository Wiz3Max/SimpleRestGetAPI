package com.wiz3max.simplerest.file.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiz3max.simplerest.exception.ErrorCode;
import com.wiz3max.simplerest.exception.FileException;
import com.wiz3max.simplerest.exception.IllegalFileFormatException;
import com.wiz3max.simplerest.metadata.Metadata;
import com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl;
import com.wiz3max.simplerest.util.RequestExtractorUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@CommonsLog
public class JsonFileReader implements DataReader{

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String path;
    private final Map<String, Metadata> metadataMap;

    public JsonFileReader(String path, Map<String, Metadata> metadataMap){
        this.path = path;
        this.metadataMap = metadataMap;
    }

    @Autowired
    private RequestExtractorUtil requestExtractorUtil;

    @Override
    public List<Map<String, Object>> parseFile() {
        log.info("Start reading json file");

        List<Map<String, String>> rawDataMap = null;
        try {
            rawDataMap = objectMapper.readValue(new BufferedInputStream(new FileInputStream(path)), new TypeReference<>() {});
            List<Map<String, Object>> parsedDataMap = rawDataMap.stream()
                    .filter(this::isValidRow)
                    .map(this::parseLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return parsedDataMap;
        } catch (IOException e) {
            throw new FileException("Cannot Open File or read Json file", ErrorCode.FILE_CANNOT_OPEN, e);
        }
    }

    private boolean isValidRow(Map<String, String> rawRowMap){
        boolean isValidRow = false;
        for (String value : rawRowMap.values()) {
            if(StringUtils.hasText(value)){
                isValidRow = true;
                break;
            }
        }
        return isValidRow;
    }

    private Map<String, Object> parseLine(Map<String, String> rawRowMap) throws IllegalFileFormatException {
        Map<String, Object> parsedRowMap = new LinkedHashMap<>();

        for(Map.Entry<String, String> entry: rawRowMap.entrySet()){
            String columnName = entry.getKey();
            String value = entry.getValue();

            if(!metadataMap.containsKey(columnName)){
                throw new IllegalFileFormatException("Column not found", ErrorCode.FILE_ILLEGAL_FORMAT);
            }

            try{
                Object parsedValue = requestExtractorUtil.parseValue(columnName, value, MetadataProviderImpl.SchemaType.JSON);
                parsedRowMap.put(columnName, parsedValue);
            } catch (Throwable e){
                log.warn("Skip row due to parsed error Line: " + rawRowMap.get(TIMESTAMP_FIELD_NAME) + " ,column " + columnName + " : " + value, e);
                return null;
            }
        }

        return parsedRowMap;
    }
}
