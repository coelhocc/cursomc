package com.djaian.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.djaian.cursomc.domain.Categoria;
import com.djaian.cursomc.dto.CategoriaDTO;
import com.djaian.cursomc.services.CategoriaService;

/*
 * Controlador REST
 **/

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		
		Categoria obj = service.find(id);
		
		return ResponseEntity.ok(obj);
		
		/*
		Categoria cat1 = new Categoria(1, "Informática");
		Categoria cat2 = new Categoria(2, "Escritório");
		
		List<Categoria> lista = new ArrayList<>();
		lista.add(cat1);
		lista.add(cat2);
		
		return lista;
		*/
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO){
		Categoria obj = service.fromDTO(objDTO);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDTO, @PathVariable Integer id){
		Categoria obj = service.fromDTO(objDTO);
		obj.setId(id);
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}


	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		
		List<Categoria> list = service.findAll();
		List<CategoriaDTO> listDTO = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList()); //Percorre a lista efetuando uma operação para cada elemento da lista
		
		return ResponseEntity.ok().body(listDTO);
	}

	@RequestMapping(value="/page", method=RequestMethod.GET)
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(name="page", defaultValue="0") Integer page, 
			@RequestParam(name="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(name="orderby", defaultValue="nome") String orderby, 
			@RequestParam(name="direction", defaultValue="ASC") String direction) {
		
		Page<Categoria> list = service.findPage(page, linesPerPage, orderby, direction);
		Page<CategoriaDTO> listDTO = list.map(obj -> new CategoriaDTO(obj)); //Percorre a lista efetuando uma operação para cada elemento da lista
		
		return ResponseEntity.ok().body(listDTO);
	}
	
}
