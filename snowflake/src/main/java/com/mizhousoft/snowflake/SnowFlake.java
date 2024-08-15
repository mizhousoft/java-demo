package com.mizhousoft.snowflake;

/**
 * SnowFlake
 * 
 * +------+----------------------+----------------+----------------+
 * | sign |      platform id     |    server id   |  delta seconds |
 * +------+----------------------+----------------+----------------+
 *   1bit            8bits             13bits           28bits
 *
 * @version
 */
public class SnowFlake
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		long aa = 8191L;
		System.out.println(aa);
		System.out.println(Long.toBinaryString(aa));
		aa = aa << 28;
		System.out.println(aa);
		System.out.println(Long.toBinaryString(aa));

		System.out.println();

		long bb = 255L;
		System.out.println(bb);
		System.out.println(Long.toBinaryString(bb));
		bb = bb << 41;
		System.out.println(bb);
		System.out.println(Long.toBinaryString(bb));

		System.out.println();

		long cc = 447811200L;
		System.out.println(Long.toBinaryString(cc));

		System.out.println();

		long value = aa | bb | cc;
		System.out.println(value);
	}

}
