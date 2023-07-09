package com.wiz3max.simplerest.metadata;

import java.util.Map;

public interface MetadataProvider {
    Map<String, Metadata> getMetadata();
}
