package ro.unitbv.webservicesecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ro.unitbv.webservicesecurity.dto.ProductRequestDto;
import ro.unitbv.webservicesecurity.dto.ProductResponse;
import ro.unitbv.webservicesecurity.service.ProductCrudService;

@RestController
public class ProductCrudController {

    @Autowired
    private ProductCrudService productCrudService;

    @PostMapping("/product")
    public ResponseEntity<ProductResponse> createProduct(ProductRequestDto productRequestDto){
        var productResponse = productCrudService.createProduct(productRequestDto.name(),productRequestDto.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }
}
