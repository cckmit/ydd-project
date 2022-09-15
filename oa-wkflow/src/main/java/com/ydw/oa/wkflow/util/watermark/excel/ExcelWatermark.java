package com.ydw.oa.wkflow.util.watermark.excel;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWatermark {
	
	public static void main(String[] args)  {
		addWaterMark("D:\\test2.xls","BGYP-2021-0001",".xls");
	}

	public static void addWaterMark(String filePath,String text,String suffix) {
		Workbook ws = null;
		OutputStream fos = null;
		ByteArrayOutputStream os = null;
		try {
			if ("xls".equals(suffix)) {
				ws = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(filePath)));
			} else if ("xlsx".equals(suffix)) {
				ws = new XSSFWorkbook(new FileInputStream(filePath));
			}
			Iterator<Sheet> sheetIterator = ws.sheetIterator();
			while (sheetIterator.hasNext()) {
				Sheet sheet = sheetIterator.next();
				BufferedImage bufferImg = ImageUtils.createWaterMark(text);
				ExcelWaterRemarkUtils.putWaterRemarkToExcel(ws, sheet, bufferImg, 0, 5, 9, 10, 3, 3, 0, 0);
			}
			os = new ByteArrayOutputStream();
			ws.write(os);
			ws.close();
			byte[] content = os.toByteArray();

			File file1 = new File(filePath);// Excel文件生成后存储的位置。
			fos = new FileOutputStream(file1);
			fos.write(content);
			os.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ws != null) {
					ws.close();
				}
				if (os != null) {
					os.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {

			}

		}

	}

}
