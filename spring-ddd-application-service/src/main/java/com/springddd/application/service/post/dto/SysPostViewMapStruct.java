package com.springddd.application.service.post.dto;

import com.springddd.infrastructure.persistence.entity.SysPostEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysPostViewMapStruct {

    SysPostView toView(SysPostEntity entity);

    List<SysPostView> toViews(List<SysPostEntity> entities);
}
