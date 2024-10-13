package com.ecommerce.repository;

import com.ecommerce.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    List<Product> findBySellerId(Long id);

    @Query("SELECT p FROM Product p WHERE (:query is null or lower(p.title)" +
            "LIKE lower(concat('%', :query, '%') ))" +
            "or (:query is null or lower(p.category.name)" +
            "LIKE lower(concat('%', :query, '%')))")
    List<Product> searchProduct(@Param("query") String query);
}
