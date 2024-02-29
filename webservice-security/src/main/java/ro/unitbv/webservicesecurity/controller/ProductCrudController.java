package ro.unitbv.webservicesecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ro.unitbv.webservicesecurity.dto.ProductRequestDto;
import ro.unitbv.webservicesecurity.dto.ProductResponse;
import ro.unitbv.webservicesecurity.service.ProductCrudService;

@RestController
public class ProductCrudController {

    @Autowired
    private ProductCrudService productCrudService;

    @PostMapping("/product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto){
        var productResponse = productCrudService.createProduct(productRequestDto.name(),productRequestDto.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> validationExceptionHandler(MethodArgumentNotValidException ex){
        return ResponseEntity.badRequest().body("Requested body is not valid");
    }
}
