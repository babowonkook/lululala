package com.wakuang.hehe.pingtai;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class SearchBithumbPriceTest extends BaseJunitTest {

	@Autowired 
	@Qualifier("bithumbService")
	private SearchPingtaiPrice bithumbService;
	@Test
	public void testGetTakerFee() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDepositFee() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPrice() throws Exception {
		bithumbService.getPrice();
		fail("Not yet implemented");
	}

	@Test
	public void testGetPriceByCoin() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBalance() throws Exception {
		
		fail("Not yet implemented");
	}

}
