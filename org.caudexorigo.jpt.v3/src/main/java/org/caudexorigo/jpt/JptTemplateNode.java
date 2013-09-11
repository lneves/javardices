package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

public class JptTemplateNode extends JptNode
{
	private boolean _isInSlot;

	private CompiledTemplate _compiled_template;

	JptTemplateNode(String template, boolean isInSlot)
	{
		_isInSlot = isInSlot;
		_compiled_template = TemplateCompiler.compileTemplate(template);
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
		String sout = String.valueOf(TemplateRuntime.execute(_compiled_template, context));
		out.write(sout);
	}

	public boolean isInSlot()
	{
		return _isInSlot;
	}
}