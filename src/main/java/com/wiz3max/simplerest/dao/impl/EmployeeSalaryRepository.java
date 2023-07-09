package com.wiz3max.simplerest.dao.impl;

import com.wiz3max.simplerest.dao.EmployeeSalaryDao;
import com.wiz3max.simplerest.file.reader.CsvFileReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.wiz3max.simplerest.constant.AppConstant.INITIAL_FILE_DATA_CAPACITY;

@Repository
public class EmployeeSalaryRepository implements EmployeeSalaryDao {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CsvFileReader employeeSalaryCsvFileReader;

    @PostConstruct
    public void initCache() throws IOException {
        loadStaticFileToCache();
    }

    //TODO: need to test cache not effect from manipulate after get data
    @Override
    @Cacheable(cacheNames = "employeeSalary")
    public List<Map<String, Object>> queryEmployeeSalary(){
        return readEmployeeSalary();
    }

    private List<Map<String, Object>> loadDatafromCsv() {
        return employeeSalaryCsvFileReader.parseFile();
    }

//    private List<Map<String, Object>> loadDataFromJson(){
//        return null;
//    }

    private void loadStaticFileToCache() throws IOException {
        cacheManager.getCache("employeeSalary").put(SimpleKey.EMPTY, readEmployeeSalary());
    }

    private List<Map<String, Object>> readEmployeeSalary() {
        List<Map<String, Object>> data = new ArrayList<>(INITIAL_FILE_DATA_CAPACITY);
        data.addAll(loadDatafromCsv());
        //TODO: implement
//        data.addAll(loadDataFromJson());
        return data;
    }

}
