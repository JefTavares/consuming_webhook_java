package br.com.jeftavares.webhook_caixa_residencial.repository;

import br.com.jeftavares.webhook_caixa_residencial.domain.entities.ContactsEntity;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(collection = "contacts")
public interface ContactsRepository extends MongoRepository<ContactsEntity, Bson> {
}