package com.addressbook;

import com.addressbook.model.JVMState;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import java.time.Duration;
import java.util.stream.Stream;

@Service
public class JVMStateService {
    public Flux<JVMState> getJVMState() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<JVMState> jvmStateFlux = Flux.fromStream(Stream.generate(JVMState::new));
        return Flux.zip(interval, jvmStateFlux).map(Tuple2::getT2);
    }
}
