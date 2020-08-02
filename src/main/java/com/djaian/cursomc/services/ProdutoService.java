package com.djaian.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.djaian.cursomc.domain.Categoria;
import com.djaian.cursomc.domain.Produto;
import com.djaian.cursomc.repositories.CategoriaRepository;
import com.djaian.cursomc.repositories.ProdutoRepository;
import com.djaian.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		
		Optional<Produto> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado ID: "+id
				+", Tipo: "+Produto.class.getName()));
		
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderby, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderby);
		
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		
		//return repo.search(nome, categorias, pageRequest);
		//ou
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
		
	}

}
