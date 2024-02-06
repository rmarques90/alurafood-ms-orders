package br.com.alurafood.pedidos.service;

import br.com.alurafood.pedidos.dto.OrderDTO;
import br.com.alurafood.pedidos.dto.StatusDto;
import br.com.alurafood.pedidos.model.Order;
import br.com.alurafood.pedidos.model.Status;
import br.com.alurafood.pedidos.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private final ModelMapper modelMapper;


    public List<OrderDTO> getAll() {
        return repository.findAll().stream()
                .map(p -> modelMapper.map(p, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public OrderDTO getById(Long id) {
        Order pedido = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(pedido, OrderDTO.class);
    }

    public OrderDTO createOrder(OrderDTO dto) {
        Order order = modelMapper.map(dto, Order.class);

        order.setDateTime(LocalDateTime.now());
        order.setStatus(Status.REALIZED);
        order.getItems().forEach(item -> item.setOrder(order));
        Order salvo = repository.save(order);

        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO updateStatus(Long id, StatusDto dto) {

        Order order = repository.byIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(dto.getStatus());
        repository.updateStatus(dto.getStatus(), order);
        return modelMapper.map(order, OrderDTO.class);
    }

    public void approveOrderPayment(Long id) {

        Order order = repository.byIdWithItems(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(Status.PAYED);
        repository.updateStatus(Status.PAYED, order);
    }
}
