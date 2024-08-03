package products.service;

import java.util.List;

import products.entitys.Order;


public interface OrderService {
	
    public Order saveOrder(Order order);

    public List<Order> getAllOrders();
    
    public Order getOrderById(Long orderId);
    
    public void deleteOrderById(Long orderId);
    
   
    

}

