package com.ydw.oa.wkflow.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreatedTools {

	private final static DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

	// 返回long型的创建时间
	public static long getCreated() {
		Date d = new Date();
		long l = Long.parseLong(df.format(d));
		return l;
	}

	public static long getCreated(int add) {
		long t = System.currentTimeMillis() + add;
		Timestamp time = new Timestamp(t);
		long l = Long.parseLong(df.format(time));
		return l;
	}

	public static long t() {
		return 0l;
	}

}
