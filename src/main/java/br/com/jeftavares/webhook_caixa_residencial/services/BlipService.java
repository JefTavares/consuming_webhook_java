package br.com.jeftavares.webhook_caixa_residencial.services;

import br.com.jeftavares.webhook_caixa_residencial.domain.entities.ContactsEntity;
import br.com.jeftavares.webhook_caixa_residencial.domain.entities.EventEntity;
import br.com.jeftavares.webhook_caixa_residencial.domain.entities.MessageEntity;
import br.com.jeftavares.webhook_caixa_residencial.domain.entities.ProcessingErrorEntity;
import br.com.jeftavares.webhook_caixa_residencial.domain.enums.BlipWebhookTypes;
import br.com.jeftavares.webhook_caixa_residencial.repository.ContactsRepository;
import br.com.jeftavares.webhook_caixa_residencial.repository.EventsRepository;
import br.com.jeftavares.webhook_caixa_residencial.repository.MessagesRepository;
import br.com.jeftavares.webhook_caixa_residencial.repository.ProcessingErrorRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Service
public class BlipService {

    private static final Logger log = LoggerFactory.getLogger(BlipService.class);

    private final MessagesRepository messagesRepository;
    private final EventsRepository eventsRepository;
    private final ContactsRepository contactsRepository;
    private final ProcessingErrorRepository errorRepository;

    public BlipService(MessagesRepository messageRepository, EventsRepository eventsRepository, ContactsRepository contactsRepository, ProcessingErrorRepository errorRepository) {
        this.messagesRepository = messageRepository;
        this.eventsRepository = eventsRepository;
        this.contactsRepository = contactsRepository;
        this.errorRepository = errorRepository;
    }


    public void saveMessage(Map<String, Object> body) throws JsonProcessingException {
        try {
            BlipWebhookTypes type = getType(body);
            processDataAsync(body, type);
        } catch (Exception e) {
            log.error("saveMessage error: body={}, mensagem={}",
                    body, e.getMessage(), e);

            ProcessingErrorEntity error = new ProcessingErrorEntity();
            error.setTimestamp(Instant.now());
            error.setErrorMessage(e.getMessage());
            error.setStackTrace(Arrays.toString(e.getStackTrace()));
            //error.setRequestData(body.toString());
            error.setRequestData(new ObjectMapper().writeValueAsString(body));

            errorRepository.save(error);
        }
    }

    @Async
    public void processDataAsync(Map<String, Object> body, BlipWebhookTypes type) throws JsonProcessingException {
        try {
            switch (type) {
                case BlipWebhookTypes.EVENT:
                    //System.out.println("Processando assincronamente - Tipo: " + type);
                    addEvent(body);
                    break;
                case BlipWebhookTypes.CONTACT:
                    //System.out.println("Processando assincronamente - Tipo: " + type);
                    addContact(body);
                    break;
                case BlipWebhookTypes.MESSAGE:
                    //System.out.println("Processando assincronamente - Tipo: " + type);
                    addMessage(body);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo não suportado");
            }
        } catch (Exception e) {
            // Log detalhado com informações para diagnóstico
            log.error("Error processDataAsync: tipo={}, body={}, mensagem={}",
                    type, body, e.getMessage(), e);

            ProcessingErrorEntity error = new ProcessingErrorEntity();
            error.setTimestamp(Instant.now());
            error.setOperationType(type.toString());
            error.setErrorMessage(e.getMessage());
            error.setStackTrace(Arrays.toString(e.getStackTrace()));
            //error.setRequestData(body.toString());
            error.setRequestData(new ObjectMapper().writeValueAsString(body));

            errorRepository.save(error);

            // Opcional: enviar notificação ou registrar em sistema de monitoramento
            //notifyErrorMonitoringSystem(e, body, type);

            // Rethrow para propagação do erro (opcional)
            //throw new RuntimeException("Erro ao processar dados: " + type, e);
        }
        CompletableFuture.completedFuture(null);
    }

    private BlipWebhookTypes getType(Map<String, Object> body) {

        if (body.get("category") != null && body.get("action") != null) {
            return BlipWebhookTypes.EVENT;
        }

        if (body.get("lastMessageDate") != null) {
            return BlipWebhookTypes.CONTACT;
        }

        return BlipWebhookTypes.MESSAGE;
    }

    private void addEvent(Map<String, Object> body) {

        EventEntity eventEntity = new EventEntity();

        eventEntity.setOwnerIdentity(body.get("ownerIdentity").toString());
        eventEntity.setOwnerIdentity(body.get("identity").toString());
        eventEntity.setContact(convertToDocument(body.get("contact")));

        if (body.containsKey("messageId") && body.get("messageId") != null) {
            eventEntity.setMessageId(body.get("messageId").toString());
        }

        eventEntity.setStorageDate(body.get("storageDate").toString());
        eventEntity.setCategory(body.get("category").toString());
        eventEntity.setAction(body.get("action").toString());
        if (body.containsKey("extras") && body.get("extras") != null) {
            eventEntity.setExtras(convertToDocument(body.get("extras")));
        }

        eventsRepository.save(eventEntity);
    }

    private void addMessage(Map<String, Object> body) {
        // Implementação existente...
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setType(body.get("type").toString());
        if (body.get("content") instanceof Map) {
            messageEntity.setContent(convertToDocument(body.get("content")));
        } else {
            messageEntity.setContent(new Document(Map.of("content", body.get("content").toString())));
        }

        messageEntity.setFrom(body.get("from").toString());
        messageEntity.setTo(body.get("to").toString());
        messageEntity.setMetadata(convertToDocument(body.get("metadata")));

        // Salva a entidade no MongoDB
        messagesRepository.save(messageEntity);
    }

    private void addContact(Map<String, Object> body) {

        ContactsEntity contactEntity = new ContactsEntity();

        contactEntity.setLastMessageDate(body.get("lastMessageDate").toString());
        contactEntity.setIdentity(body.get("identity").toString());
        contactEntity.setSource(body.get("source").toString());
        contactEntity.setName(body.get("name").toString());
        contactEntity.setEmail(body.get("email").toString());
        contactEntity.setPhoneNumber(body.get("phoneNumber").toString());
        contactEntity.setGender(body.get("gender").toString());
        contactEntity.setTaxDocument(body.get("taxDocument").toString());
        contactEntity.setExtras(convertToDocument(body.get("extras")));

        contactsRepository.save(contactEntity);
    }

    /**
     * Converte um objeto para Document do MongoDB.
     * Se o objeto não for um Map, retorna um Document vazio.
     *
     * @param obj O objeto a ser convertido
     * @return O Document convertido
     */
    private Document convertToDocument(Object obj) {
        if (obj == null) {
            return new Document(Collections.emptyMap());
        }

        if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            return new Document(map);
        }

        return new Document(Collections.emptyMap());
    }
}