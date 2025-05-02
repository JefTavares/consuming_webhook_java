package br.com.jeftavares.webhook_caixa_residencial.controller;

import br.com.jeftavares.webhook_caixa_residencial.controller.dto.ProcessingErrorDTO;
import br.com.jeftavares.webhook_caixa_residencial.domain.entities.ProcessingErrorEntity;
import br.com.jeftavares.webhook_caixa_residencial.repository.ProcessingErrorRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/monitoring")

public class MonitoringController {

    private final ProcessingErrorRepository errorRepository;


    public MonitoringController(ProcessingErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    @GetMapping("/errors")
    public ResponseEntity<List<ProcessingErrorDTO>> findByDate(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                               @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {


        List<ProcessingErrorEntity> errors = errorRepository.findByTimestampBetween(startDate.toInstant(), endDate.toInstant());

        System.out.println(errors);

        System.out.println("Timestamp " + errors.getFirst().getTimestamp());
        System.out.println("errorMessage " + errors.getFirst().getErrorMessage());
        System.out.println("operationType " + errors.getFirst().getOperationType());
        System.out.println("RequestData " + errors.getFirst().getRequestData());
        System.out.println("StackTrace " + errors.getFirst().getStackTrace());

        List<ProcessingErrorDTO> errorDTOs = errors.stream()
                .map(error -> new ProcessingErrorDTO(
                        error.getErrorMessage(),
                        error.getOperationType(),
                        error.getRequestData(),
                        error.getStackTrace(),
                        error.getTimestamp()))
                .toList();

        return ResponseEntity.ok(errorDTOs);
    }
}