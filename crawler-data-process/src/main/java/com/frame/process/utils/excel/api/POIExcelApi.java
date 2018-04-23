package com.frame.process.utils.excel.api;

import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import com.frame.process.utils.excel.api.callback.ExcelReaderCallBack;

/**
 * POI的excel读取实现
 * Created by zhh on 2018/04/21.
 */
public class POIExcelApi implements ExcelApi {

    private static final POIExcelApi instance = new POIExcelApi();

    private POIExcelApi() {
    }

    public static POIExcelApi getInstance() {
        return instance;
    }

    @Override
    public void read(InputStream inputStream, ExcelReaderCallBack callBack) throws Exception {
        Workbook wbs = null;
        wbs = WorkbookFactory.create(inputStream);
        int numberOfSheets = wbs.getNumberOfSheets();
        for (int k = 0; k < numberOfSheets; k++) {
            // 获取sheets
            Sheet sheet = wbs.getSheetAt(k);
            // 空sheet不读取
            if(sheet == null){
                continue;
            }
            // 判断当前Sheet中是否有合并的单元格
            Sheet shet = isMergedRegions(sheet);
            // 得到总行数     
            int rowNum = shet.getLastRowNum();
            Row row = shet.getRow(0);
            // 空row不读取
            if(row == null){
                continue;
            }
            int colNum = row.getPhysicalNumberOfCells();
            for (int i = 0; i <= rowNum; i++) {
                row = shet.getRow(i);
                if (row == null) continue;
                for (int j = 0; j < (colNum = row.getPhysicalNumberOfCells()); j++) {
                    if (callBack.isShutdown()) {
                        return;
                    }
                    Cell cell = row.getCell(j);
                    
                    // 创建单元格数据
                    ExcelReaderCallBack.CellContent cellContent = new ExcelReaderCallBack.CellContent();
                    cellContent.setCellProxy(cell);
                    cellContent.setFirst(j == 0);
                    cellContent.setLast(j == colNum - 1);
                    cellContent.setRow(i);
                    cellContent.setColumn(j);
                    
                    Object value = cell2Object(cell);
                    cellContent.setValue(value);
                    // 调用回掉
                    callBack.onCell(cellContent);
                }
            }
            callBack.sheetDone(shet);
        }
        callBack.done(wbs);
    }

    /**
     * 将单元格数据转为java对象
     *
     * @param cell 单元格数据
     * @return 对应的java对象
     */
    protected Object cell2Object(Cell cell) {
        if (cell == null)
            return "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case HSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            default:
                return "";
        }
    }

    public static Sheet isMergedRegions(Sheet sheet){
		Sheet seet = sheet;
		if(seet.getNumMergedRegions()>0){	// 判断当前的Sheet中是否有合并区域
			for (int x = 0; x < seet.getNumMergedRegions(); x++) {
				CellRangeAddress region = seet.getMergedRegion(x);  // 得到当前Sheet中的第x个合并区域
				
				int firstCol = region.getFirstColumn(); 		// 合并区域首列位置
				int lastCol = region.getLastColumn();			// 合并区域的尾列位置
			
				int firstRow = region.getFirstRow(); 			// 合并区域的首行位置
				int lastRow = region.getLastRow();				// 合并区域的尾行位置
			
				String cellData = seet.getRow(firstRow).getCell(firstCol).getStringCellValue();	// 获取当前合并单元格的值
				
				seet.removeMergedRegion(x);					// 移除当前Sheet的合并格式，让当前的合并区域也变成格式化的区域
				
				for(int y = firstCol; y <= lastCol;y++){		// 遍历合并的单元格
					for(int z = firstRow; z <= lastRow; z++){
					Cell cell = seet.getRow(z).getCell(y);		// 获取合并单元格中的一个小单元格
					cell.setCellValue(cellData);
					}
				}	
			}		
		}
		
		return seet;
}
}