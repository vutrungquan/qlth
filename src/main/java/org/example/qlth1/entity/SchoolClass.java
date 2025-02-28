package org.example.qlth1.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "school_class")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClass {
    @Id
    private String classCode; 
    private String className;
}
