package products.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import products.entitys.Order;
import products.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    public Order saveOrder(Order order) {
    	order.setSaveDate(LocalDate.now());
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }
    
    public void deleteOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
    
   
    

}

