package org.directtruststandards.timplus.monitor.tx.model;

/**
 * The type of stanza.  RFC 6120 defines three type of stanzas: message, presence, and information/query (IQ).  The monitoring system
 * sub-classifies these into more granular types to help identify errors and AMP (advanced message processes) types.
 * @author gm2552
 *
 */
public enum TxStanzaType
{
	/**
	 * Unknown stanza type
	 */
	UNKNOWN,
	
	/**
	 * XMPP Message stanza
	 */
	MESSAGE,
	
	/**
	 * XMPP Presense stanza
	 */
	PRESENSE,
	
	/**
	 * Information/Query stanza
	 */
	IQ,
	
	/**
	 * A special sub type of MESSAGE that includes AMP (advance message processing) information.  Used
	 * for tracking the delivery or offline delivery status of a message.
	 */
	AMP,
	
	/**
	 * A special sub type of IQ that includes error information.
	 */
	IQ_ERROR,
	
	/**
	 * A special sub type of MESSAGE that includes error information.
	 */
	PRESENSE_ERROR,
	
	/**
	 * A special sub type of MESSAGE that includes error information.
	 */
	MESSAGE_ERROR
}
