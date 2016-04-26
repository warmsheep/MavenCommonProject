package org.warmsheep.util.export;

import java.util.List;



/**
 * 描述: 数据导出,数据源
 *
 */
public interface ExportDataSource<T>{
	<T> List<T> getData();
}
