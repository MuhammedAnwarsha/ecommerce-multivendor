package com.ecommerce.repository;

import com.ecommerce.modal.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
