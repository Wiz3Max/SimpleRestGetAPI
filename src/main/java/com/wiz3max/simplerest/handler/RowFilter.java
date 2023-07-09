package com.wiz3max.simplerest.handler;

import com.wiz3max.simplerest.dto.FieldCriteria;

import java.util.Map;

public interface RowFilter<T> {

    T filterRow(T input, Map<String, FieldCriteria> fieldCriteriaMap);
}
