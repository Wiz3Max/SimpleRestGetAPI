package com.wiz3max.simplerest.metadata;

import com.wiz3max.simplerest.metadata.impl.MetadataProviderImpl;

import java.util.Map;

public interface MetadataProvider {
    Map<String, Metadata> getMetadata(MetadataProviderImpl.SchemaType schemaType);
}
