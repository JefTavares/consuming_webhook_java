package br.com.jeftavares.webhook_caixa_residencial.repository;

import br.com.jeftavares.webhook_caixa_residencial.domain.entities.ProcessingErrorEntity;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;


@Document(collection = "processing_errors")
public interface ProcessingErrorRepository extends MongoRepository<ProcessingErrorEntity, Bson> {
}