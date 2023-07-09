package com.wiz3max.simplerest.handler.impl;

import com.wiz3max.simplerest.handler.FieldFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class SimpleFieldFilter implements FieldFilter<List<Map<String, Object>>> {

    @Override
    public List<Map<String, Object>> filter(List<Map<String, Object>> input, List<String> selFields) {
        if(selFields == null || selFields.size() <= 0){
            return input;
        }

        List<Map<String, Object>> dataMaps = new ArrayList<>();
        for (Map<String, Object> row : input) {
            Map<String, Object> dataMap = new LinkedHashMap<>();
            for (String selectedField : selFields) {
                dataMap.put(selectedField, row.get(selectedField));
            }
            dataMaps.add(dataMap);
        }

        return dataMaps;
    }
}
