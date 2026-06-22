package com.springddd.application.service.leaf.dto;

import com.springddd.infrastructure.persistence.entity.LeafWorkerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeafWorkerViewMapStruct {

    LeafWorkerView toView(LeafWorkerEntity entity);

    List<LeafWorkerView> toViews(List<LeafWorkerEntity> entities);
}
