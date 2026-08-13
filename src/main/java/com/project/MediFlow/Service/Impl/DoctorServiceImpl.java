package com.project.MediFlow.Service.Impl;

import com.project.MediFlow.Dtos.DoctorRequest;
import com.project.MediFlow.Dtos.DoctorResponse;
import com.project.MediFlow.Exception.ResourceNotFoundException;
import com.project.MediFlow.entities.Doctor;
import com.project.MediFlow.Repository.DoctorRepository;
import com.project.MediFlow.Service.DoctorService;
import com.project.MediFlow.Exception.DuplicateResourceException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorResponse createDoctor(DoctorRequest request) {

        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        if (doctorRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("Phone number already exists.");
        }

        Doctor doctor = Doctor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .specialization(request.getSpecialization())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return DoctorResponse.builder()
                .id(savedDoctor.getId())
                .firstName(savedDoctor.getFirstName())
                .lastName(savedDoctor.getLastName())
                .gender(savedDoctor.getGender())
                .specialization(savedDoctor.getSpecialization())
                .phone(savedDoctor.getPhone())
                .email(savedDoctor.getEmail())
                .build();
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> DoctorResponse.builder()
                        .id(doctor.getId())
                        .firstName(doctor.getFirstName())
                        .lastName(doctor.getLastName())
                        .gender(doctor.getGender())
                        .specialization(doctor.getSpecialization())
                        .phone(doctor.getPhone())
                        .email(doctor.getEmail())
                        .build())
                .toList();
    }
    @Override
    public DoctorResponse getDoctorById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found with id: " + id)
                );

        return DoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .gender(doctor.getGender())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .build();
    }

    @Override
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found with id: " + id)
                );

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setGender(request.getGender());
        doctor.setPhone(request.getPhone());
        doctor.setEmail(request.getEmail());

        Doctor updatedDoctor = doctorRepository.save(doctor);

        return DoctorResponse.builder()
                .id(updatedDoctor.getId())
                .firstName(updatedDoctor.getFirstName())
                .lastName(updatedDoctor.getLastName())
                .gender(updatedDoctor.getGender())       
                .specialization(updatedDoctor.getSpecialization())
                .phone(updatedDoctor.getPhone())
                .email(updatedDoctor.getEmail())
                .build();
    }

    @Override
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found with id: " + id)
                );

        doctorRepository.delete(doctor);
    }
}