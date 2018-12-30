package com.webapp;

import com.webapp.model.IgniteMetricsContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.stream.Stream;

@Service
public class IgniteStateService {
    public Flux<IgniteMetricsContainer> getIgniteState() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<IgniteMetricsContainer> igniteMetricsContainerFlux = Flux.fromStream(Stream.generate(IgniteMetricsContainer::new));
        return Flux.zip(interval, igniteMetricsContainerFlux).map(Tuple2::getT2);
    }
}
