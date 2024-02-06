package br.com.alurafood.pedidos.controller;

import br.com.alurafood.pedidos.dto.OrderDTO;
import br.com.alurafood.pedidos.dto.StatusDto;
import br.com.alurafood.pedidos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

        @Autowired
        private OrderService service;

        @GetMapping()
        public List<OrderDTO> listAll() {
            return service.getAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrderDTO> listById(@PathVariable @NotNull Long id) {
            OrderDTO dto = service.getById(id);

            return  ResponseEntity.ok(dto);
        }

        @PostMapping()
        public ResponseEntity<OrderDTO> makeOrder(@RequestBody @Valid OrderDTO dto, UriComponentsBuilder uriBuilder) {
            OrderDTO pedidoRealizado = service.createOrder(dto);

            URI address = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.getId()).toUri();

            return ResponseEntity.created(address).body(pedidoRealizado);

        }

        @PutMapping("/{id}/status")
        public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestBody StatusDto status){
           OrderDTO dto = service.updateStatus(id, status);

            return ResponseEntity.ok(dto);
        }


        @PutMapping("/{id}/payment")
        public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
            service.approveOrderPayment(id);

            return ResponseEntity.ok().build();

        }
}
