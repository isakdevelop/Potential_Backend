package com.potential.api.repository;

import com.potential.api.model.EmailCertification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailCertificationRepository extends CrudRepository<EmailCertification, String> {
}
