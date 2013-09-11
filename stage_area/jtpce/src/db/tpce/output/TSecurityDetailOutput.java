package db.tpce.output;

import java.util.List;

import db.bench.output.TxOutput;
import db.tpce.common.SecurityDetail_Info1;
import db.tpce.common.SecurityDetail_Info2;
import db.tpce.common.SecurityDetail_Info3;
import db.tpce.common.SecurityDetail_Info4;
import db.tpce.common.SecurityDetail_Info5;
import db.tpce.common.SecurityDetail_Info6;
import db.tpce.common.SecurityDetail_Info7;

public class TSecurityDetailOutput extends TxOutput
{
	public final SecurityDetail_Info1 sd_info_1;
	public final List<SecurityDetail_Info2> lst_sd_info_2;
	public final List<SecurityDetail_Info3> lst_sd_info_3;
	public final List<SecurityDetail_Info4> lst_sd_info_4;
	public final SecurityDetail_Info5 sd_info_5;
	public final List<SecurityDetail_Info6> lst_sd_info_6;
	public final List<SecurityDetail_Info7> lst_sd_info_7;

	public TSecurityDetailOutput(int status, SecurityDetail_Info1 sd_info_1, List<SecurityDetail_Info2> lst_sd_info_2, List<SecurityDetail_Info3> lst_sd_info_3, List<SecurityDetail_Info4> lst_sd_info_4, SecurityDetail_Info5 sd_info_5, List<SecurityDetail_Info6> lst_sd_info_6, List<SecurityDetail_Info7> lst_sd_info_7)
	{
		super(status);
		this.sd_info_1 = sd_info_1;
		this.lst_sd_info_2 = lst_sd_info_2;
		this.lst_sd_info_3 = lst_sd_info_3;
		this.lst_sd_info_4 = lst_sd_info_4;
		this.sd_info_5 = sd_info_5;
		this.lst_sd_info_6 = lst_sd_info_6;
		this.lst_sd_info_7 = lst_sd_info_7;
	}

	@Override
	public String toString()
	{
		return String.format("TSecurityDetailOutput [%nsd_info_1=%s%n, lst_sd_info_2=%s%n, lst_sd_info_3=%s%n, lst_sd_info_4=%s%n, sd_info_5=%s%n, lst_sd_info_6=%s%n, lst_sd_info_7=%s%n, status=%s%n, tx_time=%s%n]", sd_info_1, lst_sd_info_2, lst_sd_info_3, lst_sd_info_4, sd_info_5, lst_sd_info_6, lst_sd_info_7, status, ((double) tx_time / 1000000.0));
	}
}