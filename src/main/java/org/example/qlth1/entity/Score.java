package org.example.qlth1.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "score")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @ManyToOne
    @JoinColumn(name = "subject_code")
    private Subject subject;

    
    private Double scoreValue;

    
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}