package com.wiz3max.simplerest.file.reader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataReader {

    String TIMESTAMP_FIELD_NAME = "Timestamp";

    List<Map<String, Object>> parseFile() throws IOException;
}
