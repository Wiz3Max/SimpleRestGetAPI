package com.wiz3max.simplerest.handler;

import java.util.List;

public interface FieldFilter<T> {
    T filter(T input, List<String> selFields);
}
