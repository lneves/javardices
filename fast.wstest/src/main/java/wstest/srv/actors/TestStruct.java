
package wstest.srv.actors;

/**
 * Schema fragment(s) for this class:
 * 
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="TestStruct">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:int" name="i"/>
 *     &lt;xs:element type="xs:float" nillable="true" name="f"/>
 *     &lt;xs:element type="xs:string" nillable="true" name="s"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class TestStruct
{
	private int i;
	private Float f;
	private String s;

	/**
	 * Get the 'i' element value.
	 * 
	 * @return value
	 */
	public int getI()
	{
		return i;
	}

	/**
	 * Set the 'i' element value.
	 * 
	 * @param i
	 */
	public void setI(int i)
	{
		this.i = i;
	}

	/**
	 * Get the 'f' element value.
	 * 
	 * @return value
	 */
	public Float getF()
	{
		return f;
	}

	/**
	 * Set the 'f' element value.
	 * 
	 * @param f
	 */
	public void setF(Float f)
	{
		this.f = f;
	}

	/**
	 * Get the 's' element value.
	 * 
	 * @return value
	 */
	public String getS()
	{
		return s;
	}

	/**
	 * Set the 's' element value.
	 * 
	 * @param s
	 */
	public void setS(String s)
	{
		this.s = s;
	}
}
