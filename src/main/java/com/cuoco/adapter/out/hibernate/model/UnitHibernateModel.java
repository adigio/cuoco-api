package com.cuoco.adapter.out.hibernate.model;

import com.cuoco.application.usecase.model.Unit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "units")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitHibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private String symbol;

    public static UnitHibernateModel fromDomain(Unit unit) {
        return UnitHibernateModel.builder()
                .id(unit.getId())
                .description(unit.getDescription())
                .symbol(unit.getSymbol())
                .build();
    }

    public Unit toDomain() {
        return Unit.builder()
                .id(id)
                .description(description)
                .symbol(symbol)
                .build();
    }
}