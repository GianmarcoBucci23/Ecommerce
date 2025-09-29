package com.ecommerce.project.controller;

import com.ecommerce.project.configuration.AppConstans;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @PathVariable Long categoryId,@RequestBody ProductDTO productDTO){

        ProductDTO saveProductDTO = productService.addProduct(categoryId,productDTO);

        return new ResponseEntity<>(saveProductDTO,HttpStatus.CREATED);
    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name="pageNumber",defaultValue = AppConstans.PAGE_NUMBER) Integer pageNumber,
                                                          @RequestParam(name="pageSize",defaultValue = AppConstans.PAGE_SIZE) Integer pageSize,
                                                          @RequestParam(name="sortBy",defaultValue = AppConstans.SORT_PRODUCTS_BY) String sortBy,
                                                          @RequestParam(name="sortOrder", defaultValue = AppConstans.SORT_ORDER) String sortOrder){

        ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(productResponse,HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name="pageNumber",defaultValue = AppConstans.PAGE_NUMBER) Integer pageNumber,
                                                                 @RequestParam(name="pageSize",defaultValue = AppConstans.PAGE_SIZE) Integer pageSize,
                                                                 @RequestParam(name="sortBy",defaultValue = AppConstans.SORT_PRODUCTS_BY) String sortBy,
                                                                 @RequestParam(name="sortOrder", defaultValue = AppConstans.SORT_ORDER) String sortOrder){

        ProductResponse productResponse = productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name="pageNumber",defaultValue = AppConstans.PAGE_NUMBER) Integer pageNumber,
                                                                @RequestParam(name="pageSize",defaultValue = AppConstans.PAGE_SIZE) Integer pageSize,
                                                                @RequestParam(name="sortBy",defaultValue = AppConstans.SORT_PRODUCTS_BY) String sortBy,
                                                                @RequestParam(name="sortOrder", defaultValue = AppConstans.SORT_ORDER) String sortOrder){

        ProductResponse productResponse = productService.getProductsByProductName(keyword,pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO , @PathVariable Long productId){

        ProductDTO updateProductDTO = productService.updateProduct(productDTO,productId);

        return new ResponseEntity<>(updateProductDTO,HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){

        ProductDTO productDTO = productService.deleteProduct(productId);

        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("Image") MultipartFile image) throws IOException {

        ProductDTO productDTO = productService.updateProductImage(productId,image);

        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

}
