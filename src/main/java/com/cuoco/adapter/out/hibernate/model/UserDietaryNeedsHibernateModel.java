package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_dietary_needs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDietaryNeedsHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne
    @JoinColumn(name = "dietary_need_id", referencedColumnName = "id")
    private DietaryNeedHibernateModel dietaryNeed;
}