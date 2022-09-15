package com.ydw.oa.wkflow.util.file;

/**
 * Html转PDF工具类
 * 
 */
public class Html2PdfUtil {

	public static boolean html2pdf() {
		String cmd = "wkhtmltopdf -O Landscape hy.html hy.pdf";
		return Html2PdfUtil.html2pdf(cmd);
	}

	public static boolean html2pdf(String html, String pdf) {
		// -O Landscape 转横向pdf
		String fmt = "wkhtmltopdf -O Landscape %s %s";
		String cmd = String.format(fmt, html, pdf);
		return Html2PdfUtil.html2pdf(cmd);
	}

	public static boolean html2pdf(String cmd) {
		boolean b = false;
		String str = CmdTools.cmd(cmd);
		if (str.contains("100%")) {
			b = true;
		}
		return b;
	}

}
