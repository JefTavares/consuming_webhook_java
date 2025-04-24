package br.com.jeftavares.webhook_caixa_residencial.controller;

import br.com.jeftavares.webhook_caixa_residencial.services.BlipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhook/v1/blip")
public class BlipController {

    private BlipService blipService;

    public BlipController(BlipService blipService) {
        this.blipService = blipService;
    }

    @PostMapping()
    public ResponseEntity<?> handlerWebhookBlip(@RequestBody Map<String, Object> body) {
        try {
            // Apenas inicia o processamento e retorna imediatamente
            blipService.saveMessage(body);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("message", "Webhook recebido, processando dados"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}