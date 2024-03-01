package ro.unitbv.webservicesecurity.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ro.unitbv.webservicesecurity.domain.model.Product;
import ro.unitbv.webservicesecurity.dto.ProductResponse;

@Service
public class ProductCrudService {

    private static final Map<String, Product> IN_MEMORY_PRODUCT_STORE = new HashMap<>();

    public ProductResponse createProduct(String name, String description) {
        var product = new Product(UUID.randomUUID(),name,description);
        IN_MEMORY_PRODUCT_STORE.put(product.id().toString(), product);
        return new ProductResponse(product.id().toString(), product.name(),product.description());
    }

    public Optional<ProductResponse> getProduct(String id){
        var product = IN_MEMORY_PRODUCT_STORE.get(id);
        if(product != null){
            return Optional.of(new ProductResponse(product.id().toString(),product.name(), product.description()));
        }
        return Optional.empty();
    }
}
