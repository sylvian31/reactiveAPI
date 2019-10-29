package com.scaron.reactiveAPI.repository;

import com.scaron.reactiveAPI.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends ReactiveMongoRepository<Item, String> {

    Mono<Item> findByDescription(String description);
}
