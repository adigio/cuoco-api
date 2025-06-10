package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity(name = "user_allergies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAllergiesHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "allergy_id", referencedColumnName = "id")
    private AllergyHibernateModel allergy;
}