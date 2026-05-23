package com.springddd.application.service.user.dto;

import org.junit.jupiter.api.Test;

class ClassLocationTest {
    @Test
    void printLocation() {
        System.out.println("SysUserCommand location: " + SysUserCommand.class.getProtectionDomain().getCodeSource().getLocation());
    }
}
