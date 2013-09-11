package db.tpce.common;

import java.util.Date;

public class Customer
{
	public long c_id;
	public String c_tax_id;
	public String c_st_id;
	public String c_l_name;
	public String c_f_name;
	public String c_m_name;
	public String c_gndr;
	public int c_tier;
	public Date c_dob;
	public long c_ad_id;
	public String c_ctry_1;
	public String c_area_1;
	public String c_local_1;
	public String c_ext_1;
	public String c_ctry_2;
	public String c_area_2;
	public String c_local_2;
	public String c_ext_2;
	public String c_ctry_3;
	public String c_area_3;
	public String c_local_3;
	public String c_ext_3;
	public String c_email_1;
	public String c_email_2;
	
	
	@Override
	public String toString()
	{
		return String.format("Customer {c_id=%s, c_tax_id=%s, c_st_id=%s, c_l_name=%s, c_f_name=%s, c_m_name=%s, c_gndr=%s, c_tier=%s, c_dob=%s, c_ad_id=%s, c_ctry_1=%s, c_area_1=%s, c_local_1=%s, c_ext_1=%s, c_ctry_2=%s, c_area_2=%s, c_local_2=%s, c_ext_2=%s, c_ctry_3=%s, c_area_3=%s, c_local_3=%s, c_ext_3=%s, c_email_1=%s, c_email_2=%s}", c_id, c_tax_id, c_st_id, c_l_name, c_f_name, c_m_name, c_gndr, c_tier, c_dob, c_ad_id, c_ctry_1, c_area_1, c_local_1, c_ext_1, c_ctry_2, c_area_2, c_local_2, c_ext_2, c_ctry_3, c_area_3, c_local_3, c_ext_3, c_email_1, c_email_2);
	}

}
