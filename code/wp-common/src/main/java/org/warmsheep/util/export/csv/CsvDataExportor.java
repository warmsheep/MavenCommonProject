package org.warmsheep.util.export.csv;

import java.io.OutputStream;

import org.warmsheep.util.export.DataField;
import org.warmsheep.util.export.ExportDataSource;
import org.warmsheep.util.export.txt.TxtDataExportor;

/**
 * 描述: csv格式数据导出工具
 *
 */
public class CsvDataExportor<T> extends TxtDataExportor<T> {
	public CsvDataExportor(DataField[] fields, ExportDataSource<T> dataSource,OutputStream os) {
		super(fields, dataSource, os,",");
	}
}
