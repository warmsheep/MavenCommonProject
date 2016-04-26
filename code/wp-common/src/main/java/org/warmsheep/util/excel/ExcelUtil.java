package org.warmsheep.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.warmsheep.util.StringUtils;


/**
 * Excel导入工具类
 */
public class ExcelUtil {
	
	/**
	 * Excel文件内容读取
	 * @param input 文件读取流
	 * @param isExcel07 是否2007版本（true是；false否）
	 * @param startRow 数据读取开始行（默认从0开始）
	 * @param length 结果集大小
	 * @return
	 * @version 1.0,2015年12月4日 下午2:42:52,Liugl,Ins
	 */
	public static List<String[]> readExcel(InputStream input, boolean isExcel07, int startRow, int length) {
		List<String[]> data = new ArrayList<String[]>();
		try {
			Workbook wb  = null;
			//根据文件格式(2003或者2007)来初始化
			if(isExcel07)
				wb = new XSSFWorkbook(input);
			else
				wb = new HSSFWorkbook(input);
			Sheet sheet = wb.getSheetAt(0);				//获得第一个表单
			Iterator<Row> rows = sheet.rowIterator();	//获得第一个表单的迭代器
			String[] str = null;
			int idx = 0;
			
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
			
			while (rows.hasNext()) {
				Row row = rows.next();//获得行数据
				if (startRow > 0) {
					startRow--;
					continue;
				}
				Iterator<Cell> cells = row.cellIterator();//获得第一行的迭代器
				str = new String[length];
				idx = 0;
				
				while (cells.hasNext()) {
					Cell cell = cells.next();
					//根据cell中的类型来输出数据
					switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								//处理日期格式、时间格式
								if (null != cell.getCellStyle().getDataFormatString() && "yyyymmdd".equals(cell.getCellStyle().getDataFormatString()))
									str[idx] = sdfDate.format(cell.getDateCellValue());
								else if (null != cell.getCellStyle().getDataFormatString() && "hhmmss".equals(cell.getCellStyle().getDataFormatString()))
									str[idx] = sdfTime.format(cell.getDateCellValue());
							} else if (cell.getCellStyle().getDataFormat() == 58) {
								//处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
								final double value = cell.getNumericCellValue();  
								final Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
								str[idx] = formatDate.format(date);
							} else {
								//数值
								if (StringUtils.isNotNull(cell.getNumericCellValue()))
									str[idx] = String.valueOf(cell.getNumericCellValue());
							}
							break;
						case HSSFCell.CELL_TYPE_STRING:
							str[idx] = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							if (StringUtils.isNotNull(cell.getBooleanCellValue()))
								str[idx] = String.valueOf(cell.getBooleanCellValue());
							break;
						case HSSFCell.CELL_TYPE_FORMULA:
							str[idx] = cell.getCellFormula();
							break;
						default:
							System.out.println("UnSuported Cell Type "+">> "+idx);
						break;
					}//end switch
					idx++;
				}//end while
				
				//结果集组装
				data.add(str);
			}//end while
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
	public static void main(String[] args) throws Exception {
		String dir = "E:\\项目资料\\快钱对账文件_2015-12-23.xls";
		File file = new File(dir);
		InputStream input = new FileInputStream(file);
		List<String[]> data = readExcel(input, false, 1, 17);
		System.out.println("结果集大小:"+data.size());
		for (String[] s : data) {
			System.out.println(s[1]+"--|--"+s[11]);
		}
	}
	
}
