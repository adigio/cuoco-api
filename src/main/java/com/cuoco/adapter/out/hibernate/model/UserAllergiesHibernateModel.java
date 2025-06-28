package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_allergies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAllergiesHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne
    @JoinColumn(name = "allergy_id", referencedColumnName = "id")
    private AllergyHibernateModel allergy;
}