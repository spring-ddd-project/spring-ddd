package com.springddd.domain.menu;

import lombok.Data;

@Data
public class MenuExtendInfo {

    private Integer sortOrder;

    private String visible;

    private String embedded;

    private String menuStatus;
}
