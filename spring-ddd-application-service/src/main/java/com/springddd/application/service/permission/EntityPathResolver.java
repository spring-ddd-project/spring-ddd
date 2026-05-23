package com.springddd.application.service.permission;

import java.util.Optional;

public interface EntityPathResolver {

    Optional<String> resolveEntityCode(String path);
}
