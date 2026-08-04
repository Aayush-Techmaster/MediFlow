package com.project.MediFlow.Controller;


import com.project.MediFlow.Dtos.PatientRequest;
import com.project.MediFlow.Dtos.PatientResponse;
import com.project.MediFlow.Repository.PatientRepository;
import com.project.MediFlow.Service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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


    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {

        PatientResponse updatedPatient =
                patientService.updatePatient(id, request);

        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }


}
