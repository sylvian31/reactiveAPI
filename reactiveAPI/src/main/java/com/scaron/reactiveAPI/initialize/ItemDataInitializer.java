package com.scaron.reactiveAPI.initialize;

import com.scaron.reactiveAPI.document.Item;
import com.scaron.reactiveAPI.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
@Slf4j
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemRepository itemRepository;


    @Override
    public void run(String... args) throws Exception {
        initalDataSetUp();
    }


    public List<Item> data() {

        return Arrays.asList(new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 329.99),
                new Item(null, "Apple Watch", 349.99),
                new Item("ABC", "Beats HeadPhones", 149.99));
    }


    private void initalDataSetUp() {

        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemRepository::save)
                .thenMany(itemRepository.findAll())
                .subscribe((item -> {
                    System.out.println("Item inserted from CommandLineRunner : " + item);
                }));

    }
}
