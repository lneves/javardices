package test;

import org.caudexorigo.cryto.MD5;

public class Md5Test
{

	public static void main(String[] args)
	{
		long c_id = 4300000001L;
		String cid = "" + c_id;
		String e_md5 = "e7deac4af9dad2389d42bc4492e00320";
		String e_md5_s = "e7deac4af9dad2389d42";

		System.out.println(e_md5);
		System.out.println(MD5.getHashString(cid));

		System.out.println("*************************");
		System.out.println(e_md5_s);
		System.out.println(e_md5.substring(0, 20));

	}

}
