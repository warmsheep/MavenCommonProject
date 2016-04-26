package org.warmsheep.util.export.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.warmsheep.util.export.DataExportor;
import org.warmsheep.util.export.DataField;
import org.warmsheep.util.export.ExportDataSource;
import org.warmsheep.util.export.csv.CsvDataExportor;
import org.warmsheep.util.string.StringTools;

/**
 * 描述: Excel数据导出工具,默认为CSV格式,CSV格式文件可以用excel正常打开(兼容03和07+) 如果指定为Excel,则导出Excel03
 * 
 */
public class ExcelDataExportor<T> extends DataExportor<T> {
	private MODE mode;

	private String sheetName;

	public ExcelDataExportor(DataField[] fields, ExportDataSource<T> dataSource, OutputStream os) {
		this(fields, dataSource, os, MODE.CSV);
	}

	public ExcelDataExportor(DataField[] fields, ExportDataSource<T> dataSource, OutputStream os, MODE mode) {
		super(fields, dataSource, os);
		this.mode = mode;
	}

	@Override
	public void export() throws IOException {
		if (mode == null || mode == MODE.CSV) {
			new CsvDataExportor<T>(fields, dataSource, os).export();
		} else {
			try {
				exportExcel07();
			} catch (RowsExceededException e) {
				throw new RuntimeException(e);
			} catch (WriteException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void exportExcel07() throws IOException, RowsExceededException, WriteException {
		Collection<?> dataList;
		WritableWorkbook workBook = Workbook.createWorkbook(os);
		int sheetIndex = 1;
		WritableSheet sheet = workBook.createSheet(createSheetName(sheetIndex), 0);
		int rowIndex = 1;
		createHead(sheet);
		Object value;
		Label label;
		do {
			dataList = dataSource.getData();
			for (Object data : dataList) {
				int count = 0;
				if (isUseIndex()) {
					label = new Label(0, rowIndex, "" + rowIndex);
					sheet.addCell(label);
					count++;
				}
				for (int i = 0 + count; i < fields.length + count; i++) {
					value = getValue(data, fields[i - count].getField());
					if (value != null) {
						label = new Label(i, rowIndex, value.toString());
						sheet.addCell(label);
					}
				}
				rowIndex++;
			}
		} while (rowIndex <= dataList.size());
		workBook.write();
		workBook.close();
		os.flush();
	}

	/**
	 * 将指定行创建为表头
	 * 
	 * @param row
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private void createHead(WritableSheet sheet) throws RowsExceededException, WriteException {
		DataField field;
		int count = 0;
		Label label;
		if (isUseIndex()) {
			label = new Label(0, 0, "序号");
			sheet.addCell(label);
			// sheet.setColumnView(0, 20);
			count++;
		}
		for (int i = 0 + count; i < fields.length + count; i++) {
			field = fields[i - count];
			label = new Label(i, 0, field.getName());
			sheet.addCell(label);
			// if (StringUtils.isNotNull(field.getColumnWidth())) {
			// sheet.setColumnView(i, field.getColumnWidth());
			// }
			continue;
		}
	}

	private String createSheetName(int index) {
		if (StringTools.isEmpty(sheetName)) {
			this.sheetName = "sheet";
		}
		return this.sheetName + index;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
