package com.rebeca.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rebeca.springboot.model.Telefone;

@Repository
@Transactional
public interface TelefoneRepository extends CrudRepository<Telefone, Long> {
	


}