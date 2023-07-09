package com.wiz3max.simplerest.dao.impl;

import com.wiz3max.simplerest.dao.EmployeeSalaryDao;
import com.wiz3max.simplerest.dao.FileCache;
import com.wiz3max.simplerest.file.reader.CsvFileReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.wiz3max.simplerest.constant.AppConstant.INITIAL_FILE_DATA_CAPACITY;

@Repository
public class EmployeeSalaryRepository implements EmployeeSalaryDao {

    @Autowired
    private FileCache fileCache;

    @Override
    public List<Map<String, Object>> queryEmployeeSalary(){
        List<Map<String, Object>> cacheData = fileCache.readEmployeeSalary();
        List<Map<String, Object>> copyData = new ArrayList<>(cacheData.size());
        for (Map<String, Object> row: cacheData) {
            Map<String, Object> copyRow = new LinkedHashMap<>();
            copyRow.putAll(row);
            copyData.add(copyRow);
        }
        return copyData;
    }

}
