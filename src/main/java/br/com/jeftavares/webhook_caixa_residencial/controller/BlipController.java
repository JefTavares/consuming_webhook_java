package br.com.jeftavares.webhook_caixa_residencial.controller;

import br.com.jeftavares.webhook_caixa_residencial.service.BlipService;
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

        //System.out.println("Body recebido: " + body);

        blipService.saveMessage(body);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
}