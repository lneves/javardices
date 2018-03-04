package jpt.test;

public class SimpleContext2
{

	public String getValue()
	{
		return "Hello World!!";
	}

	public String getLink()
	{
		return "http://www.yahoo.com/";
	}

	public String getCssLink()
	{
		return "link1";
	}

	public boolean isBoolt()
	{
		return true;
	}

	public Person[] getPersons()
	{
		Person[] p = new Person[4];
		p[0] = new Person(32, "luis");
		p[1] = new Person(34, "ana");
		p[2] = new Person(2, "elisa");
		p[3] = new Person(1, "xavier");

		return p;
	}

	public String getSalut()
	{
		return "Hello <strong>World</strong>!";
	}
}
