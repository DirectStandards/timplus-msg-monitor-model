package org.directtruststandards.timplus.monitor.impl;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.directtruststandards.timplus.monitor.tx.TxParser;
import org.directtruststandards.timplus.monitor.tx.model.Tx;
import org.directtruststandards.timplus.monitor.tx.model.TxDetail;
import org.directtruststandards.timplus.monitor.tx.model.TxDetailType;
import org.directtruststandards.timplus.monitor.tx.model.TxStanzaType;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.StanzaError;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.amp.AMPDeliverCondition;
import org.jivesoftware.smackx.amp.packet.AMPExtension;
import org.jivesoftware.smackx.amp.packet.AMPExtension.Rule;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the TxParser interface
 * @author Greg Meyer
 * @since 1.0
 */
public class DefaultTxParser implements TxParser
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTxParser.class);
	
	private static final String CLIENT_NAMESPACE = "xmlns='jabber:client'";
	
	private static final String SERVER_NAMESPACE = "xmlns='jabber:server'";
	
	public DefaultTxParser()
	{
		super();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Tx parseStanza(String stanza)
	{
		Tx retVal = null;
		try
		{			
			final Stanza parsedStanza = toStanza(stanza);
			
			retVal = parseStanza(parsedStanza);
		}
		catch (Exception e)
		{
			LOGGER.warn("Failed to retrieve stanza details.", e);
		}
		
		return retVal;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Tx parseStanza(InputStream stream)
	{
		Tx retVal = null;
		try
		{			
			final Stanza parsedStanza = toStanza(stream);
			
			retVal = parseStanza(parsedStanza);
		}
		catch (Exception e)
		{
			LOGGER.warn("Failed to retrieve stanza details.", e);
		}
		
		return retVal;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Tx parseStanza(Stanza stanza)
	{
		final Map<TxDetailType, TxDetail> details = getStanzaDetails(stanza);
		
		final Tx retVal = new Tx();
		retVal.setDetails(details);
		
		// Determine the stanza type
		if (stanza instanceof Message)
		{
			// An error detail indicates a message error
			if (details.get(TxDetailType.ERROR_CONDITION) != null)
				retVal.setStanzaType(TxStanzaType.MESSAGE_ERROR);
			
			// An AMP detail indicates a message AMP
			else if (details.get(TxDetailType.AMP_CONDITION_VALUE) != null)
				retVal.setStanzaType(TxStanzaType.AMP);
			
			// Otherwise the is a normal message
			else
				retVal.setStanzaType(TxStanzaType.MESSAGE);	
		}
		else if (stanza instanceof IQ)
		{
			// An error detail indicates an IQ error
			if (details.get(TxDetailType.ERROR_CONDITION) != null)
				retVal.setStanzaType(TxStanzaType.IQ_ERROR);
			
			// Otherwise the is a normal IQ
			else
				retVal.setStanzaType(TxStanzaType.IQ);	
		}
		else if (stanza instanceof Presence)
		{
			// An error detail indicates a presence error
			if (details.get(TxDetailType.ERROR_CONDITION) != null)
				retVal.setStanzaType(TxStanzaType.PRESENSE_ERROR);
			
			// Otherwise the is a normal presence
			else
				retVal.setStanzaType(TxStanzaType.PRESENSE);	
		}
		else
			// Unknown type
			retVal.setStanzaType(TxStanzaType.UNKNOWN);
		
		return retVal;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Map<TxDetailType, TxDetail> getStanzaDetails(String stanza)
	{
		Map<TxDetailType, TxDetail> retVal = null;
		try
		{			
			final Stanza parsedStanza = toStanza(stanza);
			
			retVal = getStanzaDetails(parsedStanza);
		}
		catch (Exception e)
		{
			LOGGER.warn("Failed to retrieve stanza details.", e);
		}
		
		return retVal;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public Map<TxDetailType, TxDetail> getStanzaDetails(InputStream stream)
	{
		Map<TxDetailType, TxDetail> retVal = null;
		try
		{
			retVal = getStanzaDetails(toStanza(stream));
		}
		catch (Exception e)
		{
			LOGGER.warn("Failed to retrieve stanza details.", e);
		}
		
		return retVal;
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public Map<TxDetailType, TxDetail> getStanzaDetails(Stanza stanza)
	{
		final Map<TxDetailType, TxDetail> retVal = new HashMap<TxDetailType, TxDetail>();
		
		// get the message id
		final String msgId = stanza.getStanzaId();
		if (!StringUtils.isEmpty(msgId))
			retVal.put(TxDetailType.MSG_ID, new TxDetail(TxDetailType.MSG_ID, msgId));
		
		// get the source
		final Jid from = stanza.getFrom();
		if (from != null)
			retVal.put(TxDetailType.FROM, new TxDetail(TxDetailType.FROM, from.asBareJid().toString()));
		
		// get the destination
		final Jid to = stanza.getTo();
		if (to != null)
			retVal.put(TxDetailType.RECIPIENTS, new TxDetail(TxDetailType.RECIPIENTS, to.asBareJid().toString()));
		
		
		// get the error if any
		final StanzaError error = stanza.getError();
		if (error != null)
			retVal.put(TxDetailType.ERROR_CONDITION, new TxDetail(TxDetailType.ERROR_CONDITION, error.getCondition().toString()));
		
		if (stanza instanceof Message)
		{
			final Message msg = Message.class.cast(stanza);
			
			// get the type attribute if it exists
			final Type type = msg.getType();
			if (type != null)
				retVal.put(TxDetailType.TYPE, new TxDetail(TxDetailType.TYPE, type.name()));
			
			// check if this is an AMP message
			final ExtensionElement xmpExtension = msg.getExtension(AMPExtension.NAMESPACE);
			if (xmpExtension != null)
			{					
				// start digging into the status attribute and rule element of the AMP message
				if (xmpExtension instanceof AMPExtension)
				{					
					final AMPExtension ampExtension = AMPExtension.class.cast(xmpExtension);
					
					// the 'to' attribute is the original recipient of the message stanza
					if (!StringUtils.isEmpty(ampExtension.getTo()))
					{
						try
						{
							final Jid orginalRecipJid = JidCreate.from(ampExtension.getTo());
							retVal.put(TxDetailType.ORIGINAL_RECIPIENT, new TxDetail(TxDetailType.ORIGINAL_RECIPIENT, orginalRecipJid.asBareJid().toString()));
						} 
						catch (XmppStringprepException e)
						{
							LOGGER.warn("Failed to parse ORIGINAL_RECIPIENT Jid from given string: " + ampExtension.getTo());
						}
					}
						
					// TIM+ message delivery falls under the status of "notify"
					if (ampExtension.getStatus() == AMPExtension.Status.notify && ampExtension.getRulesCount() > 0)
					{
						for (Rule rule : ampExtension.getRules())
						{
							// TIM+ message delivery uses the "action" of "notify" and the "condition" of "deliver"
							if (rule.getAction() == AMPExtension.Action.notify && rule.getCondition() != null &&
									rule.getCondition().getName() == AMPDeliverCondition.NAME)
							{
								retVal.put(TxDetailType.AMP_CONDITION_VALUE, new TxDetail(TxDetailType.AMP_CONDITION_VALUE, rule.getCondition().getValue()));
								break;
							}
						}
					}

				}
			}
		}
		else if (stanza instanceof IQ)
		{
			final IQ iq = IQ.class.cast(stanza);
			
			// get the type attribute if it exists
			final IQ.Type type = iq.getType();
			if (type != null)
				retVal.put(TxDetailType.TYPE, new TxDetail(TxDetailType.TYPE, type.name()));
		}
		else if (stanza instanceof Presence)
		{
			final Presence iq = Presence.class.cast(stanza);
			
			// get the type attribute if it exists
			final Presence.Type type = iq.getType();
			if (type != null)
				retVal.put(TxDetailType.TYPE, new TxDetail(TxDetailType.TYPE, type.name()));
		}		
		
		
		return retVal;
	}
	
	/**
	 * Converts a string into a Stanza object
	 * @param stanza The stanza in string format.
	 * @return A parsed Stanza object
	 * @throws Exception Thrown if the stanza cannot be parsed into a Stanza object.
	 */
	protected Stanza toStanza(String stanza) throws Exception
	{
		return PacketParserUtils.parseStanza(toNameSpacedStanza(stanza));
	}
	
	/**
	 * Converts an input stream into a Stanza object
	 * @param stanza The stanza contained within an input stream
	 * @return A parsed Stanza object
	 * @throws Exception Thrown if the stanza cannot be parsed into a Stanza object.
	 */
	protected Stanza toStanza(InputStream stream) throws Exception
	{
		return PacketParserUtils.parseStanza(IOUtils.toString(stream, Charset.defaultCharset()));
	}
	
	/**
	 * The underlying parser implementation requires that the stanza include a default namespace attribute.
	 * This method prepares a staza for parsing by setting a default namespace if one does not already exist.
	 * @param stanza The stanza to process.
	 * @return The input stanza with default client namespace (if the stanza did not already include a default namespace).
	 */
	protected String toNameSpacedStanza(String stanza)
	{
		String trimmedStanza = stanza.trim();
		
		if (!(trimmedStanza.contains(CLIENT_NAMESPACE) || trimmedStanza.contains(SERVER_NAMESPACE)))
		{
			final String start = trimmedStanza.substring(0, trimmedStanza.indexOf(' '));
			final String end = trimmedStanza.substring(trimmedStanza.indexOf(' '));
			
			return new StringBuilder(start).append(' ').append(CLIENT_NAMESPACE).append(' ').append(end).toString(); 
		}
		else
			return trimmedStanza;
	}
}
