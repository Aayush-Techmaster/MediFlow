package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.AppointmentRequest;
import com.project.MediFlow.Dtos.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAllAppointments();

//    AppointmentResponse getAppointmentById(Long id);
//
//    AppointmentResponse updateAppointment(Long id, AppointmentRequest request);
//
//    void deleteAppointment(Long id);
}