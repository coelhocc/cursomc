package com.djaian.cursomc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.djaian.cursomc.domain.Cliente;
import com.djaian.cursomc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{

	@Transactional(readOnly=true)//Para redução de lock
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}
