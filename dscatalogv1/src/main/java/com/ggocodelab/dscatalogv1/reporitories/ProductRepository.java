package com.ggocodelab.dscatalogv1.reporitories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ggocodelab.dscatalogv1.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
