package com.djaian.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.djaian.cursomc.security.UserSS;

public class UserService {

	public static UserSS authenticated() {
		try {
			//Retorna o usuário que está logado no sistema
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
}
