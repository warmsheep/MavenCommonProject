package org.warmsheep.util.excel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelRead {
	/**
	 * 从excel文件中读取内容以第一行为key，从第二行开始为正式数据
	 * @param file 文件
	 * @param sheetIndex
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public static final List<Map<String, String>> readSheet(File file, int sheetIndex) throws BiffException, IOException {
		Workbook workbook = Workbook.getWorkbook(file);
		jxl.Sheet sheet = workbook.getSheet(0);
		int rowSize = sheet.getRows();
		Cell[] headCell = sheet.getRow(0);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>(rowSize - 1);
		Cell[] cells;
		for (int i = 1; i < rowSize; i++) {
			cells = sheet.getRow(i);
			Map<String, String> data = new HashMap<String, String>();
			for (int j = 0; j < headCell.length; j++) {
				if (cells.length > j) {
					if (cells[j].getType() == CellType.DATE) { // TODO:时间类型读取
						DateCell dc = (DateCell) cells[j];
						Date date = dc.getDate();
						TimeZone zone = TimeZone.getTimeZone("GMT");
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						sdf.setTimeZone(zone);
						String dateStr = sdf.format(date);
						data.put(trim(headCell[j].getContents()), dateStr);
					} else {
						data.put(trim(headCell[j].getContents()), trim(cells[j].getContents()));
					}
				}
			}
			result.add(data);
		}
		return result;
	}
	private static final String trim(String str) {
		return str == null ? "" : str.trim();
	}
}
