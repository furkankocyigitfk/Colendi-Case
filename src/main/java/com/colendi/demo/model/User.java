package com.colendi.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp createDate;
    @UpdateTimestamp
    private Timestamp updateDate;
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Credit> credits;
}
