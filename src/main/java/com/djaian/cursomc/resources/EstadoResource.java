package com.djaian.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.djaian.cursomc.domain.Cidade;
import com.djaian.cursomc.domain.Estado;
import com.djaian.cursomc.dto.CidadeDTO;
import com.djaian.cursomc.dto.EstadoDTO;
import com.djaian.cursomc.services.CidadeService;
import com.djaian.cursomc.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {

	@Autowired
	EstadoService service;
	
	@Autowired
	CidadeService CidadeService;

	@RequestMapping(value="/{estadoId}/cidades", method=RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
		
		List<Cidade> list = CidadeService.findByEstado(estadoId);
		List<CidadeDTO> listDTO = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList()); //Percorre a lista efetuando uma operação para cada elemento da lista
		
		return ResponseEntity.ok().body(listDTO);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll() {
		
		List<Estado> list = service.findAll();
		List<EstadoDTO> listDTO = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList()); //Percorre a lista efetuando uma operação para cada elemento da lista
		
		return ResponseEntity.ok().body(listDTO);
	}

}
