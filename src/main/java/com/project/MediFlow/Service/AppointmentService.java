package com.project.MediFlow.Service;

import com.project.MediFlow.Dtos.AppointmentRequest;
import com.project.MediFlow.Dtos.AppointmentResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest request);

    List<AppointmentResponse> getAllAppointments();

    AppointmentResponse getAppointmentById(Long id);

    AppointmentResponse cancelAppointment(Long id);

    AppointmentResponse confirmAppointment(Long id);

    AppointmentResponse completeAppointment(Long id);

    AppointmentResponse rescheduleAppointment(
            Long id,
            LocalDateTime newAppointmentDateTime
    );
//
//    AppointmentResponse updateAppointment(Long id, AppointmentRequest request);
//
//    void deleteAppointment(Long id);
}