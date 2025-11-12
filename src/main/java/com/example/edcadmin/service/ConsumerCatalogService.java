package com.example.edcadmin.service;

import com.example.edcadmin.repository.EdcConsumerCatalogRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ConsumerCatalogService {

    private final EdcConsumerCatalogRepository consumerCatalogRepository;

    public ConsumerCatalogService(EdcConsumerCatalogRepository consumerCatalogRepository) {
        this.consumerCatalogRepository = consumerCatalogRepository;
    }

    public Mono<String> fetchCatalog(String providerProtocolUrl) {
        return consumerCatalogRepository.fetchCatalog(providerProtocolUrl);
    }
}

