package org.example.qlth1.mapper;


import org.example.qlth1.dto.request.RoleRequest;
import org.example.qlth1.dto.response.PermissionResponse;
import org.example.qlth1.dto.response.RoleResponse;
import org.example.qlth1.entity.Permission;
import org.example.qlth1.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "permissions", target = "permissions", qualifiedByName = "mapPermissionsFromString")
    Role toRole(RoleRequest request);

    @Mapping(source = "permissions", target = "permissions", qualifiedByName = "mapPermissionsToPermissionResponse")
    RoleResponse toRoleResponse(Role role);

    @Named("mapPermissionsFromString")
    default Set<Permission> mapPermissionsFromString(Set<String> permissionNames) {
        if (permissionNames == null) {
            return null;
        }
        return permissionNames.stream()
                .map(name -> Permission.builder().name(name).build())
                .collect(Collectors.toSet());
    }

    @Named("mapPermissionsToPermissionResponse")
    default Set<PermissionResponse> mapPermissionsToPermissionResponse(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(permission -> PermissionResponse.builder()
                        .name(permission.getName())
                        .description(permission.getDescription())
                        .build())
                .collect(Collectors.toSet());
    }
}
