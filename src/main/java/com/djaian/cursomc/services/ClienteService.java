package com.djaian.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.djaian.cursomc.domain.Cidade;
import com.djaian.cursomc.domain.Cliente;
import com.djaian.cursomc.domain.Endereco;
import com.djaian.cursomc.domain.enums.Perfil;
import com.djaian.cursomc.domain.enums.TipoCliente;
import com.djaian.cursomc.dto.ClienteDTO;
import com.djaian.cursomc.dto.ClienteNewDTO;
import com.djaian.cursomc.repositories.ClienteRepository;
import com.djaian.cursomc.repositories.EnderecoRepository;
import com.djaian.cursomc.security.UserSS;
import com.djaian.cursomc.services.exceptions.AuthorizationException;
import com.djaian.cursomc.services.exceptions.DataIntegrityException;
import com.djaian.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado ID: "+id
				+", Tipo: "+Cliente.class.getName()));
		
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newobj = find(obj.getId());
		updateData(newobj, obj);
		return repo.save(newobj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderby, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderby);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	private void updateData(Cliente newobj, Cliente obj) {
		newobj.setNome(obj.getNome());
		newobj.setEmail(obj.getEmail());
	}

	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfcnpj(), TipoCliente.toEnum(objDTO.getTipo()), pe.encode(objDTO.getSenha()));
		Cidade cid  = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().addAll(Arrays.asList(end));
		cli.getTelefones().addAll(Arrays.asList(objDTO.getTelefone1()));
		
		if(objDTO.getTelefone2() != null)
			cli.getTelefones().addAll(Arrays.asList(objDTO.getTelefone2()));

		if(objDTO.getTelefone3() != null)
			cli.getTelefones().addAll(Arrays.asList(objDTO.getTelefone3()));
		return cli;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		
		/*
		URI uri = s3Service.uploadFile(multipartFile);
		
		Optional<Cliente> cli = repo.findById(user.getId());
		cli.orElse(null).setImageUrl(uri.toString());
		repo.save(cli.orElse(null));
		
		return uri;
		*/
	}
	
}
