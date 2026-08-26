package com.project.MediFlow.Controller;

import com.project.MediFlow.Dtos.AppointmentRequest;
import com.project.MediFlow.Dtos.AppointmentResponse;
import com.project.MediFlow.Service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody AppointmentRequest request) {

        return ResponseEntity.ok(
                appointmentService.createAppointment(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }
}