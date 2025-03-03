package org.example.qlth1.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequest {
    @NotNull(message = "Username không được để trống")
    private String username;
    
    @NotEmpty(message = "Danh sách vai trò không được để trống")
    private Set<String> roleNames;
}