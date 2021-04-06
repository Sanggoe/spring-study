package org.springframework.samples.petclinic.proxy;

import org.junit.jupiter.api.Test;

class StoreTest {
	@Test
	public void testPay() {
		Payment cashPerf = new CashPerf();
		Store store = new Store(cashPerf);
		store.buySomthing(100);
	}
}
