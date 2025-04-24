package br.com.jeftavares.webhook_caixa_residencial.services;

import br.com.jeftavares.webhook_caixa_residencial.domain.entities.ContactsEntity;
import br.com.jeftavares.webhook_caixa_residencial.domain.entities.EventEntity;
import br.com.jeftavares.webhook_caixa_residencial.domain.entities.MessageEntity;
import br.com.jeftavares.webhook_caixa_residencial.domain.enums.BlipWebhookTypes;
import br.com.jeftavares.webhook_caixa_residencial.repository.ContactsRepository;
import br.com.jeftavares.webhook_caixa_residencial.repository.EventsRepository;
import br.com.jeftavares.webhook_caixa_residencial.repository.MessagesRepository;
import org.bson.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class BlipService {

    private final MessagesRepository messagesRepository;
    private final EventsRepository eventsRepository;
    private final ContactsRepository contactsRepository;


    public BlipService(MessagesRepository messageRepository, EventsRepository eventsRepository, ContactsRepository contactsRepository) {
        this.messagesRepository = messageRepository;
        this.eventsRepository = eventsRepository;
        this.contactsRepository = contactsRepository;
    }


    public void saveMessage(Map<String, Object> body) {
        try {
            //Verifica o tipo
            BlipWebhookTypes type = getType(body);

            // Processa os dados de forma assíncrona
            processDataAsync(body, type);

        } catch (Exception e) {
            System.out.println(Map.of(
                    "body", body.toString(),
                    "stack-error", e.getMessage()
            ));

            body.toString();
        }
    }

    @Async
    public CompletableFuture<Void> processDataAsync(Map<String, Object> body, BlipWebhookTypes type) {
        try {
            switch (type) {
                case BlipWebhookTypes.EVENT:
                    System.out.println("Processando assincronamente - Tipo: " + type);
                    addEvent(body);
                    break;
                case BlipWebhookTypes.CONTACT:
                    System.out.println("Processando assincronamente - Tipo: " + type);
                    addContact(body);
                    break;
                case BlipWebhookTypes.MESSAGE:
                    System.out.println("Processando assincronamente - Tipo: " + type);
                    addMessage(body);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo não suportado");
            }
        } catch (Exception e) {
            System.out.println("Erro no processamento assíncrono: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    private BlipWebhookTypes getType(Map<String, Object> body) {
        // Implementação existente...
        if (body.get("category") != null && body.get("action") != null) {
            return BlipWebhookTypes.EVENT;
        }

        if (body.get("lastMessageDate") != null) {
            return BlipWebhookTypes.CONTACT;
        }

        return BlipWebhookTypes.MESSAGE;
    }

    private void addEvent(Map<String, Object> body) {
        // Implementação existente...
        EventEntity eventEntity = new EventEntity();

        eventEntity.setOwnerIdentity(body.get("ownerIdentity").toString());
        eventEntity.setOwnerIdentity(body.get("identity").toString());
        eventEntity.setContact(new Document((Map<String, ?>) body.get("contact")));

        if (body.containsKey("messageId") && body.get("messageId") != null) {
            eventEntity.setMessageId(body.get("messageId").toString());
        }

        eventEntity.setStorageDate(body.get("storageDate").toString());
        eventEntity.setCategory(body.get("category").toString());
        eventEntity.setAction(body.get("action").toString());
        if (body.containsKey("extras") && body.get("extras") != null) {
            eventEntity.setExtras(new Document((Map<String, Object>) body.get("extras")));
        }

        eventsRepository.save(eventEntity);
    }

    private void addMessage(Map<String, Object> body) {
        // Implementação existente...
        MessageEntity messageEntity = new MessageEntity();

        messageEntity.setType(body.get("type").toString());
        if (body.get("content") instanceof Map) {
            messageEntity.setContent(new Document((Map<String, Object>) body.get("content")));
        } else {
            messageEntity.setContent(new Document(Map.of("content", body.get("content").toString())));
        }

        messageEntity.setFrom(body.get("from").toString());
        messageEntity.setTo(body.get("to").toString());
        messageEntity.setMetadata(new Document((Map<String, Object>) body.get("metadata")));

        // Salva a entidade no MongoDB
        messagesRepository.save(messageEntity);
    }

    private void addContact(Map<String, Object> body) {
        // Implementação existente...
        ContactsEntity contactEntity = new ContactsEntity();

        contactEntity.setLastMessageDate(body.get("lastMessageDate").toString());
        contactEntity.setIdentity(body.get("identity").toString());
        contactEntity.setSource(body.get("source").toString());
        contactEntity.setName(body.get("name").toString());
        contactEntity.setEmail(body.get("email").toString());
        contactEntity.setPhoneNumber(body.get("phoneNumber").toString());
        contactEntity.setGender(body.get("gender").toString());
        contactEntity.setTaxDocument(body.get("taxDocument").toString());
        contactEntity.setExtras(new Document((Map<String, Object>) body.get("extras")));

        contactsRepository.save(contactEntity);
    }
}