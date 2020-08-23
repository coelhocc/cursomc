package com.djaian.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.djaian.cursomc.domain.Estado;
import com.djaian.cursomc.repositories.EstadoRepository;
import com.djaian.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class EstadoService {

	@Autowired
	EstadoRepository repo;

	public Estado find(Integer id) {
		
		Optional<Estado> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado ID: "+id
				+", Tipo: "+Estado.class.getName()));
		
	}

	public List<Estado> findAll(){
		return repo.findAllByOrderByNome();
	}
	
}
