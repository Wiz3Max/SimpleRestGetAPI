package com.wiz3max.simplerest.cache;

import com.wiz3max.simplerest.file.reader.CsvFileReader;
import com.wiz3max.simplerest.file.reader.JsonFileReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wiz3max.simplerest.constant.AppConstant.INITIAL_FILE_DATA_CAPACITY;

@Component
public class SimpleFileCache implements FileCache {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CsvFileReader employeeSalaryCsvFileReader;

    @Autowired
    private JsonFileReader employeeSalaryJsonFileReader;

    @PostConstruct
    public void initCache() throws IOException {
        loadStaticFileToCache();
    }

    private void loadStaticFileToCache() throws IOException {
        cacheManager.getCache("employeeSalary").put(SimpleKey.EMPTY, readEmployeeSalary());
    }

    private List<Map<String, Object>> loadDatafromCsv() {
        return employeeSalaryCsvFileReader.parseFile();
    }

    private List<Map<String, Object>> loadDataFromJson(){
        return employeeSalaryJsonFileReader.parseFile();
    }

    @Override
    @Cacheable(cacheNames = "employeeSalary")
    public List<Map<String, Object>> readEmployeeSalary() {
        List<Map<String, Object>> data = new ArrayList<>(INITIAL_FILE_DATA_CAPACITY);
        data.addAll(loadDatafromCsv());
        data.addAll(loadDataFromJson());
        return data;
    }
}
