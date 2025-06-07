package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "allergies")
@Data
public class AllergiesHibernateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
}
