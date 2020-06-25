package org.directtruststandards.timplus.monitor.tx.impl;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.directtruststandards.timplus.monitor.impl.DefaultTxParser;
import org.directtruststandards.timplus.monitor.tx.TxParser;
import org.directtruststandards.timplus.monitor.tx.model.Tx;
import org.directtruststandards.timplus.monitor.tx.model.TxDetail;
import org.directtruststandards.timplus.monitor.tx.model.TxDetailType;
import org.directtruststandards.timplus.monitor.tx.model.TxStanzaType;
import org.jivesoftware.smackx.amp.AMPDeliverCondition;
import org.junit.Test;

public class DefaultTxParserTest
{
	protected TxParser parser = new DefaultTxParser();
	
	@Test
	public void testParseMessageStanza() throws Exception
	{
		final String stanza = IOUtils.resourceToString("/messages/plainMessageStanza.txt", Charset.defaultCharset());
		
		final Tx tx = parser.parseStanza(stanza);
		
		assertEquals(TxStanzaType.MESSAGE, tx.getStanzaType());
		
		final Map<TxDetailType, TxDetail> details = tx.getDetails();
		
		assertEquals("sl3nx51f", details.get(TxDetailType.MSG_ID).getDetailValue());
		assertEquals("juliet@example.com", details.get(TxDetailType.RECIPIENTS).getDetailValue());
		assertEquals("romeo@example.net", details.get(TxDetailType.FROM).getDetailValue());	
		assertEquals("chat", details.get(TxDetailType.TYPE).getDetailValue());	
	}
	
	
	@Test
	public void testParseAMPDeliveredStanza() throws Exception
	{
		final String stanza = IOUtils.resourceToString("/messages/ampDeliveredStanza.txt", Charset.defaultCharset());
		
		final Tx tx = parser.parseStanza(stanza);
		
		assertEquals(TxStanzaType.AMP, tx.getStanzaType());
		
		final Map<TxDetailType, TxDetail> details = tx.getDetails();
		
		assertEquals("chatty2", details.get(TxDetailType.MSG_ID).getDetailValue());
		assertEquals("bernardo@hamlet.lit", details.get(TxDetailType.RECIPIENTS).getDetailValue());
		assertEquals("hamlet.lit", details.get(TxDetailType.FROM).getDetailValue());
		assertEquals(AMPDeliverCondition.Value.direct.name(), details.get(TxDetailType.AMP_CONDITION_VALUE).getDetailValue());
		assertEquals("francisco@hamlet.lit", details.get(TxDetailType.ORIGINAL_RECIPIENT).getDetailValue());	
	}
	
	@Test
	public void testParseMessageErrorStanza() throws Exception
	{
		final String stanza = IOUtils.resourceToString("/messages/serviceUnavailableStanza.txt", Charset.defaultCharset());
		
		final Tx tx = parser.parseStanza(stanza);
		
		assertEquals(TxStanzaType.MESSAGE_ERROR, tx.getStanzaType());
		
		final Map<TxDetailType, TxDetail> details = tx.getDetails();
		
		assertEquals("error1", details.get(TxDetailType.MSG_ID).getDetailValue());
		assertEquals("romeo@example.net", details.get(TxDetailType.RECIPIENTS).getDetailValue());
		assertEquals("juliet@im.example.com", details.get(TxDetailType.FROM).getDetailValue());
		assertEquals("service-unavailable", details.get(TxDetailType.ERROR_CONDITION).getDetailValue());
	}
	
	@Test
	public void testParseIQSetStanza() throws Exception
	{
		final String stanza = IOUtils.resourceToString("/messages/iqRosterSetStanza.txt", Charset.defaultCharset());
		
		final Tx tx = parser.parseStanza(stanza);
		
		assertEquals(TxStanzaType.IQ, tx.getStanzaType());
		
		final Map<TxDetailType, TxDetail> details = tx.getDetails();
		
		assertEquals("ix7s53v2", details.get(TxDetailType.MSG_ID).getDetailValue());
		assertEquals("romeo@example.net", details.get(TxDetailType.RECIPIENTS).getDetailValue());
		assertEquals("juliet@example.com", details.get(TxDetailType.FROM).getDetailValue());
		assertEquals("set", details.get(TxDetailType.TYPE).getDetailValue());	
	}
	
	@Test
	public void testParsSubscribeStanza() throws Exception
	{
		final String stanza = IOUtils.resourceToString("/messages/presenceSubscribeStanza.txt", Charset.defaultCharset());
		
		final Tx tx = parser.parseStanza(stanza);
		
		assertEquals(TxStanzaType.PRESENSE, tx.getStanzaType());
		
		final Map<TxDetailType, TxDetail> details = tx.getDetails();
		
		assertEquals("xk3h1v69", details.get(TxDetailType.MSG_ID).getDetailValue());
		assertEquals("juliet@example.com", details.get(TxDetailType.RECIPIENTS).getDetailValue());
		assertEquals("romeo@example.net", details.get(TxDetailType.FROM).getDetailValue());
		assertEquals("subscribe", details.get(TxDetailType.TYPE).getDetailValue());	
	}
}
