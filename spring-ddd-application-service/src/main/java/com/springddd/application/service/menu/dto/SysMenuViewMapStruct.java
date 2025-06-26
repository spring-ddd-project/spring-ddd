package com.springddd.application.service.menu.dto;

import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysMenuViewMapStruct {

    @Mapping(source = "order", target = "meta.order")
    @Mapping(source = "title", target = "meta.title")
    @Mapping(source = "affixTab", target = "meta.affixTab")
    @Mapping(source = "noBasicLayout", target = "meta.noBasicLayout")
    SysMenuView toView(SysMenuEntity entity);

    List<SysMenuView> toViewList(List<SysMenuEntity> entities);
}
