package br.com.jeftavares.webhook_caixa_residencial.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "contacts")
public class ContactsEntity {

    @Id
    @Field(name = "_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field(name = "lastMessageDate")
    private String lastMessageDate;

    @Field(name = "identity")
    private String identity;

    @Field(name = "source")
    private String source;

    @Field(name = "name")
    private String name;

    @Field(name = "email")
    private String email;

    @Field(name = "phoneNumber")
    private String phoneNumber;

    @Field(name = "gender")
    private String gender;

    @Field(name = "taxDocument")
    private String taxDocument;

    @Field(name = "extras")
    private Bson extras;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bson getExtras() {
        return extras;
    }

    public void setExtras(Bson extras) {
        this.extras = extras;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(String lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTaxDocument() {
        return taxDocument;
    }

    public void setTaxDocument(String taxDocument) {
        this.taxDocument = taxDocument;
    }
}