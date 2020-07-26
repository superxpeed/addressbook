package com.addressbook.services

import com.addressbook.ignite.IgniteMetricsContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.util.function.Tuple2

import java.time.Duration
import java.util.stream.Stream

@Service
class IgniteStateService {

    @Autowired
    lateinit var igniteMetricsContainer: IgniteMetricsContainer

    fun getIgniteState(): Flux<IgniteMetricsContainer> {
        val interval = Flux.interval(Duration.ofSeconds(1))
        val igniteMetricsContainerFlux = Flux.fromStream(Stream.generate(igniteMetricsContainer::refresh))
        return Flux.zip(interval, igniteMetricsContainerFlux).map(Tuple2<Long, IgniteMetricsContainer>::getT2)
    }
}
