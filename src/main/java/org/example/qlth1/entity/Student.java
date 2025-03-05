package org.example.qlth1.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Liên kết với bảng user

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;
    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects; // Thêm trường subjects
}
