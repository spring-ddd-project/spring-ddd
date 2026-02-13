package com.springddd.domain.role.composite;

import java.util.ArrayList;
import java.util.List;

public class CompositePermission implements PermissionComponent {
    private final List<PermissionComponent> components = new ArrayList<>();

    public void add(PermissionComponent component) {
        components.add(component);
    }

    @Override
    public boolean isAuthorized(Object context) {
        return components.stream().allMatch(c -> c.isAuthorized(context));
    }
}






















