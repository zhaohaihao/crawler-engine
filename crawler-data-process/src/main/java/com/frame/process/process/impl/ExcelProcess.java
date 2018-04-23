package com.frame.process.process.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Queue;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.frame.process.constants.GobalConstant.*;
import com.frame.process.process.interfaces.AbstractProcess;
/**
 * 
 * Created by zhh on 2018/04/20.
 */
@Service
public class ExcelProcess extends AbstractProcess {
	
	// 默认高版本 excel 格式
	private String suffix = FileType.XLSX;
	
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public void datasToFilesConverter(String tableType) {
		String fileName = getFileName(tableType);
		try {
			saveFile(fileName, getDBDatas(tableType));
		} catch (Exception e) {
			logger.info("=== 当前数据文件: " + fileName + " 打包发生异常 ===");
			e.printStackTrace();
		}
	}

	@Override
	public void filesToDatasConverter(String filePath) {
		filePath = "C:/Users/Administrator/Desktop/test/TieBaContent_e3fc17b980984c758804ea71faf087cd.xlsx";
		String tableType = getTableType(filePath);
		try {
			List fileDatas = getFileDatas(filePath, tableType);
			saveFileDatas(fileDatas, tableType);
		} catch (Exception e) {
			logger.info("=== 当前数据文件: " + filePath + " 解包入库发生异常 ===");
			e.printStackTrace();
		}
	}

	@Override
	public String saveFile(String fileName, Queue<String> datas) throws Exception {
		// 创建 Workbook 对象(excel的文档对象)
		Workbook workbook;
		switch (suffix) {
		case FileType.XLS:
			workbook = ExcelVersion.XLS.getWorkbook();
			break;
		case FileType.XLSX:
			workbook = ExcelVersion.XLSX.getWorkbook();
			break;
		default:
			// TODO: 文件类型异常
			return null;
		}
		// 创建Sheet对象
		Sheet sheet = workbook.createSheet("sheet0");
        // 在sheet里创建第二行
        Row row = sheet.createRow(0);
        for (int i = 1, j = 0; !datas.isEmpty(); ) {
            String poll = datas.poll();
            if (LINE_BREAK.equals(poll)) {
                row = sheet.createRow(i);
                i++;
                j = 0;
            } else {
                row.createCell(j).setCellValue(poll);
                j++;
            }
        }
        // 输出Excel文件
        String path = fileName + suffix;
        OutputStream outputFile = new FileOutputStream(new File(fileLocation + "/" + path));
        workbook.write(outputFile);
        outputFile.close();
		return path;
	}
	
	// Excel 版本枚举
	public enum ExcelVersion {
		
		XLS(FileType.XLSX, new HSSFWorkbook()),	// 03及03以前更低版本
		XLSX(FileType.XLSX, new XSSFWorkbook());	// 07及07以后更高版本
		
		// 后缀
		private String suffix;
		// 工作簿类型
		private Workbook workbook;

		private ExcelVersion(String suffix, Workbook workbook) {
			this.suffix = suffix;
			this.workbook = workbook;
		}

		public String getSuffix() {
			return suffix;
		}

		public void setSuffix(String suffix) {
			this.suffix = suffix;
		}

		public Workbook getWorkbook() {
			return workbook;
		}

		public void setWorkbook(Workbook workbook) {
			this.workbook = workbook;
		}
	}
}
