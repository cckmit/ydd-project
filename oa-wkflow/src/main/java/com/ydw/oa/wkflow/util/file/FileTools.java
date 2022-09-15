package com.ydw.oa.wkflow.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;

public class FileTools {

	// 解析propertis中的字符串
	public static String parseStr(String str) {

		str = str.replace("{", "").replace("}", "");

		String[] split = str.split(",");

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < split.length; i++) {
			String[] split2 = split[i].split("=");
			map.put(split2[0].trim(), split2[1].trim());
		}
		return map.get("bidPrice").toString();
	}

	// 解析propertis中的字符串
	public static Map<String, Object> parseStrToMap(String str) {

		str = str.replace("{", "").replace("}", "");

		String[] split = str.split(",");

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < split.length; i++) {
			String[] split2 = split[i].split("=");
			map.put(split2[0].trim(), split2[1].trim());
		}
		return map;
	}

	public static String getSuffix(String filename) {
		if (ChkUtil.isNull(filename)) {
			return "";
		}
		if (!filename.contains(".")) {
			return "";
		}
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	/**
	 * 
	 * copy文件从一个目录到另一个目录
	 * 
	 * @param srcFile  源文件路径
	 * @param destFile 目标文件路径
	 * @return
	 */
	public static boolean copyFile(String srcFile, String destFile) {
		boolean flag = false;
		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		try {
			// 获取源文件和目标文件的输入输出流
			fin = new FileInputStream(srcFile);
			fout = new FileOutputStream(destFile);
			// 获取输入输出通道
			fcin = fin.getChannel();
			fcout = fout.getChannel();
			// 创建缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (true) {
				// clear方法重设缓冲区，使它可以接受读入的数据
				buffer.clear();
				// 从输入通道中将数据读到缓冲区
				int r = fcin.read(buffer);
				// read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
				if (r == -1) {
					flag = true;
					break;
				}
				// flip方法让缓冲区可以将新读入的数据写入另一个通道
				buffer.flip();
				// 从输出通道中将数据写入缓冲区
				fcout.write(buffer);
			}
			fout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fin) {
					fin.close();
				}
				if (null != fout) {
					fout.close();
				}
				if (null != fcin) {
					fcin.close();
				}
				if (null != fcout) {
					fcout.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 判断文件夹中所有文件的名字是否含有.bid
	 * 
	 * @param file 想要读取的文件对象
	 * @return boolean
	 */
	public static boolean checkFileName(String filePath, String exclusive_name) {
		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}

		// 含有.bid，返回true,否则返回false
		boolean status = false;
		File fa[] = f.listFiles();
		for (int i = 0; i < fa.length; i++) {
			File fs = fa[i];
			String name = fs.getName();
			if (name.contains(exclusive_name)) {
				status = true;
				return status;
			} else {
				status = false;
			}
		}
		return status;
	}

	/**
	 * 读取txt文件的内容
	 * 
	 * @param file 想要读取的文件对象
	 * @return 返回文件内容
	 */
	/*
	 * public static String getKey(File file) { try { return
	 * FileUtils.readFileToString(file, "utf-8"); } catch (IOException e) {
	 * e.printStackTrace(); return ""; } } public static String getKeyOld(File file)
	 * { StringBuilder result = new StringBuilder(); try { BufferedReader br = new
	 * BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件 String s =
	 * null; while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
	 * result.append(System.lineSeparator() + s); } br.close(); } catch (Exception
	 * e) { e.printStackTrace(); } return result.toString().trim(); }
	 */

	/**
	 * 
	 * 查找某个文件下，包含某个关键字的文件
	 * 
	 * @param folder
	 * @param keyWord
	 * @return
	 */
	public static File searchFile(File folder, final String keyWord) {// 递归查找包含关键字的文件

		File[] subFolders = folder.listFiles(new FileFilter() {// 运用内部匿名类获得文件
			@Override
			public boolean accept(File pathname) {// 实现FileFilter类的accept方法
				// 目录或文件包含关键字
				if (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())) {
					return true;
				}

				return false;
			}
		});

		File foldResult = null;
		for (int i = 0; i < subFolders.length; i++) {// 循环显示文件夹或文件
			if (subFolders[i].isFile()) {// 如果是文件则将文件添加到结果列表中
				foldResult = subFolders[i];
			} else {// 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
				searchFile(subFolders[i], keyWord);
			}
		}

		return foldResult;
	}

	/**
	 * 
	 * 查找某个文件下，包含某个关键字的文件夹
	 * 
	 * @param folder
	 * @param keyWord
	 * @return
	 */
	public static File searchFileFolder(File folder, final String keyWord) {// 递归查找包含关键字的文件

		File[] subFolders = folder.listFiles(new FileFilter() {// 运用内部匿名类获得文件
			@Override
			public boolean accept(File pathname) {// 实现FileFilter类的accept方法
				// 目录或文件包含关键字
				if (pathname.isDirectory() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())) {
					return true;
				}
				return false;
			}
		});

		File foldResult = null;
		for (int i = 0; i < subFolders.length; i++) {// 循环显示文件夹或文件
			if (subFolders[i].isDirectory()) {// 如果是文件夹则将文件夹添加到结果列表中
				foldResult = subFolders[i];
			}
		}

		return foldResult;
	}

	/**
	 * 文件写入数据
	 * 
	 * @param content
	 * @param write_url
	 * @return
	 */
	public static boolean writeFile(String content, String url) {
		if (ChkUtil.isNull(url)) {
			return false;
		}
		BufferedWriter writer = null;
		try {

			File file = new File(url);
			if (file.exists()) {
				file.delete();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream writerStream = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
			writer.write(content);
			writer.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public static void main(String[] args) {

		// String s = "x.ds"; System.err.println(FileTools.getSuffix(s));

		// boolean fileName =
		// checkFileName("C:\\tmp\\74f926b3-26cb-4d36-8f86-634b49323d33");
		// System.err.println(fileName);

		// File file = new
		// File("C:\\data\\data\\bid\\6Yyfh6wZxyBJkYDzn5M95T\\key.txt");
		// System.out.println(getKey(file));

		// File folder = new
		// File("C:\\data\\data\\bid\\KTugyHVuGAvFrkCngLBXnc");// 默认目录
		// String keyword = ".bid";
		// if (!folder.exists()) {// 如果文件夹不存在
		// System.out.println("目录不存在：" + folder.getAbsolutePath());
		// return;
		// }
		// File result = searchFile(folder, keyword);// 调用方法获得文件数组
		// System.out.println("在 " + folder + " 以及所有子文件时查找对象" + keyword);
		// System.out.println(result.getAbsolutePath() + " ");// 显示文件绝对路径
		String unzip_dir_url = "C:/data/data/bid/8rGZfcqDHbc9W7WTKUUrZe/ceshi";
		File attachmentFileFolder = FileTools.searchFile(new File(unzip_dir_url), ".docx");
		// File[] attachmentFiles = attachmentFileFolder.listFiles();
		// System.err.println(JsonUtil.toJson(attachmentFiles));

	}

	public static String readFileToString(String file) {
		String content = "";
		// 2、建立数据通道
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int length = 0;
			// 循环读取文件内容，输入流中将最多buf.length个字节的数据读入一个buf数组中,返回类型是读取到的字节数。
			// 当文件读取到结尾时返回 -1,循环结束。
			while ((length = fis.read(buf)) != -1) {
				content += new String(buf, 0, length);
			}
			// 最后记得，关闭流
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}
}
