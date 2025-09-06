package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.*;
import org.springframework.util.ObjectUtils;

public record ProjectInfo(String tableName, String packageName, String className, String moduleName, String projectName) {

    public ProjectInfo {
        if (ObjectUtils.isEmpty(tableName)) {
            throw new TableNameNullException();
        }
        if (ObjectUtils.isEmpty(packageName)) {
            throw new PackageNameNullException();
        }
        if (ObjectUtils.isEmpty(className)) {
            throw new ClassNameNullException();
        }
        if (ObjectUtils.isEmpty(moduleName)) {
            throw new ModuleNameNullException();
        }
        if (ObjectUtils.isEmpty(projectName)) {
            throw new ProjectNameNullException();
        }
    }
}
