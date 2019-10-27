package com.scaron.reactiveAPI.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "azerty", "monopolie")
//                .concatWith(Flux.error(new RuntimeException("oulalalala")))
                .log();
        stringFlux.subscribe(System.out::println, System.err::println, () -> System.out.println("Completed"));
    }


    @Test
    public void fluxTestElements_WithoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "azerty", "monopolie")
                .log();

        //l'ordre est important sinon il y a une erreur
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("azerty")
                .expectNext("monopolie")
                .verifyComplete();
    }


    @Test
    public void fluxTestElements_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "azerty", "monopolie")
                .concatWith(Flux.error(new RuntimeException("oulalalala")))
                .log();

        //l'ordre est important sinon il y a une erreur
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("azerty")
                .expectNext("monopolie")
//                .expectError(RuntimeException.class)
                .expectErrorMessage("oulalalala")
                .verify();
    }


    @Test
    public void fluxTestElementsCount_WithError() {
        Flux<String> stringFlux = Flux.just("Spring", "azerty", "monopolie")
                .concatWith(Flux.error(new RuntimeException("oulalalala")))
                .log();

        //l'ordre est important sinon il y a une erreur
        StepVerifier.create(stringFlux)
                .expectNextCount(3)
//                .expectError(RuntimeException.class)
                .expectErrorMessage("oulalalala")
                .verify();
    }

    @Test
    public void fluxTestElements_WithError1() {
        Flux<String> stringFlux = Flux.just("Spring", "azerty", "monopolie")
                .concatWith(Flux.error(new RuntimeException("oulalalala")))
                .log();

        //l'ordre est important sinon il y a une erreur
        StepVerifier.create(stringFlux)
                .expectNext("Spring", "azerty", "monopolie")
//                .expectError(RuntimeException.class)
                .expectErrorMessage("oulalalala")
                .verify();
    }


    @Test
    public void MonoTest(){
        Mono<String> stringMono = Mono.just("salut");

        StepVerifier.create(stringMono)
                .expectNext("salut")
                .verifyComplete();
    }

    @Test
    public void MonoTest_Error(){

        StepVerifier.create(Mono.error(new RuntimeException("chut !")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
