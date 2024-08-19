package com.mizhousoft.snowflake;

import java.time.LocalDate;

import com.mizhousoft.commons.lang.LocalDateUtils;

/**
 * SnowFlake
 * 
 * +------+----------------------+----------------+----------------+
 * | sign |      platform id     |    server id   |  delta seconds |
 * +------+----------------------+----------------+----------------+
 *   1bit            8bits             13bits           28bits
 * 
 * Platform id max value is 256
 * Server id max value is 8291
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
		
		long seconds = LocalDateUtils.toSecond(LocalDate.now()) - LocalDateUtils.toSecond(LocalDate.of(2024, 8, 1));
		System.out.println(seconds);
		System.out.println(Long.toBinaryString(seconds));

		System.out.println();

		long value = aa | bb | seconds;
		System.out.println(value);
		System.out.println(Long.toBinaryString(value));
	}
}
