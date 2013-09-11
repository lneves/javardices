package db.tpce.gen;

import java.util.ArrayList;
import java.util.List;

import db.bench.DbRandom;
import db.tpce.common.Sector;

public class SectorSelection
{
	private static final SectorSelection instance = new SectorSelection();
	private static final DbRandom drand = new DbRandom();

	private final List<Sector> lst_sector;

	private SectorSelection()
	{
		super();

		lst_sector = new ArrayList<Sector>();

		lst_sector.add(new Sector("BM", "Basic Materials"));
		lst_sector.add(new Sector("CG", "Capital Goods"));
		lst_sector.add(new Sector("CO", "Conglomerates"));
		lst_sector.add(new Sector("CC", "Consumer Cyclical"));
		lst_sector.add(new Sector("CN", "Consumer Non-Cyclical"));
		lst_sector.add(new Sector("EN", "Energy"));
		lst_sector.add(new Sector("FN", "Financial"));
		lst_sector.add(new Sector("HC", "Healthcare"));
		lst_sector.add(new Sector("SV", "Services"));
		lst_sector.add(new Sector("TC", "Technology"));
		lst_sector.add(new Sector("TR", "Transportation"));
		lst_sector.add(new Sector("UT", "Utilities"));
	}

	public static Sector get()
	{
		int s = instance.lst_sector.size() - 1;
		int r = drand.rndIntRange(0, s);
		return instance.lst_sector.get(r);
	}
}