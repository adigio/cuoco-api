package com.cuoco.adapter.out.hibernate;

import com.cuoco.adapter.out.hibernate.model.AllergyHibernateModel;
import com.cuoco.adapter.out.hibernate.repository.GetAllergiesByIdHibernateRepositoryAdapter;
import com.cuoco.application.port.out.GetAllergiesByIdRepository;
import com.cuoco.application.usecase.model.Allergy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GetAllergiesByIdDatabaseRepositoryAdapter implements GetAllergiesByIdRepository {

    static final Logger log = LoggerFactory.getLogger(GetAllergiesByIdDatabaseRepositoryAdapter.class);

    private final GetAllergiesByIdHibernateRepositoryAdapter getAllergiesByIdHibernateRepositoryAdapter;

    public GetAllergiesByIdDatabaseRepositoryAdapter(GetAllergiesByIdHibernateRepositoryAdapter getAllergiesByIdHibernateRepositoryAdapter) {
        this.getAllergiesByIdHibernateRepositoryAdapter = getAllergiesByIdHibernateRepositoryAdapter;
    }

    @Override
    public List<Allergy> execute(List<Integer> allergiesIds) {
        log.info("Get allergies by IDs {} in database", allergiesIds);

        List<AllergyHibernateModel> allergies = getAllergiesByIdHibernateRepositoryAdapter.findByIdIn(allergiesIds);

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
