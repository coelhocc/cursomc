package com.djaian.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.djaian.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}
