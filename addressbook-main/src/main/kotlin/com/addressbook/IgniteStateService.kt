package com.addressbook

import com.addressbook.model.IgniteMetricsContainer
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.util.function.Tuple2

import java.time.Duration
import java.util.stream.Stream

@Service
class IgniteStateService {
    fun getIgniteState(): Flux<IgniteMetricsContainer> {
        val interval = Flux.interval(Duration.ofSeconds(1))
        val igniteMetricsContainerFlux = Flux.fromStream(Stream.generate(::IgniteMetricsContainer))
        return Flux.zip(interval, igniteMetricsContainerFlux).map(Tuple2<Long, IgniteMetricsContainer>::getT2)
    }
}
