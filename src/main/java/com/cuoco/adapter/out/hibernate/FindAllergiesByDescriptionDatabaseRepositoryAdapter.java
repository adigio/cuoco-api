package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.FindAllergiesByDescriptionHibernateRepositoryAdapter;
import com.cuoco.application.port.out.FindAllergiesByDescriptionRepository;
import com.cuoco.application.usecase.model.Allergy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FindAllergiesByDescriptionDatabaseRepositoryAdapter implements FindAllergiesByDescriptionRepository {

    private final FindAllergiesByDescriptionHibernateRepositoryAdapter findAllergiesByDescriptionHibernateRepositoryAdapter;

    public FindAllergiesByDescriptionDatabaseRepositoryAdapter(FindAllergiesByDescriptionHibernateRepositoryAdapter findAllergiesByDescriptionHibernateRepositoryAdapter) {
        this.findAllergiesByDescriptionHibernateRepositoryAdapter = findAllergiesByDescriptionHibernateRepositoryAdapter;
    }

    @Override
    public List<Allergy> execute(List<String> descriptions) {
        List<AllergyHibernateModel> allergies = findAllergiesByDescriptionHibernateRepositoryAdapter.findByDescriptionIn(descriptions);

        return buildAllergies(allergies);
    }

    private List<Allergy> buildAllergies(List<AllergyHibernateModel> allergies) {
        return allergies.stream()
                .map(this::buildAllergy)
                .collect(Collectors.toList());
    }

    private Allergy buildAllergy(AllergyHibernateModel allergyResponse) {
        return Allergy.builder()
                .id(allergyResponse.getId())
                .description(allergyResponse.getDescription())
                .build();
    }
}
