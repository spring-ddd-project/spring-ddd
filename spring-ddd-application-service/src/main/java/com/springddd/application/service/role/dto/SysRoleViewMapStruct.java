package com.springddd.application.service.role.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.role.DataPermission;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysRoleViewMapStruct {

    @Mapping(target = "dataPermission", source = "dataPermission", qualifiedByName = "toDataPermission")
    SysRoleView toView(SysRoleEntity entity);

    List<SysRoleView> toViewList(List<SysRoleEntity> entities);

    @Named("toDataPermission")
    default DataPermission toDataPermission(String dataPermission) {
        if (dataPermission == null || dataPermission.isEmpty()) {
            return null;
        }
        try {
            return new ObjectMapper().readValue(dataPermission, DataPermission.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}












