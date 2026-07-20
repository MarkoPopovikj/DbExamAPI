package finki.ukim.mk.dbexamapi.service.content.impl;

import finki.ukim.mk.dbexamapi.domain.dtos.content.DocumentDto;
import finki.ukim.mk.dbexamapi.domain.exceptions.content.DocumentDoesNotExistException;
import finki.ukim.mk.dbexamapi.domain.models.content.Document;
import finki.ukim.mk.dbexamapi.repository.DocumentRepository;
import finki.ukim.mk.dbexamapi.service.content.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Optional<Document> findById(String id) {
        return documentRepository.findById(id);
    }

    @Override
    public Document findByIdNotNull(String id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentDoesNotExistException(id));
    }

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    @Transactional
    public Document create(DocumentDto documentDto) {
        Document document = new Document();
        document.setFolder(documentDto.folder());
        document.setName(documentDto.name());
        document.setDocumentMd(documentDto.documentMd());

        Document saved = documentRepository.save(document);
        log.info("Created document with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Document update(String id, DocumentDto documentDto) {
        Document document = findByIdNotNull(id);

        document.setFolder(documentDto.folder());
        document.setName(documentDto.name());
        document.setDocumentMd(documentDto.documentMd());

        Document saved = documentRepository.save(document);
        log.info("Updated document with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Document deleteById(String id) {
        Document document = findByIdNotNull(id);
        documentRepository.delete(document);
        log.info("Deleted document with id: {}", id);
        return document;
    }
}
