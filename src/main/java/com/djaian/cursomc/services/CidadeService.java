package com.djaian.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.djaian.cursomc.domain.Cidade;
import com.djaian.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	CidadeRepository repo;

	public List<Cidade> findByEstado(Integer estadoId){
		return repo.findCidades(estadoId);
	}
}
