package com.ydw.oa.auth.util.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tmsps.fk.common.util.ChkUtil;

/**
 * 操作Excel表格的功能类
 */
public class ExcelReadTools {
	private Workbook wb;
	private Sheet sheet;
	private Row row;

	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @return String 表头内容的数组
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 */
	public String[] readExcelTitle(InputStream is, int rownumber) {
		try {
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(rownumber);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}

	/**
	 * 读取Excel数据内容 --> List< Map<String,Object>>
	 * 
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public List<Map<String, Object>> readExcel2ListMap(InputStream is, String[] classFields, int rownum) {
		ExcelReadTools excelReader = new ExcelReadTools();
		return excelReader.readExcel2ListMap(is, classFields, rownum, 0);
	}

	/**
	 * 读取Excel数据内容 --> List< Map<String,Object>>
	 * 
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public List<Map<String, Object>> readExcel2ListMap(InputStream is, String[] classFields, int rowIndex,
			int checkColnum) {

		try {
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(rowIndex);
		int colNum = row.getPhysicalNumberOfCells();
		// 判断列数与表头长度是否一致
		if (colNum != classFields.length) {
			throw new RuntimeException("excel 列数 和 classFields的长度不一致！！！");
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = rowIndex; i <= rowNum; i++) {
			row = sheet.getRow(i);
			if (row == null || ChkUtil.isNull(row.getCell((short) checkColnum))) {
				continue;
			}
			int j = 0;
			Map<String, Object> map = new HashMap<String, Object>();
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				map.put(classFields[j], getCellFormatValue(row.getCell((short) j)).trim());
				j++;
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public Map<Integer, String> readExcelContent(InputStream is) {
		Map<Integer, String> content = new HashMap<Integer, String>();
		String str = "";
		try {
			wb = WorkbookFactory.create(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		System.err.println(rowNum + "-----------");

		row = sheet.getRow(1);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 0; i <= rowNum + 1; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				content.put(i, "");
				continue;
			}
			int j = 0;
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				str += getCellFormatValue(row.getCell((short) j)).trim() + "#";

				j++;
			}
			content.put(i, str);
			str = "";
		}
		return content;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case NUMERIC:
			case FORMULA: {
				// 判断当前的cell是否为Date
				if (DateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					NumberFormat nf = new DecimalFormat("0");
					cellvalue = nf.format(cell.getNumericCellValue());
				}
				break;
			}
			// 如果当前Cell的Type为STRIN
			case STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

	public static void main(String[] args) {
		InputStream is = null;
		try {
			// 对读取Excel表格标题测试
			is = new FileInputStream("d:\\initial.xlsx");
			System.err.println("excel 在该工具类下面.");
			ExcelReadTools excelReader = new ExcelReadTools();
			String[] titles = excelReader.readExcelTitle(is, 2);
			for (String map : titles) {
				System.err.println(map);
			}
			is = new FileInputStream("d:\\particular_score.xlsx");
			List<Map<String, Object>> list = excelReader.readExcel2ListMap(is,
					new String[] { "index", "evaluation_name", "review_factor", "review_standard", "is_deviation",
							"review_method_name", "review_particular_score_min", "review_particular_score", "sum" },
					3);
			for (Map<String, Object> map : list) {
				System.err.println(map);
			}

		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}