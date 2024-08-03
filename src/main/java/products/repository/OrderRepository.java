package products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import products.entitys.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

