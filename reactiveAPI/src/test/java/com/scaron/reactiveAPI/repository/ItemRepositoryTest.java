package com.scaron.reactiveAPI.repository;

import com.scaron.reactiveAPI.document.Item;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@DirtiesContext
@Ignore
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    static List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.99),
            new Item("ABC", "Bose Headphones", 149.99));


    public void setUp() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemRepository::save)
                .doOnNext((item -> {
                    System.out.println("Inserted Item is :" + item);
                }))
                .blockLast();

    }


    @Test
    public void getAllItems() {
        setUp();
        StepVerifier.create(itemRepository.findAll()) // 4
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemByID() {
        setUp();
        StepVerifier.create(itemRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches((item -> item.getDescription().equals("Bose Headphones")))
                .verifyComplete();


    }

    @Test
    public void findItemByDescrition() {
        setUp();
        StepVerifier.create(itemRepository.findByDescription("Bose Headphones").log("findItemByDescrition : "))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        setUp();
        Item item = new Item(null, "Google Home Mini", 30.00);
        Mono<Item> savedItem = itemRepository.save(item);
        StepVerifier.create(savedItem.log("saveItem : "))
                .expectSubscription()
                .expectNextMatches(item1 -> (item1.getId() != null && item1.getDescription().equals("Google Home Mini")))
                .verifyComplete();

    }

    @Test
    public void updateItem() {
        setUp();
        double newPrice = 520.00;
        Mono<Item> updatedItem = itemRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice); //setting the new price
                    return item;
                })
                .flatMap((item) -> {
                    return itemRepository.save(item); //saving the item with the new price
                });

        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 520.00)
                .verifyComplete();


    }


    @Test
    public void deleteItemById() {

        setUp();
        Mono<Void> deletedItem = itemRepository.findById("ABC") // Mono<Item>
                .map(Item::getId) // get Id -> Transform from one type to another type
                .flatMap((id) -> {
                    return itemRepository.deleteById(id);
                });

        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findAll().log("The new Item List : "))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();


    }

    @Test
    public void deleteItem() {

        setUp();
        Mono<Void> deletedItem = itemRepository.findByDescription("LG TV") // Mono<Item>
                .flatMap((item) -> {
                    return itemRepository.delete(item);
                });

        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findAll().log("The new Item List : "))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();


    }


}
