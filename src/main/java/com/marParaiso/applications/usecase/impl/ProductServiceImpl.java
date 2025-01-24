package com.marParaiso.applications.usecase.impl;

import com.marParaiso.applications.dto.ProductDTO;
import com.marParaiso.applications.ports.ProductRepository;
import com.marParaiso.applications.usecase.ProductService;
import com.marParaiso.domain.entity.Product;
import com.marParaiso.domain.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        return mapToProductDTO(product);
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO.getCategory() == null || productDTO.getCategory().isEmpty()) {
            productDTO.setCategory("default_category");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());

        String imageUrl = productDTO.getImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.out.println("Llamando a assignImageByCategory...");
            imageUrl = assignImageByCategory(productDTO.getCategory());
        }

        System.out.println("imageUrl: " + imageUrl);
        product.setImageUrl(imageUrl); // Se asigna la URL obtenida por categoría
        product.setCategory(productDTO.getCategory());

        return mapToProductDTO(productRepository.save(product));
    }

    private String assignImageByCategory(String category) {
        String imageUrl;
        if (category == null || category.isEmpty()) {
            imageUrl = "/images/default.jpg"; // Ruta local
        } else {
            String categoryLower = category.toLowerCase();
            switch (categoryLower) {
                case "poker":
                    imageUrl = "/images/poker.jpg"; // Ruta local
                    break;
                case "costeña":
                    imageUrl = "/images/costeña.jpg"; // Ruta local
                    break;
                case "aguila":
                    imageUrl = "/images/aguila.jpg"; // Ruta local
                    break;
                case "aguila light":
                    imageUrl = "/images/aguilaLight.jpg";
                    break;
                case "aguardiente":
                    imageUrl = "/images/aguardiente.jpg"; // Ruta local
                    break;
                default:
                    imageUrl = "/images/default.jpg"; // Ruta local
            }
        }
        System.out.println("Image URL: " + imageUrl); // Agrega este log para verificar
        return imageUrl;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        return mapToProductDTO(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateStock(Long productId, int quantity) {
        Product product = getProductEntityById(productId);
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    @Override
    public Product getProductEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
    }

    private ProductDTO mapToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

    public void updateProductImage(Long productId, String imageUrl) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setImageUrl(imageUrl);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Producto no encontrado");
        }
    }

    @Override
    public List<ProductDTO> getFilteredProducts(String search, String category) {
        List<Product> products = productRepository.findAll();

        if (search != null && !search.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (category != null && !category.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getCategory() != null && p.getCategory().toLowerCase().equals(category.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return products.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }
}

