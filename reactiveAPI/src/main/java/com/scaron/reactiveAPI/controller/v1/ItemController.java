package com.scaron.reactiveAPI.controller.v1;

import com.scaron.reactiveAPI.document.Item;
import com.scaron.reactiveAPI.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.scaron.reactiveAPI.constants.ItemConstants.ITEM_END_POINT_V1;

@RestController
@Slf4j
public class ItemController {

       /* @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
        log.error("Exception caught in handleRuntimeException :  {} " , ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }*/

    @Autowired
    ItemRepository itemRepository;


    @GetMapping(ITEM_END_POINT_V1)
    public Flux<Item> getAllItems() {
        log.info("salut");
        return itemRepository.findAll();

    }

    @GetMapping(ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id) {

        return itemRepository.findById(id)
                .map((item) -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping(ITEM_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {

        return itemRepository.save(item);


    }

    @DeleteMapping(ITEM_END_POINT_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {

        return itemRepository.deleteById(id);


    }

    @GetMapping(ITEM_END_POINT_V1 + "/runtimeException")
    public Flux<Item> runtimeException() {

        return itemRepository.findAll()
                .concatWith(Mono.error(new RuntimeException("RuntimeException Occurred.")));
    }

    //id and item to be updated in the req = path variable and request body - completed
    // using the id get the item from database - completed
    // updated the item retrieved with the value from the request body - completed
    // save the item - completed
    //return the saved item - completed
    @PutMapping(ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id,
                                                 @RequestBody Item item) {

        return itemRepository.findById(id)
                .flatMap(currentItem -> {

                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemRepository.save(currentItem);
                })
                .map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
