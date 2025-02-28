package org.example.qlth1.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subject")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    private String subjectCode; 
    private String subjectName;
}