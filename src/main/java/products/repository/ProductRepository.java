package products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import products.entitys.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // Define custom query methods if needed
}