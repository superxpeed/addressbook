package com.addressbook.services

import com.addressbook.dto.JvmMetricsDto
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.util.function.Tuple2

import java.time.Duration
import java.util.stream.Stream

@Service
class JVMStateService {
    fun getJVMState(): Flux<JvmMetricsDto> {
        return Flux.zip(Flux.interval(Duration.ofSeconds(1)),
                Flux.fromStream(Stream.generate(::JvmMetricsDto))).map(Tuple2<Long, JvmMetricsDto>::getT2)
    }
}
