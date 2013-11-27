package wstest.srv;

public class BuildMessages
{
//
//	private static final int[] lens = { 20, 100 };
//
//	public static void main(String[] args) throws Throwable
//	{
//		try
//		{
//			for (int i = 0; i < lens.length; i++)
//			{
//				buildEchoStruct(lens[i]);
//				buildEchoList(lens[i]);
//				buildGetOrder(lens[i]);
//			}
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace();
//			System.exit(-1);
//		}
//
//	}
//
//	private static void buildGetOrder(int size) throws Throwable
//	{
//		GetOrder go = new GetOrder();
//		go.setOrderId(1);
//		go.setCustomerId(2);
//		go.setMessageSize(size);
//
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./bench/post_files/getorder" + size + ".javabin"));
//		ObjectMapper json_out = new ObjectMapper();
//
//		out.writeObject(go);
//		json_out.writeValue(new FileOutputStream("./bench/post_files/getorder" + size + ".json"), go);
//	}
//
//	private static void buildEchoStruct(int size) throws Throwable
//	{
//
//		COfTestStruct cots = new COfTestStruct();
//
//		for (int i = 1; i <= size; i++)
//		{
//			TestStruct ts = new TestStruct();
//			ts.setF(i);
//			ts.setI(i);
//			ts.setS("wstest");
//			cots.addTestStruct(ts);
//		}
//
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./bench/post_files/echostruct" + size + ".javabin"));
//		ObjectMapper json_out = new ObjectMapper();
//
//		out.writeObject(cots);
//		json_out.writeValue(new FileOutputStream("./bench/post_files/echostruct" + size + ".json"), cots);
//
//	}
//
//	private static void buildEchoList(int size) throws Throwable
//	{
//		JsonFactory jsonF = new JsonFactory();
//		JsonGenerator jg = jsonF.createJsonGenerator(System.out, JsonEncoding.UTF8);
//		jg.useDefaultPrettyPrinter();
//
//		TestNode tn0 = new TestNode();
//		tn0.setF(1f);
//		tn0.setI(1);
//		tn0.setS("wstest");
//
//		tn0 = fill(tn0, 1, 1, "wstest", size);
//
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./bench/post_files/echolist" + size + ".javabin"));
//		ObjectMapper json_out = new ObjectMapper();
//
//		out.writeObject(tn0);
//		json_out.writeValue(new FileOutputStream("./bench/post_files/echolist" + size + ".json"), tn0);
//
//	}
//
//	private static final TestNode fill(TestNode tn_parent, int i, float f, String s, int limit)
//	{
//		try
//		{
//			TestNode tn = new TestNode();
//			tn.setF(i + 1);
//			tn.setI(i + 1);
//			tn.setS(s);
//
//			tn_parent.setNext(tn);
//
//			if (i < limit)
//			{
//				fill(tn, i + 1, f + 1, s, limit);
//			}
//
//			return tn_parent;
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace();
//			System.exit(-1);
//			return null;
//		}
//	}
}
