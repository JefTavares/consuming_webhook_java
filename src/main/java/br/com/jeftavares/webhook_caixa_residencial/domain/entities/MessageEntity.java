package br.com.jeftavares.webhook_caixa_residencial.domain.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "messages")
public class MessageEntity {

    @Id
    @Field(name = "_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Field("type")
    private String type;

    @Field("content")
    private Bson content;

    @Field("from")
    private String from;

    @Field("to")
    private String to;

    @Field("metadata")
    private Bson metadata;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bson getContent() {
        return content;
    }

    public void setContent(Bson content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Bson getMetadata() {
        return metadata;
    }

    public void setMetadata(Bson metadata) {
        this.metadata = metadata;
    }
}