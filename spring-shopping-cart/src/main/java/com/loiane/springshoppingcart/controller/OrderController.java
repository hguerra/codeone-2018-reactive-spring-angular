package com.loiane.springshoppingcart.controller;

import com.loiane.springshoppingcart.model.Order;
import com.loiane.springshoppingcart.model.OrderEvent;
import com.loiane.springshoppingcart.repository.OrderRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import javax.print.attribute.standard.Media;
import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import static java.time.Duration.ofSeconds;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderRepository repository;

    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Order> streamOrderStatus() {
        return repository.findAll().delayElements(Duration.ofSeconds(7));
        /* return repository.findAll()
                .flatMap(record -> {
                    Flux<Long> interval = Flux.interval(ofSeconds(3));
                    Flux<OrderEvent> events = Flux.fromStream(
                            Stream.generate(() -> new OrderEvent(record, new Date()))
                    );
                    return Flux.zip(interval, events).map(Tuple2::getT2);
                }); */
    }
}
