package com.springddd.application.service.menu.dto;

import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysMenuViewMapStruct {

    SysMenuView toView(SysMenuEntity entity);

    List<SysMenuView> toViewList(List<SysMenuEntity> entities);
}
