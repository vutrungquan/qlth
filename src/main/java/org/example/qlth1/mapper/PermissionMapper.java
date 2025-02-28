package org.example.qlth1.mapper;

import org.example.qlth1.dto.request.PermissionRequest;
import org.example.qlth1.dto.response.PermissionResponse;
import org.example.qlth1.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
