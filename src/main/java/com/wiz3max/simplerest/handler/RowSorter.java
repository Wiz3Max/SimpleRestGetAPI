package com.wiz3max.simplerest.handler;

import com.wiz3max.simplerest.metadata.Metadata;

import java.util.List;
import java.util.Map;

public interface RowSorter<T extends List<? extends Map<? extends String, ?>>, K extends List<? extends String>> {

    enum SortDirection {
        ASC(1),
        DESC(-1);

        private int vectorValue;

        SortDirection(int vectorValue) {
            this.vectorValue = vectorValue;
        }

        public int getVectorValue(){
            return this.vectorValue;
        }

        public static SortDirection valueOfIgnoreCase(String sortDirectionStr) {
            return SortDirection.valueOf(sortDirectionStr.toUpperCase());
        }
    }

    T sort(T input, K sortByFields, SortDirection sortDirection, Map<String, Metadata> metadataMap);

}
