package br.com.jeftavares.webhook_caixa_residencial.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "events")
public class EventEntity {

    @Id
    @Field(name = "_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("ownerIdentity")
    private String ownerIdentity;

    @Field("identity")
    private String identity;

    @Field("contact")
    private Bson contact;

    @Field("messageId")
    private String messageId;

    @Field("storageDate")
    private String storageDate;

    @Field("category")
    private String category;

    @Field("action")
    private String action;

    @Field("extras")
    private Bson extras;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Bson getContact() {
        return contact;
    }

    public void setContact(Bson contact) {
        this.contact = contact;
    }

    public Bson getExtras() {
        return extras;
    }

    public void setExtras(Bson extras) {
        this.extras = extras;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getOwnerIdentity() {
        return ownerIdentity;
    }

    public void setOwnerIdentity(String ownerIdentity) {
        this.ownerIdentity = ownerIdentity;
    }

    public String getStorageDate() {
        return storageDate;
    }

    public void setStorageDate(String storageDate) {
        this.storageDate = storageDate;
    }
}