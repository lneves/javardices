
package wstest.srv.actors;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="uri:WSTestWeb-TestService" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="COfTestStruct">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="ns:TestStruct" name="l" minOccurs="1" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
public class COfTestStruct
{
    private List<TestStruct> lList = new ArrayList<TestStruct>();

    /** 
     * Get the list of 'l' element items.
     * 
     * @return list
     */
    public List<TestStruct> getLs() {
        return lList;
    }

    /** 
     * Set the list of 'l' element items.
     * 
     * @param list
     */
    public void setLs(List<TestStruct> list) {
        lList = list;
    }
}
