package com.springddd.application.service.leaf.dto;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeafAllocViewMapStruct {

    LeafAllocView toView(LeafAllocEntity entity);

    List<LeafAllocView> toViews(List<LeafAllocEntity> entities);
}
