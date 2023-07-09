package com.wiz3max.simplerest.handler.impl;

import com.wiz3max.simplerest.exception.ErrorCode;
import com.wiz3max.simplerest.exception.InternalErrorException;
import com.wiz3max.simplerest.handler.RowSorter;
import com.wiz3max.simplerest.metadata.Metadata;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CommonsLog
@Component
public class SimpleRowSorterByField implements RowSorter<List<Map<String, Object>>, List<String>> {

    @Override
    public List<Map<String, Object>> sort(List<Map<String, Object>> input,
                                          List<String> sortByFields,
                                          SortDirection sortDirection,
                                          Map<String, Metadata> metadataMap) {
        if(sortByFields == null || sortByFields.size() <= 0){
            return input;
        }

        final SortDirection nonNullSortDirection = sortDirection == null ? SortDirection.ASC : sortDirection;

        input.sort((e1, e2) -> {
            for (int i = 0; i < sortByFields.size(); i++) {
                String sortByField = sortByFields.get(i);

                int resCompare = 0;

                switch (metadataMap.get(sortByField).getType()) {
                    case TIMESTAMP -> {
                        //TODO: try comparable type to reduce code
                        LocalDateTime v1 = (LocalDateTime) e1.get(sortByField);
                        LocalDateTime v2 = (LocalDateTime) e2.get(sortByField);

                        if(v1 == null && v2 != null){
                            resCompare = -1;
                        } else if(v1 != null && v2 == null){
                            resCompare = 1;
                        } else if(v1 != null && v2 != null){
                            resCompare = v1.compareTo(v2);
                        }

                        if (resCompare == 0 && i < sortByFields.size() - 1) continue;
                        return resCompare * nonNullSortDirection.getVectorValue();
                    }
                    case DECIMAL -> {
                        Double v1 = (Double) e1.get(sortByField);
                        Double v2 = (Double) e2.get(sortByField);

                        if(v1 == null && v2 != null){
                            resCompare = -1;
                        } else if(v1 != null && v2 == null){
                            resCompare = 1;
                        } else if(v1 != null && v2 != null){
                            resCompare = v1.compareTo(v2);
                        }

                        if (resCompare == 0 && i < sortByFields.size() - 1) continue;
                        return resCompare * nonNullSortDirection.getVectorValue();
                    }
                    case STRING -> {
                        String v1 = (String) e1.get(sortByField);
                        String v2 = (String) e2.get(sortByField);

                        if(v1 == null && v2 != null){
                            resCompare = -1;
                        } else if(v1 != null && v2 == null){
                            resCompare = 1;
                        } else if(v1 != null && v2 != null){
                            resCompare = v1.compareTo(v2);
                        }

                        if (resCompare == 0 && i < sortByFields.size() - 1) continue;
                        return resCompare * nonNullSortDirection.getVectorValue();
                    }
                    default -> {
                        log.warn("Found new type doesn't defined : " + metadataMap.get(sortByField).getType());
                        return 0;
                    }
                }
            }
            //Should not have this case
            throw new InternalErrorException("Should not touch this flow", ErrorCode.INTERNAL_ERROR);
        });
        return input;
    }
}
