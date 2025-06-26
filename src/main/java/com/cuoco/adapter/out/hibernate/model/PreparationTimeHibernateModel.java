package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.PreparationTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "preparation_times")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreparationTimeHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;

    public PreparationTime toDomain() {
        return PreparationTime.builder()
                .id(id)
                .description(description)
                .build();
    }
}
