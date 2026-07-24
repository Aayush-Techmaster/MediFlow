package com.project.MediFlow.Repository;

import com.project.MediFlow.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
