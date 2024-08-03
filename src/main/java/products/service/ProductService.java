package products.service;

import java.util.List;

import products.entitys.Product;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductByItemCode(String itemCode);
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(String itemCode);
}