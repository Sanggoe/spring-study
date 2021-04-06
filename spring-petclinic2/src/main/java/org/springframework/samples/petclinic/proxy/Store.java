package org.springframework.samples.petclinic.proxy;

import org.springframework.beans.factory.annotation.Autowired;

public class Store {
	Payment payment;
	@Autowired
	public Store(Payment payment) {
		this.payment = payment;
	}
	public void buySomthing(int amount) {
		payment.pay(amount);
	}
}
