package finki.ukim.mk.dbexamapi.service.content;

import finki.ukim.mk.dbexamapi.domain.dtos.content.DocumentDto;
import finki.ukim.mk.dbexamapi.domain.models.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<Document> findById(String id);

    Document findByIdNotNull(String id);

    List<Document> findAll();

    Document create(DocumentDto documentDto);

    Document update(String id, DocumentDto documentDto);

    Document deleteById(String id);
}
