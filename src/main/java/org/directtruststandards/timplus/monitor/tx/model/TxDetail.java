package org.directtruststandards.timplus.monitor.tx.model;

import java.io.Serializable;

/**
 * A mapping of stanza monitoring type to a value.
 * @author Greg Meyer
 * @since 1.0
 */
public class TxDetail implements Serializable
{
	private static final long serialVersionUID = -2571032309921504221L;

	protected TxDetailType detailType;
	
	protected String detailValue;
	
	/**
	 * Constructor
	 */
	public TxDetail()
	{
		detailType = TxDetailType.UNKNOWN;
		detailValue = "";
	}
	
	/**
	 * Constructor
	 * @param detailType The detail type
	 * @param detailValue The detail value
	 */
	public TxDetail(TxDetailType detailType, String detailValue)
	{
		if (detailType == null)
			throw new IllegalArgumentException("Detail type cannot be null or empty");
		
		if (detailValue == null)
			throw new IllegalArgumentException("Detail value cannot be null");
		
		this.detailType = detailType;
		this.detailValue = detailValue;
	}
	
	/**
	 * Sets the detail type
	 * @param detailType The detail type
	 */
	public void setDetailType(TxDetailType detailType)
	{
		if (detailType == null)
			throw new IllegalArgumentException("Detail type cannot be null or empty");
		
		this.detailType = detailType;
	}
	
	/**
	 * Gets the detail type
	 * @return The detail type
	 */
	public TxDetailType getDetailType()
	{
		return this.detailType;
	}
	
	/**
	 * Sets the detail value
	 * @param detailValue The detail value
	 */
	public void setDetailValue(String detailValue)
	{
		if (detailValue == null)
			throw new IllegalArgumentException("Detail value cannot be null");
		
		this.detailValue = detailValue;
	}
	
	/**
	 * Gets the detail value
	 * @return The detail value
	 */
	public String getDetailValue()
	{
		return this.detailValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{	
		final StringBuilder builder = new StringBuilder(detailType.getType()).append("\r\n").append(detailValue);
		
		return builder.toString();
	}
}
