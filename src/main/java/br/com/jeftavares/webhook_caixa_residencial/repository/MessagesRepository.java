package br.com.jeftavares.webhook_caixa_residencial.repository;

import br.com.jeftavares.webhook_caixa_residencial.domain.entities.MessageEntity;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(collection = "messages")
//@CompoundIndex(def = "{'dateTime': 2}")
public interface MessagesRepository extends MongoRepository<MessageEntity, Bson> {


}