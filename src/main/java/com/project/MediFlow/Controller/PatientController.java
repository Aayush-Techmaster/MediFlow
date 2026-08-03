package com.project.MediFlow.Controller;


import com.project.MediFlow.Dtos.PatientRequest;
import com.project.MediFlow.Dtos.PatientResponse;
import com.project.MediFlow.Repository.PatientRepository;
import com.project.MediFlow.Service.PatientService;
import com.project.MediFlow.entities.Patient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(("/api/patients"))
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final PatientRepository patientRepository;

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody PatientRequest request) {

        PatientResponse response = patientService.createPatient(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients(){
        List<PatientResponse> patients = patientService.getAllPatients();

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity< Optional<PatientResponse>> getPatientById(@PathVariable Long id){
        Optional<PatientResponse>optionalPatient = patientService.getPatientById(id);
        return ResponseEntity.ok(optionalPatient);
    }


}
