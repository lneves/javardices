package org.caudexorigo.jpt;

import nu.xom.Element;

public class SlotElement
{
	public SlotElement(Element slot, Object slot_ctx)
	{
		_slot = slot;
		_slot_jxpath_ctx = slot_ctx;
	}

	public Object getSlotContext()
	{
		return _slot_jxpath_ctx;
	}

	public Element getSlot()
	{
		return _slot;
	}

	private Object _slot_jxpath_ctx;

	private Element _slot;
}
