package br.com.jeftavares.webhook_caixa_residencial.controller.dto;

public record ProcessingErrorDTO(String errorMessage,
                                 String operationType,
                                 String requestData,
                                 String stackTrace,
                                 java.time.Instant timeStamp) {

}