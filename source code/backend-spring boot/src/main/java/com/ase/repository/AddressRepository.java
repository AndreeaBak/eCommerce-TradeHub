package com.ase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ase.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
