package com.scaron.reactiveAPI.fluxandmonoplayground;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DirtiesContext
@Ignore
public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("adam","anna","jack","jenny");

    @Test
    public void filterTest(){

        Flux<String> namesFlux = Flux.fromIterable(names) // adam, anna, jack,jenny
                .filter(s->s.startsWith("a"))
                .log(); //adam,anna

        StepVerifier.create(namesFlux)
                .expectNext("adam","anna")
                .verifyComplete();

    }

    @Test
    public void filterTestLength(){

        Flux<String> namesFlux = Flux.fromIterable(names) // adam, anna, jack,jenny
                .filter(s->s.length() >4)
                .log(); //jenny

        StepVerifier.create(namesFlux)
                .expectNext("jenny")
                .verifyComplete();

    }
}
