package com.djaian.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SMTPEmailService extends AbstractEmailService {

	private static final Logger LOG = LoggerFactory.getLogger(SMTPEmailService.class);
	
	@Autowired
	private MailSender MailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {

		LOG.info("Enviando envio de email...");
		MailSender.send(msg);
		LOG.info("Email enviado");
	}

}
