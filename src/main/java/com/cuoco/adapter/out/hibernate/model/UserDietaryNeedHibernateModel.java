package com.cuoco.adapter.out.hibernate.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserDietaryNeedHibernateModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    // Relación ManyToOne con la necesidad alimentaria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dietary_need_id", nullable = false)
    private DietaryNeedsHibernateModel dietaryNeed;
}
