package org.caudexorigo.jpt;

import org.caudexorigo.text.StringUtils;

public class RepeatElements
{
	private String _loopVar;

	private String _padding;

	private String _loopSourceExpression;

	private int _loopIncrement = 1;

	public RepeatElements(String repeatExpression, String padding)
	{
		_padding = padding;
		String[] repeatElements = StringUtils.stripAll(StringUtils.split(repeatExpression, ';'));

		for (int i = 0; i < repeatElements.length; i++)
		{
			if (repeatElements[i].trim().length() > 0)
			{
				String[] subElements = StringUtils.stripAll(StringUtils.split(repeatElements[i], '='));

				if (subElements.length == 2)
				{
					if (subElements[0].equalsIgnoreCase("var"))
					{
						_loopVar = subElements[1];
					}

					if (subElements[0].equalsIgnoreCase("step"))
					{
						_loopIncrement = Integer.valueOf(subElements[1]).intValue();
					}

					if (subElements[0].equalsIgnoreCase("source"))
					{
						_loopSourceExpression = subElements[1];
					}
				}
			}
		}
	}

	public String getLoopSourceExpression()
	{
		return _loopSourceExpression;
	}

	public String getLoopVar()
	{
		return _loopVar;
	}

	public int getLoopIncrement()
	{
		return _loopIncrement;
	}

	public String getPadding()
	{
		return _padding;
	}
}