package com.djaian.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SMTPEmailService extends AbstractEmailService {

	private static final Logger LOG = LoggerFactory.getLogger(SMTPEmailService.class);
	
	@Autowired
	private MailSender MailSender;
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando envio de email...");
		MailSender.send(msg);
		LOG.info("Email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando envio de email...");
		javaMailSender.send(msg);
		LOG.info("Email enviado");
		
	}

}
