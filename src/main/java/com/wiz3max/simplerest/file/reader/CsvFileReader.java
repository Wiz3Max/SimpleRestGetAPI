package com.wiz3max.simplerest.file.reader;

import com.wiz3max.simplerest.exception.ErrorCode;
import com.wiz3max.simplerest.exception.FileException;
import com.wiz3max.simplerest.exception.IllegalFileFormatException;
import com.wiz3max.simplerest.metadata.Metadata;
import com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl;
import com.wiz3max.simplerest.util.RequestExtractorUtil;
import de.siegmar.fastcsv.reader.NamedCsvReader;
import de.siegmar.fastcsv.reader.NamedCsvRow;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@CommonsLog
public class CsvFileReader implements DataReader {

    public static final char DEFAULT_FIELD_SEPARATOR = ',';
    public static final char DEFAULT_QUOTE_SEPARATOR = '"';

    private final char fieldSeparator;
    private final char quoteCharacter;
    private final String path;
    private final Map<String, Metadata> metadataMap;

    @Autowired
    private RequestExtractorUtil requestExtractorUtil;

    public CsvFileReader(String path, Map<String, Metadata> metadataMap){
        this(path, metadataMap, DEFAULT_FIELD_SEPARATOR, DEFAULT_QUOTE_SEPARATOR);
    }

    public CsvFileReader(String path, Map<String, Metadata> metadataMap, char fieldSeparator, char quoteCharacter){
        this.path = path;
        this.metadataMap = metadataMap;
        this.fieldSeparator = fieldSeparator;
        this.quoteCharacter = quoteCharacter;
    }

    @Override
    public List<Map<String, Object>> parseFile() {
        log.info("Start reading csv file");

        try (NamedCsvReader csv = NamedCsvReader.builder()
                .fieldSeparator(this.fieldSeparator)
                .quoteCharacter(this.quoteCharacter)
                .build(Paths.get(this.path))) {
            return csv.stream()
                    .map(NamedCsvRow::getFields)
                    .filter(this::isValidRow)
                    .map(this::parseLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new FileException("Cannot Open File", ErrorCode.FILE_CANNOT_OPEN, e);
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
                Object parsedValue = requestExtractorUtil.parseValue(columnName, value, MetadataProviderImpl.SchemaType.CSV);
                parsedRowMap.put(columnName, parsedValue);
            } catch (Throwable e){
                log.warn("Skip row due to parsed error Line: " + rawRowMap.get(TIMESTAMP_FIELD_NAME) + " ,column " + columnName + " : " + value, e);
                return null;
            }
        }

        return parsedRowMap;
    }
}
