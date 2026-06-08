package com.springddd.application.service.gen.dto;

import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenTemplateViewMapStruct {

    GenTemplateView toView(GenTemplateEntity entity);

    List<GenTemplateView> toViews(List<GenTemplateEntity> entities);
}
