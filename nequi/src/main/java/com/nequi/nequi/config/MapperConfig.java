package com.nequi.nequi.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.NameTransformers;
import org.modelmapper.convention.NamingConventions;

public class MapperConfig<D,B> {

    private final Class<D> data;
    private final Class<B> builder;

    private final ModelMapper mapper;

    public MapperConfig(Class<D> data, Class<B> builder){
        this.data = data;
        this.builder = builder;

        mapper = new ModelMapper();
        Configuration builderConfiguration = mapper.getConfiguration().copy()
                .setDestinationNameTransformer(NameTransformers.builder())
                .setDestinationNamingConvention(NamingConventions.builder());
        mapper.createTypeMap(data, builder, builderConfiguration);
    }

    public B toEntity(D data){
        return mapper.map(data, builder);
    }

    public D toData(Object entity){
        return mapper.map(entity, data);
    }
}
