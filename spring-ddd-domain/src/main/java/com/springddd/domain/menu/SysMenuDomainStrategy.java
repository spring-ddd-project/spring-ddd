package com.springddd.domain.menu;

public interface SysMenuDomainStrategy {

    boolean check(Integer type);

    SysMenuDomain handle(String name, Catalog catalog, Menu menu, Button button, MenuExtendInfo menuExtendInfo);
}






