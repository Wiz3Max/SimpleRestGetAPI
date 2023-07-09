package com.wiz3max.simplerest.config;

import com.wiz3max.simplerest.file.reader.CsvFileReader;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${employeesalary.csv.filepath}")
    private String employeeSalaryCsvFilePath;

    @Autowired
    private MetadataProvider metadataProvider;

    @Bean
    public CsvFileReader csvFileReader(){
        return new CsvFileReader(employeeSalaryCsvFilePath, metadataProvider.getMetadata());
    }

}
