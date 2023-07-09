package com.wiz3max.simplerest.dao.impl;

import com.wiz3max.simplerest.dao.EmployeeSalaryDao;
import com.wiz3max.simplerest.cache.FileCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeSalaryRepository implements EmployeeSalaryDao {

    @Autowired
    private FileCache fileCache;

    @Override
    public List<Map<String, Object>> queryEmployeeSalary(){
        List<Map<String, Object>> cacheData = fileCache.readEmployeeSalary();
        List<Map<String, Object>> copyData = new ArrayList<>(cacheData.size());
        for (Map<String, Object> row: cacheData) {
            Map<String, Object> copyRow = new LinkedHashMap<>((int)Math.ceil(row.size()/0.75));
            copyRow.putAll(row);
            copyData.add(copyRow);
        }
        return copyData;
    }

}
