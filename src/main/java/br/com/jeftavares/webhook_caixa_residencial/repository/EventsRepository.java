package br.com.jeftavares.webhook_caixa_residencial.repository;

import br.com.jeftavares.webhook_caixa_residencial.domain.entities.EventEntity;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

@Document(collection = "events")
public interface EventsRepository extends MongoRepository<EventEntity, Bson> {
}