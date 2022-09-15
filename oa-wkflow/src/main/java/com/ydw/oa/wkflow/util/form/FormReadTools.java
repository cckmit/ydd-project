package com.ydw.oa.wkflow.util.form;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tmsps.fk.common.util.ChkUtil;

/**
 * 读取模板表单
 * 
 * var formData = '@loadFormData()';
 * 
 * 替换 '@loadFormData()' 为json值
 * 
 * @author 冯晓东
 *
 */
public class FormReadTools {

	private static final String htmlSubmit;
	private static final String htmlRead;

	static {
		htmlSubmit = readModel("/models/form/form.html");
		htmlRead = readModel("/models/form/form-read.html");
	}

	public static String readModel(String file) {
		String htmlStr = "";
		InputStream is = FormReadTools.class.getResourceAsStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				htmlStr += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return htmlStr;
	}

	public static String replaceSubmitModel(String json) {
		if (ChkUtil.isNull(json)) {
			return null;
		}
		// var formData = '@loadFormData()';
		return htmlSubmit.replace("'@loadFormData()'", json);
	}

	public static String replaceReadModel(String json) {
		if (ChkUtil.isNull(json)) {
			return null;
		}
		// var formData = '@loadFormData()';
		return htmlRead.replace("'@loadFormData()'", json);
	}

	public static void main(String[] args) {
		System.err.println(readModel("/form_model/form.html"));
		System.err.println(readModel("/form_model/form-read.html"));
	}
}
