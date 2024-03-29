/*
 * Copyright 2003-2005 Elliotte Rusty Harold
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of version 2.1 of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 * 
 * You can contact Elliotte Rusty Harold by sending e-mail to elharo@metalab.unc.edu.
 * Please include the word "XOM" in the subject line. The XOM home page is located at
 * http://www.xom.nu/
 */

package org.caudexorigo.nu.xom;

/**
 * <p>
 * This class represents a CDATA section. Builders will sometimes use this class to
 * represent a CDATA section. However, they are not required to do so. This class is used
 * solely for preservation of CDATA sections from input to output.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.1b4
 *
 */
class CDATASection extends Text {

  CDATASection(Text text) {
    super(text);
  }

  CDATASection(String data) {
    super(data);
  }

  boolean isCDATASection() {
    return true;
  }

  static Text build(String data) {
    return new CDATASection(data);
  }

  String escapeText() {
    String s = this.getValue();
    if (s.indexOf("]]>") != -1)
      return super.escapeText();
    else
      return "<![CDATA[" + s + "]]>";
  }

}
