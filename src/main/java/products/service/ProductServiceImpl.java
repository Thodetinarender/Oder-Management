package products.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import products.entitys.Product;
import products.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductByItemCode(String itemCode) {
        return productRepository.findById(itemCode).orElse(null);
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.save(product); // save() can also be used for update if the entity exists
    }

    @Override
    public void deleteProduct(String itemCode) {
        productRepository.deleteById(itemCode);
    }
}
