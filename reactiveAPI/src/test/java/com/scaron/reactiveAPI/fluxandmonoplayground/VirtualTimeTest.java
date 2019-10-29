package com.scaron.reactiveAPI.fluxandmonoplayground;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

@DirtiesContext
@Ignore
public class VirtualTimeTest {

    @Test
    public void testingWithoutVirtualTime(){

        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.create(longFlux.log())
                .expectSubscription()
                .expectNext(0l,1l,2l)
                .verifyComplete();
    }

    @Test
    public void testingWithVirtualTime(){

        VirtualTimeScheduler.getOrSet();

        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.withVirtualTime(() -> longFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0l,1l,2l)
                .verifyComplete();

    }
}
