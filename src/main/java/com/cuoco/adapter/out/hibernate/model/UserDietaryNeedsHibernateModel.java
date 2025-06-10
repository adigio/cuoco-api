package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserHibernateModel user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dietary_need_id", referencedColumnName = "id")
    private DietaryNeedHibernateModel dietaryNeed;
}