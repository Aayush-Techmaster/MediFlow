package com.project.MediFlow.Controller;

import com.project.MediFlow.Dtos.AppointmentRequest;
import com.project.MediFlow.Dtos.AppointmentResponse;
import com.project.MediFlow.Dtos.RescheduleAppointmentRequest;
import com.project.MediFlow.Service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.project.MediFlow.dto.PatientAppointmentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN') or (hasRole('DOCTOR') and @doctorAuthorization.isOwnAppointment(#id, authentication.name)) or (hasRole('PATIENT') and @patientAuthorization.isOwnAppointment(#id, authentication.name))")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN') or (hasRole('PATIENT') and @patientAuthorization.isOwnAppointment(#id, authentication.name))")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN') or (hasRole('DOCTOR') and @doctorAuthorization.isOwnAppointment(#id, authentication.name))")
    public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.confirmAppointment(id));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('DOCTOR') and @doctorAuthorization.isOwnAppointment(#id, authentication.name))")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.completeAppointment(id));
    }

    @PatchMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN') or (hasRole('DOCTOR') and @doctorAuthorization.isOwnAppointment(#id, authentication.name))")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable Long id,
            @Valid @RequestBody RescheduleAppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(
                id, request.getAppointmentDateTime()));
    }


    @PostMapping("/request")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> requestAppointment(
            @Valid @RequestBody PatientAppointmentRequest request,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        appointmentService.requestAppointment(
                                request,
                                authentication.getName()
                        )
                );
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<List<AppointmentResponse>> getPendingAppointments() {
        return ResponseEntity.ok(
                appointmentService.getPendingAppointments()
        );
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<AppointmentResponse> acceptAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.acceptAppointment(id)
        );
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<AppointmentResponse> rejectAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.rejectAppointment(id)
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(
            Authentication authentication) {

        return ResponseEntity.ok(
                appointmentService.getMyAppointments(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/doctor/my")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<AppointmentResponse>> getMyDoctorAppointments(
            Authentication authentication) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsForDoctor(
                        authentication.getName()
                )
        );
    }


}