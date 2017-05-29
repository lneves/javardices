
package wstest.srv.actors;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="COfLineItem">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="ns:LineItem" nillable="true" name="LineItem" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class COfLineItem
{
	private List<LineItem> lineItemList = new ArrayList<LineItem>();

	/**
	 * Get the list of 'LineItem' element items.
	 * 
	 * @return list
	 */
	public List<LineItem> getLineItems()
	{
		return lineItemList;
	}

	/**
	 * Set the list of 'LineItem' element items.
	 * 
	 * @param list
	 */
	public void setLineItems(List<LineItem> list)
	{
		lineItemList = list;
	}
}
