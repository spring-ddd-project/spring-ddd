package com.springddd.application.service.gen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenFileView implements Serializable {

    private String layer;

    private String fileDir;

    private String fileName;

    private String content;
}
