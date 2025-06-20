package com.springddd.application.service.menu.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SysMenuParentTreeView implements Serializable {

    private Long value;

    private String label;

    private List<SysMenuParentTreeView> children;

    public SysMenuParentTreeView(Long value, String label) {
        this.value = value;
        this.label = label;
        this.children = new ArrayList<>();
    }
}
