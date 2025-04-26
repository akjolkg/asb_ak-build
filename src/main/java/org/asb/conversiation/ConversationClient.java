package org.asb.conversiation;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import org.asb.annotation.Logged;
import org.asb.model.Client;
import org.asb.model.Payment;

/**
 * 
 * @author Akzholbek Omorov
 *
 */
@Logged
@Named
@ConversationScoped
public class ConversationClient extends Conversational {
	
	private static final long serialVersionUID = -6100072166946495229L;
	
	private Payment payment;
	private Client client;
	private String comeBackUrl;
	private Payment lastPayment;

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getComeBackUrl() {
		return comeBackUrl;
	}

	public void setComeBackUrl(String comeBackUrl) {
		this.comeBackUrl = comeBackUrl;
	}

	public Payment getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(Payment lastPayment) {
		this.lastPayment = lastPayment;
	}
	
}
