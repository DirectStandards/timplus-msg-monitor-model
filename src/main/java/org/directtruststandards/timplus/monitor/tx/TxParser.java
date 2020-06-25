package org.directtruststandards.timplus.monitor.tx;

import java.io.InputStream;
import java.util.Map;

import org.directtruststandards.timplus.monitor.tx.model.Tx;
import org.directtruststandards.timplus.monitor.tx.model.TxDetail;
import org.directtruststandards.timplus.monitor.tx.model.TxDetailType;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Interface for a parser the transforms a stanza into a monitoring object along with set of monitor details
 * @author Greg Meyer
 * @since 1.0
 */
public interface TxParser
{
	/**
	 * Parses a stanza in string format into a monitoring object.
	 * @param stanza A string representation of a stanza
	 * @return A monitoring object containing the stanza type and all relevant monitoring details
	 */
	public Tx parseStanza(String stanza);
	
	/**
	 * Parses a stanza contained within an input stream into a monitoring object.
	 * @param stanza An input stream that contains the stanza
	 * @return A monitoring object containing the stanza type and all relevant monitoring details
	 */
	public Tx parseStanza(InputStream stream);
	
	/**
	 * Parses a stanza object into a monitoring object.
	 * @param stanza A stanza POJO representation
	 * @return A monitoring object containing the stanza type and all relevant monitoring details
	 */
	public Tx parseStanza(Stanza stanza);
	
	/**
	 * Parses a stanza in string format into set of monitoring details.
	 * @param stanza A string representation of a stanza
	 * @return A map containing all of the relevant monitoring details
	 */
	public Map<TxDetailType, TxDetail> getStanzaDetails(String stanza);
	
	/**
	 * Parses a stanza contained within an input stream into set of monitoring details.
	 * @param stanza An input stream that contains the stanza
	 * @return A map containing all of the relevant monitoring details
	 */
	public Map<TxDetailType, TxDetail> getStanzaDetails(InputStream stream);
	
	/**
	 * Parses a stanza object into set of monitoring details.
	 * @param stanza A stanza POJO representation
	 * @return A map containing all of the relevant monitoring details
	 */
	public Map<TxDetailType, TxDetail> getStanzaDetails(Stanza stanza);
}
