package org.caudexorigo.wstest.domain;

public class TestNode extends TestStruct
{
	private static final long serialVersionUID = -2522403737714066419L;

	protected TestNode next;

	public TestNode getNext()
	{
		return this.next;
	}

	public void setNext(TestNode next)
	{
		this.next = next;
	}

	@Override
	public String toString()
	{
		return String.format("TestNode [next=%s]", next);
	}
}