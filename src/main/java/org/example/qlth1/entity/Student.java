package org.example.qlth1.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentCode; 
    private String name;
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;
}