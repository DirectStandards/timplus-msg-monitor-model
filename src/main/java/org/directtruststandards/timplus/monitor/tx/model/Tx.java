package org.directtruststandards.timplus.monitor.tx.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Structure for stanza monitoring.  Contains the stanza type along with a map of relevant monitoring details.
 * @author gm2552
 * @since 1.0
 */
public class Tx implements Serializable
{
	private static final long serialVersionUID = -1364037182351911688L;

	protected TxStanzaType stanzaType;
	
	protected Map<TxDetailType, TxDetail> details;
	
	/**
	 * Empty constructor
	 */
	public Tx()
	{
		stanzaType = TxStanzaType.UNKNOWN;
		details = new HashMap<>();
	}
	
	/**
	 * Constructor
	 * @param stanzaType The stanza type
	 * @param details Map of message details
	 */
	public Tx(TxStanzaType stanzaType, Map<TxDetailType, TxDetail> details)
	{
		if (stanzaType == null)
			throw new IllegalArgumentException("Type cannot be null");
		
		if (details == null)
			throw new IllegalArgumentException("Details cannot be null");
		
		this.details = Collections.unmodifiableMap(details);
		this.stanzaType = stanzaType;
	}
	
	/**
	 * Gets the stanza type
	 * @return The stanza type
	 */
	public TxStanzaType getStanzaType()
	{
		return this.stanzaType;
	}
	
	/**
	 * Sets the stanza type
	 * @param type The stanza type
	 */
	public void setStanzaType(TxStanzaType type)
	{
		if (type == null)
			throw new IllegalArgumentException("Type cannot be null");
		
		this.stanzaType = type;
	}
	
	/**
	 * Sets the stanza details
	 * @param details The stanza details
	 */
	public void setDetails(Map<TxDetailType, TxDetail> details)
	{
		if (details == null)
			throw new IllegalArgumentException("Details cannot be null");
		
		this.details = details;
	}
	
	/**
	 * Gets the map of stanza details
	 * @return
	 */
	public Map<TxDetailType, TxDetail> getDetails()
	{
		return this.details;
	}
	
	/**
	 * Gets a specific stanza detail by type
	 * @param detailType The detail type to get
	 * @return The detail corresponding to the type
	 */
	@JsonIgnore
	public TxDetail getDetail(TxDetailType detailType)
	{
		if (detailType == null)
			return null;
		
		return details.get(detailType);
	}
	
	/**
	 * This is a special case of updating the Tx details.  A stanza only contains a single recipient, however
	 * in the case of group chats, all recipients are added to the Tx details at time the original monitored message is created.
	 * Because each copy of the stanza in a groupchat uses the same message id, this mitigates race conditions where original monitoring messages
	 * may be placed into the monitoring system after responses (AMP and error messages) are received.  This is especially true if the monitoring
	 * service is distributed across multiple JVMs or availability zones.
	 * @param recip
	 */
	public void addRecipient(String recip)
	{
		final TxDetail detail = getDetail(TxDetailType.RECIPIENTS);
		if (detail == null)
			details.put(TxDetailType.RECIPIENTS, new TxDetail(TxDetailType.RECIPIENTS, recip));
		else
		{
			if (detail.getDetailValue().isEmpty())
				detail.setDetailValue(recip);
			else
			{
				final StringBuilder builder = new StringBuilder(detail.getDetailValue()).append(",").append(recip);
				detail.setDetailValue(builder.toString());
			}
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder("TxType: ").append(stanzaType.toString());
		
		if (details.isEmpty())
			builder.append("\r\nNo Details");
		else
		{
			for (TxDetail detail: details.values())
				builder.append("\r\n\r\n").append(detail.toString());
		}
		
		return builder.toString();
		
	}	
}
