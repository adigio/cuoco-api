package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergiesHibernateModel;
import com.cuoco.application.port.out.FindAllergiesByNameRepository;
import com.cuoco.application.usecase.model.Allergies;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FindAllergiesByNameDatabaseRepositoryAdapter implements FindAllergiesByNameRepository {
    private final FindAllergiesByNameHibernateRepositoryAdapter findAllergiesByNameHibernateRepositoryAdapter;

    public FindAllergiesByNameDatabaseRepositoryAdapter(FindAllergiesByNameHibernateRepositoryAdapter findAllergiesByNameHibernateRepositoryAdapter) {
        this.findAllergiesByNameHibernateRepositoryAdapter = findAllergiesByNameHibernateRepositoryAdapter;
    }

    @Override
    public List<Allergies> execute(List<String> allergiesNames) {
        List<AllergiesHibernateModel> allergies = findAllergiesByNameHibernateRepositoryAdapter.findByNameIn(allergiesNames);

        return buildAllergies(allergies);
    }

    private List<Allergies> buildAllergies(List<AllergiesHibernateModel> allergies) {
        return allergies.stream()
                .map(model -> new Allergies(model.getId(), model.getName()))
                .collect(Collectors.toList());
    }
}
