package com.wiz3max.simplerest.handler;

import com.wiz3max.simplerest.dto.FieldCriteria;

import java.util.List;

public interface RowFilter<T> {

    T filterRow(T input, List<FieldCriteria> fieldCriterias);
}
