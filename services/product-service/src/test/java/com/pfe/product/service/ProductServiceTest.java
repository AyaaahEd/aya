package com.pfe.product.service;

import com.pfe.product.domain.Product;
import com.pfe.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        Product product = new Product("T-Shirt", "Cotton T-Shirt", new BigDecimal("19.99"), true, "Apparel");
        Mockito.when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> (Product) invocation.getArgument(0));

        // Act
        Product created = productService.createProduct(product);

        // Assert
        Assertions.assertNotNull(created);
        Assertions.assertEquals("T-Shirt", created.getName());
        Mockito.verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldFindProductsByCategory() {
        // Arrange
        Mockito.when(productRepository.findByCategory("Apparel")).thenReturn(List.of(
                new Product("T-Shirt", "Desc", BigDecimal.TEN, true, "Apparel")));

        // Act
        List<Product> products = productService.getProductsByCategory("Apparel");

        // Assert
        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals("Apparel", products.get(0).getCategory());
    }
}
