package com.wiz3max.simplerest.metadata.impl;

import com.wiz3max.simplerest.metadata.DataType;
import com.wiz3max.simplerest.metadata.Metadata;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Component
public class MetadataProviderImpl implements MetadataProvider {

    public enum SchemaType {
        CSV,
        JSON,
        REQ
    }

    private static Map<SchemaType, Map<String, Metadata>> internalFileSchemaTypeMap = new HashMap<>();

    private static final Map<String, Metadata> requestMetadataMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Metadata>("Timestamp", new Metadata("Timestamp", DataType.TIMESTAMP, "MM/dd/yyyy'T'HH:mm:ss")),
            new AbstractMap.SimpleEntry<String, Metadata>("Employer", new Metadata("Employer", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Location", new Metadata("Location", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Job Title", new Metadata("Job Title", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Years at Employer", new Metadata("Years at Employer", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Years of Experience", new Metadata("Years of Experience", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Salary", new Metadata("Salary", DataType.DECIMAL, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Signing Bonus", new Metadata("Signing Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Annual Bonus", new Metadata("Annual Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Annual Stock Value/Bonus", new Metadata("Annual Stock Value/Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Gender", new Metadata("Gender", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Additional Comments",new Metadata("Additional Comments", DataType.STRING, null)));

    //TODO: improve to support many metadatas and read from file
    private static final Map<String, Metadata> csvMetadataMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Metadata>("Timestamp", new Metadata("Timestamp", DataType.TIMESTAMP, "M/d/u H:m")),
            new AbstractMap.SimpleEntry<String, Metadata>("Employer", new Metadata("Employer", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Location", new Metadata("Location", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Job Title", new Metadata("Job Title", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Years at Employer", new Metadata("Years at Employer", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Years of Experience", new Metadata("Years of Experience", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Salary", new Metadata("Salary", DataType.DECIMAL, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Signing Bonus", new Metadata("Signing Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Annual Bonus", new Metadata("Annual Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Annual Stock Value/Bonus", new Metadata("Annual Stock Value/Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Gender", new Metadata("Gender", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Additional Comments",new Metadata("Additional Comments", DataType.STRING, null)));

    //TODO: improve to support many metadatas and read from file
    private static final Map<String, Metadata> jsonMetadataMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Metadata>("Timestamp", new Metadata("Timestamp", DataType.TIMESTAMP, "M/d/u H:mm:ss")),
            new AbstractMap.SimpleEntry<String, Metadata>("Employer", new Metadata("Employer", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Location", new Metadata("Location", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Job Title", new Metadata("Job Title", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Years at Employer", new Metadata("Years at Employer", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Years of Experience", new Metadata("Years of Experience", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Salary", new Metadata("Salary", DataType.DECIMAL, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Signing Bonus", new Metadata("Signing Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Annual Bonus", new Metadata("Annual Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Annual Stock Value/Bonus", new Metadata("Annual Stock Value/Bonus", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Gender", new Metadata("Gender", DataType.STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Additional Comments",new Metadata("Additional Comments", DataType.STRING, null)));

    static{
        internalFileSchemaTypeMap.put(SchemaType.CSV, csvMetadataMap);
        internalFileSchemaTypeMap.put(SchemaType.JSON, jsonMetadataMap);
        internalFileSchemaTypeMap.put(SchemaType.REQ, requestMetadataMap);
    }

    @Override
    public Map<String, Metadata> getMetadata(SchemaType schemaType){
        return internalFileSchemaTypeMap.get(schemaType);
    }
}
