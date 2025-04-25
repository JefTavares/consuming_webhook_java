package br.com.jeftavares.webhook_caixa_residencial.controller;

import br.com.jeftavares.webhook_caixa_residencial.repository.ProcessingErrorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitoring")

public class MonitoringController {

    private final ProcessingErrorRepository errorRepository;


    public MonitoringController(ProcessingErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    @GetMapping("/errors")
    public ResponseEntity<List<ProcessingErrorDTO>> getUnresolvedErrors() {
        List<ProcessingErrorEntity> errors = errorRepository.findByResolvedFalse();
        // Mapear para DTOs e retornar
    }


    @GetMapping("/status")
    public ResponseEntity<SystemStatus> getSystemStatus() {
        long errorCount = errorRepository.countByResolvedFalse();
        SystemStatus status = new SystemStatus();
        status.setHealthy(errorCount == 0);
        status.setPendingErrors(errorCount);
        // Outros dados de status
        return ResponseEntity.ok(status);
    }

}