package com.djaian.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.djaian.cursomc.domain.Cliente;
import com.djaian.cursomc.repositories.ClienteRepository;
import com.djaian.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private BCryptPasswordEncoder pe;
	@Autowired
	private EmailService emailService;
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente==null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for (int i = 0; i < vet.length; i++) {
			vet[i] = randonChar();
		}
		return new String(vet);
	}

	private char randonChar() {
		int opt = rand.nextInt(3);
		
		switch (opt) {
			case 0: //Gera Dígito
				return (char) (rand.nextInt(10)+48);
	
			case 1: //Gera letra maiúscula
				return (char) (rand.nextInt(26)+65);
	
			default: //Gera letra minúscula
				return (char) (rand.nextInt(26)+97);
		}
	}
}
