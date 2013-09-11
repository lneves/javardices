package org.caudexorigo.wstest.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class COfLineItem implements Serializable
{
	private static final long serialVersionUID = -8627189306910959713L;

	private final List<LineItem> lineItemList = new ArrayList<LineItem>();

	public void addLineItem(LineItem lineItem)
	{
		lineItemList.add(lineItem);
	}

	public LineItem getLineItem(int index)
	{
		return (LineItem) lineItemList.get(index);
	}

	public int sizeLineItemList()
	{
		return lineItemList.size();
	}

	public final List<LineItem> getLineItemList()
	{
		return lineItemList;
	}
}