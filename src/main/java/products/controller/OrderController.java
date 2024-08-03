package products.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import products.entitys.Order;
import products.entitys.OrderItem;
import products.entitys.OrderRequest;
import products.entitys.Product;
import products.repository.ProductRepository;
import products.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/list")
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();

        /*This code iterates over a collection of orders, 
         * calculating the total quantity and total price for each order based on its items. 
         * The price is computed using a fixed unit price of 10 for each item. 
         * After ensuring the total price is valid, it sets these calculated values on the order object.
         */
        
        // Calculate total quantity and total price for each order
        orders.forEach(order -> {
            int totalQuantity = order.getOrderItems().stream()
                    .mapToInt(OrderItem::getQuantity)
                    .sum();
            double totalPrice = order.getOrderItems().stream()
                    .mapToDouble(item -> item.getQuantity() * 10) // Assuming unit price is fixed at 10
                    .sum();

            // Ensure totalPrice is not null before setting
            if (!Double.isNaN(totalPrice)) {
                order.setTotalQuantity(totalQuantity);
                order.setTotalPrice(totalPrice);
            }
        });

        model.addAttribute("orders", orders);
        return "orderList"; // Create an HTML template named orderList.html to display orders
    }
    
	 @GetMapping("/addItems")
     public String addItems(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "addItems";
    }



    @PostMapping("/submitOrder")
    public ResponseEntity<String> submitOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = new Order();
            order.setUsername(orderRequest.getUsername());
            List<OrderItem> orderItems = orderRequest.getProducts().stream()
                    .map(product -> {
                        OrderItem item = new OrderItem();
                        item.setItemCode(product.getItemCode());
                        item.setDescription(product.getDescription());
                        item.setQuantity(product.getQty());
                        return item;
                    })
                    .collect(Collectors.toList());
            order.setOrderItems(orderItems);
            orderService.saveOrder(order);
            return ResponseEntity.ok("Order saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save order: " + e.getMessage()) ;
        }
    } 

  

  
    @GetMapping("/editOrder/{orderId}")
    public String editOrder(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "editOrder";
    }

    @PostMapping("/editOrder")
    public String saveEditedOrder(@ModelAttribute Order order) {
        Order existingOrder = orderService.getOrderById(order.getId());
        if (existingOrder != null) {
            existingOrder.setUsername(order.getUsername());
            existingOrder.setOrderItems(order.getOrderItems());
            existingOrder.setTotalQuantity(order.getOrderItems().stream().mapToInt(OrderItem::getQuantity).sum());
            existingOrder.setTotalPrice(order.getOrderItems().stream().mapToDouble(item -> item.getQuantity() * 10).sum()); // Assuming unit price is fixed at 10
            orderService.saveOrder(existingOrder);
        }
        return "redirect:/orders/list";
    }



    
    
    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable Long orderId, Model model) {
        // Delete order by ID
        orderService.deleteOrderById(orderId);
        
        // Redirect to order list page after deletion
        return "redirect:/orders/list";
    }
}
