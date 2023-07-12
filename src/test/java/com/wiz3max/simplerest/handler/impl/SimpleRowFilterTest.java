package com.wiz3max.simplerest.handler.impl;

import com.wiz3max.simplerest.constant.ReqParameterOperator;
import com.wiz3max.simplerest.dto.FieldCriteria;
import com.wiz3max.simplerest.exception.AppException;
import com.wiz3max.simplerest.exception.ErrorCode;
import com.wiz3max.simplerest.exception.IllegalFileFormatException;
import com.wiz3max.simplerest.file.reader.DataReader;
import com.wiz3max.simplerest.handler.RowFilter;
import com.wiz3max.simplerest.metadata.DataType;
import com.wiz3max.simplerest.metadata.Metadata;
import com.wiz3max.simplerest.metadata.MetadataProvider;
import com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.wiz3max.simplerest.metadata.DataType.*;
import static com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl.SchemaType.REQ;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleRowFilterTest {

    @Mock
    private MetadataProvider mockMetadataProvider;

    @InjectMocks
    private static final RowFilter<List<Map<String, Object>>> rowFilter = new SimpleRowFilter();

    private static final Map<String, Metadata> requestMetadataMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Metadata>("Timestamp", new Metadata("Timestamp", TIMESTAMP, "MM/dd/yyyy'T'HH:mm:ss")),
            new AbstractMap.SimpleEntry<String, Metadata>("Employer", new Metadata("Employer", STRING, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Salary", new Metadata("Salary", DECIMAL, null)),
            new AbstractMap.SimpleEntry<String, Metadata>("Gender", new Metadata("Gender", STRING, null)));

    private static final List<Map<String, Object>> inputBeforeFilter;
    static{
        Map<String, Object> row1 = new LinkedHashMap<>();
        row1.put("Timestamp", LocalDateTime.of(2022, 3,30,23, 59,00));
        row1.put("Employer", "aaaaaaaaaa");
        row1.put("Salary", 987_456_123.55);
        row1.put("Gender", "female");

        Map<String, Object> row2 = new LinkedHashMap<>();
        row2.put("Timestamp", LocalDateTime.of(2021, 12,1,12, 31,15));
        row2.put("Employer", null);
        row2.put("Salary", 123_456.5);
        row2.put("Gender", "Male");

        Map<String, Object> row3 = new LinkedHashMap<>();
        row3.put("Timestamp", LocalDateTime.of(2022, 3,31,00, 00,01));
        row3.put("Employer", "azded");
        row3.put("Salary", 680_110.21);
        row3.put("Gender", "unknown");


        inputBeforeFilter = Arrays.asList(row1, row2, row3);
    }

    static Stream<List<Map<String, Object>>> rowProvider() {
        return Stream.of(new ArrayList<>(inputBeforeFilter));
    }

    @BeforeEach
    public void setupEach(){
        lenient().when(mockMetadataProvider.getMetadata(MetadataProviderImpl.SchemaType.REQ)).thenReturn(requestMetadataMap);
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithFieldCriteriaIsNull(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = null;

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(3, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
        assertTrue(filterdData.get(1) == inputBeforeFilter.get(1));
        assertTrue(filterdData.get(2) == inputBeforeFilter.get(2));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithFieldCriteriaIsEmpty(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = new ArrayList<>();

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(3, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
        assertTrue(filterdData.get(1) == inputBeforeFilter.get(1));
        assertTrue(filterdData.get(2) == inputBeforeFilter.get(2));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowSkipCriteriaWhenCriteriaValueIsNull(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(new FieldCriteria("Salary", ReqParameterOperator.gt, null));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(3, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
        assertTrue(filterdData.get(1) == inputBeforeFilter.get(1));
        assertTrue(filterdData.get(2) == inputBeforeFilter.get(2));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowSkipCriteriaWhenSomeCriteriaValueIsNull(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(
                new FieldCriteria("Employer", ReqParameterOperator.lt, "b")
                , new FieldCriteria("Salary", ReqParameterOperator.eq, null)
                , new FieldCriteria("Salary", ReqParameterOperator.gt, 700_000.99));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(1, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithTimestampDataType(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(
                new FieldCriteria("Timestamp", ReqParameterOperator.lte, LocalDateTime.of(2022, 3,30,23, 59,00)));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(2, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
        assertTrue(filterdData.get(1) == inputBeforeFilter.get(1));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithStringDataType(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(
                new FieldCriteria("Gender", ReqParameterOperator.ne, "female"));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(2, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(1));
        assertTrue(filterdData.get(1) == inputBeforeFilter.get(2));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithDecimalDataType(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(
                new FieldCriteria("Salary", ReqParameterOperator.gte, 200_000.1));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(2, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
        assertTrue(filterdData.get(1) == inputBeforeFilter.get(2));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithMultipleCriteriasOnTheSameFieldName(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(
                new FieldCriteria("Salary", ReqParameterOperator.gte, 100_000.81)
                , new FieldCriteria("Salary", ReqParameterOperator.lte, 200_000.55));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(1, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(1));
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithMultipleCriteriasOnTheSameFieldName_EmptyResult(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(
                new FieldCriteria("Salary", ReqParameterOperator.lte, 100_001.11)
                , new FieldCriteria("Salary", ReqParameterOperator.gte, 200_020.09));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(0, filterdData.size());
    }

    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithFieldCriteriaValueRowIsNull(List<Map<String, Object>> data) {
        //input
        List<FieldCriteria> fieldCriterias = Arrays.asList(
                new FieldCriteria("Employer", ReqParameterOperator.gte, "a"));

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(2, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
        assertTrue(filterdData.get(1) == inputBeforeFilter.get(2));
    }

    //this unit test for detecting new DataType in enum without type handling implementation in this class
    @ParameterizedTest
    @MethodSource("rowProvider")
    void filterRowWithAllDataType(List<Map<String, Object>> data) {
        //USE ALL enum.values
        //input
        List<FieldCriteria> fieldCriterias = generateAllDataTypeInFieldCriteria();

        //execute
        List<Map<String, Object>> filterdData = rowFilter.filterRow(data, fieldCriterias);

        //assert
        assertEquals(1, filterdData.size());
        assertTrue(filterdData.get(0) == inputBeforeFilter.get(0));
    }

    //Intention to
    private List<FieldCriteria> generateAllDataTypeInFieldCriteria(){
        List<FieldCriteria> fieldCriterias = new ArrayList<>();
        DataType[] dataTypes = DataType.values();
        for (DataType dataType : dataTypes) {
            fieldCriterias.add(switch (dataType) {
                case TIMESTAMP -> new FieldCriteria(DataReader.TIMESTAMP_FIELD_NAME, ReqParameterOperator.eq, LocalDateTime.of(2022, 3, 30, 23, 59, 00));
                case STRING -> new FieldCriteria("Gender", ReqParameterOperator.ne, "TEST");
                case DECIMAL -> new FieldCriteria("Salary", ReqParameterOperator.gt, 1.0);
                default -> new FieldCriteria("FoundUnknownDataType", ReqParameterOperator.ne, new Object());
            });
        }
        return fieldCriterias;
    }
}