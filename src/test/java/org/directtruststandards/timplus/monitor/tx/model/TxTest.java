package org.directtruststandards.timplus.monitor.tx.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TxTest
{
	@Test
	public void testAddRecipient_emptyRecipeints_assertSingleRecipient()
	{
		final Tx tx = new Tx();
		
		assertNull(tx.getDetail(TxDetailType.RECIPIENTS));
		
		tx.addRecipient("me@domain.com");
		
		final TxDetail detail = tx.getDetail(TxDetailType.RECIPIENTS);
		assertEquals("me@domain.com", detail.getDetailValue());
	}
	
	@Test
	public void testAddRecipient_singleRecip_assertMultipleRecipients()
	{
		final Map<TxDetailType, TxDetail> details = new HashMap<>();
		details.put(TxDetailType.RECIPIENTS, new TxDetail(TxDetailType.RECIPIENTS, "me@domain.com"));
		final Tx tx = new Tx(TxStanzaType.MESSAGE, details);
		
		
		assertNotNull(tx.getDetail(TxDetailType.RECIPIENTS));
		
		tx.addRecipient("you@domain.com");
		
		final TxDetail detail = tx.getDetail(TxDetailType.RECIPIENTS);
		assertEquals("me@domain.com,you@domain.com", detail.getDetailValue());
	}
	
	@Test
	public void testAddRecipient_blankRecipeints_assertSingleRecipient()
	{
		final Map<TxDetailType, TxDetail> details = new HashMap<>();
		details.put(TxDetailType.RECIPIENTS, new TxDetail(TxDetailType.RECIPIENTS, ""));
		final Tx tx = new Tx(TxStanzaType.MESSAGE, details);
		
		
		assertNotNull(tx.getDetail(TxDetailType.RECIPIENTS));
		
		tx.addRecipient("me@domain.com");
		
		final TxDetail detail = tx.getDetail(TxDetailType.RECIPIENTS);
		assertEquals("me@domain.com", detail.getDetailValue());
	}
}
