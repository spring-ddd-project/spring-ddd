package com.springddd.application.service.user.dto;

import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysUserPostViewMapStruct {

    SysUserPostView toView(SysUserPostEntity entity);

    List<SysUserPostView> toViews(List<SysUserPostEntity> entities);
}
