package org.directtruststandards.timplus.monitor.tx.model;

/**
 * Enumeration of monitor detail types.
 * @author Greg Meyer
 * @since 1.0
 *
 */
public enum TxDetailType
{
	/**
	 * Unknown detail type
	 */
	UNKNOWN("UNKNOWN"),	
	
	/**
	 * The originator of the message
	 */
	FROM("FROM"),	
	
	/**
	 * The recipient of the message.  For user to user chats, this will contain only a single entry.  A group chat message
	 * may contain one or more recipients delimited by a comma (',').
	 */
	RECIPIENTS("RECIPIENTS"),

	/**
	 * The original recipient of a message.  Determined by the 'to' attribute of an AMP element.
	 */
	ORIGINAL_RECIPIENT("ORIGINAL_RECIPIENT"),
	
	/**
	 * Group chat room name.  Needed for generating error messages that originated from a group chat.
	 */
	GROUP_CHAT_ROOM("GROUP_CHAT_ROOM"),
	
	/**
	 * The message id of a stanza.
	 */
	MSG_ID("MSG_ID"),	
	
	/**
	 * The error condition.
	 */
	ERROR_CONDITION("ERROR_CONDITION"),
	
	/**
	 * For AMP (advanced message processing and error messages), this is the AMP condition value.  This is generally
	 * "stored" or "direct".  See section 3.3.1 of XEP 0075
	 */
	AMP_CONDITION_VALUE("AMP_CONDITION_VALUE"),
	
	/**
	 * The type attribute of a stanza.  The type attribute is defined in section 8.1.4 of RFC 6120, however the value of the attribute
	 * vary depending on the stanza type.  For example, 'message' stanza types are outlined in section 5.2.2. of RFC 6121.
	 */
	TYPE("TYPE"),
	
	/**
	 * A chat state indicator.  Chat states are enumerated in section 5.2 of XEP 0085.
	 */
	CHAT_STATE("CHAT_STATE"),
	
	/**
	 * Indicates if there is a message body present.  This detail has no value; the presence of this detail in the details list indicates that a message body
	 * is present in the message.
	 */
	MESSAGE_BODY_IND("MESSAGE_BODY_IND");
	
	private final String type;
	
	/**
	 * Private constructor
	 * @param type The type
	 */
    private TxDetailType(String type) 
	{
        this.type = type;
    }
    
    /**
     * Gets the detail type as a string
     * @return
     */
    public String getType()
    {
    	return type;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString()
    {
    	return getType();
    }
}
