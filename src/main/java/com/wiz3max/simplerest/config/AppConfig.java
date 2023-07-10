package com.wiz3max.simplerest.config;

import com.wiz3max.simplerest.file.reader.CsvFileReader;
import com.wiz3max.simplerest.file.reader.JsonFileReader;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl.SchemaType.CSV;
import static com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl.SchemaType.JSON;

@Configuration
public class AppConfig {

    @Value("${employeesalary.csv.filepath}")
    private String employeeSalaryCsvFilePath;

    @Value("${employeesalary.json.filepath}")
    private String employeeSalaryJsonFilePath;

    @Autowired
    private MetadataProvider metadataProvider;

    @Bean
    public CsvFileReader csvFileReader(){
        return new CsvFileReader(employeeSalaryCsvFilePath, metadataProvider.getMetadata(CSV));
    }

    @Bean
    public JsonFileReader jsonFileReader(){
        return new JsonFileReader(employeeSalaryJsonFilePath, metadataProvider.getMetadata(JSON));
    }

}
