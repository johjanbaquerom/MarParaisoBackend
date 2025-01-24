package com.marParaiso.applications.usecase;

import com.marParaiso.applications.dto.ProductDTO;
import com.marParaiso.domain.entity.Product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    void updateStock(Long productId, int quantity);
    Product getProductEntityById(Long id);
    void updateProductImage(Long productId, String imageUrl);
    List<ProductDTO> getFilteredProducts(String search, String category);
}
