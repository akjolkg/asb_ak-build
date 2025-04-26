package org.asb.util;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/****
 * 
 * @author Akzholbek Omorov
 *
 */

public class MailSender {
	
	private ExecutorService service = null;
	private static MailSender sender = null; 
	
	private MailSender() {
		service = Executors.newFixedThreadPool(5);
	}
	
	public synchronized static MailSender getInstance() {
		if(sender == null){
			sender = new MailSender();
		}
		return sender;
	}
	
	public synchronized static void destroy() {
		if(sender != null) sender.doDestroy();
	}
	
	public boolean send(org.asb.beans.Message m) {
		
		try {
			  Properties props = new Properties();
		      props.put("mail.smtps.auth", "false");

		      Session mailSession = Session.getDefaultInstance(props, null);
		      Transport transport = mailSession.getTransport("smtp");

		      MimeMessage message = new MimeMessage(mailSession);
		      message.setSubject(m.getSubject());
		      message.setContent(m.getContent(), "text/html; charset=UTF-8"); // html text
		      
		      message.addRecipient(Message.RecipientType.TO,
		           new InternetAddress(m.getEmail()));
		      message.addFrom(new InternetAddress[]{new InternetAddress("admin@zakupki.gov.kg")});
		      
		      message.setSender(new InternetAddress("admin@zakupki.gov.kg"));
		      
		      transport.connect("mail.zakupki.gov.kg", "admin@zakupki.gov.kg", "QWazxs123");
		      transport.sendMessage(message,
		      message.getRecipients(Message.RecipientType.TO));
		      transport.close();
		     
		      return true;
		      
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
		
	}
	
	
	
	public void asyncSend(final org.asb.beans.Message m) {
		service.submit(new Runnable() {
			public void run() {
				send(m);
			}
		});
	}
	
	public void doDestroy() {
		service.shutdown();
		sender = null;
	}
}