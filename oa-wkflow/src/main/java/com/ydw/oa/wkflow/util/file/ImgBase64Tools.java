package com.ydw.oa.wkflow.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

public class ImgBase64Tools {

	public static void main(String[] args) {
		String s = "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";
		generateImage(s, "c:\\seal.jpg");
	}

	public static boolean generateImage(String imgStr, String path) {
		return generateImage(imgStr, new File(path));
	}

	public static boolean generateImage(String imgStr, File file) {
		if (imgStr == null)
			return false;
		try {
			byte[] b = Base64.getDecoder().decode(imgStr);
			// 处理数据
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(file);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
