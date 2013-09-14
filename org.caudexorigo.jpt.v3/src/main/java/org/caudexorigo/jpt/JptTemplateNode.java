package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mvel2.ParserContext;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

public class JptTemplateNode extends JptNode
{
	private boolean _isInSlot;

	private ParserContext _parser_context;
	private CompiledTemplate _compiled_template;

	private String _template;

	JptTemplateNode(String template, boolean isInSlot)
	{
		_template = template;
		_isInSlot = isInSlot;
	}

	public int getChildCount()
	{
		return 0;
	}

	public JptNode getChild(int i)
	{
		throw new IndexOutOfBoundsException("Output Expressions do not have children");
	}

	public void render(Map<String, Object> context, Writer out) throws IOException
	{
		if (_parser_context == null)
		{
			_parser_context = ParserContext.create();

			Set<Entry<String, Object>> ctx_entries = context.entrySet();

			for (Entry<String, Object> entry : ctx_entries)
			{
				_parser_context.addInput(entry.getKey(), entry.getValue().getClass());
			}
			_compiled_template = TemplateCompiler.compileTemplate(_template, _parser_context);
		}

		String sout = String.valueOf(TemplateRuntime.execute(_compiled_template, context));
		out.write(sout);
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}