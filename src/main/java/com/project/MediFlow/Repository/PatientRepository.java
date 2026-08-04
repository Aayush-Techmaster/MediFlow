package com.project.MediFlow.Repository;

import com.project.MediFlow.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByPhoneAndIdNot(String phone, Long id);
}
