package com.frame.process.process.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frame.process.constants.GobalConstant.FileType;
import com.frame.process.process.interfaces.AbstractProcess;
import com.frame.process.utils.ThreadPoolUtils;
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
	
	@Transactional
	@Override
	public String datasToFilesConverter(String tableType) {
		String filePath = null;
		String fileName = getFileName(tableType);
		try {
			Queue<String> dbDatas = getDBDatas(tableType);
			if (dbDatas != null) {
				filePath = saveFile(fileName, dbDatas);
			}
			if (filePath != null) {
				// 开线程通知打包方法
				ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.getThreadPoolInstance();
				final String notifyFilePath = filePath;
				threadPoolExecutor.execute(new Runnable() {
					@Override
					public void run() {
						notifyPackFile(notifyFilePath);
					}
				});
			}
			return filePath;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("=== 当前数据文件: " + fileName + " 打包发生异常 ===");
		}
		return null;
	}

	@Override
	public void filesToDatasConverter(String filePath) {
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
			workbook = new HSSFWorkbook();
			break;
		case FileType.XLSX:
			workbook = new XSSFWorkbook();
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
        // 输出Excel全路径
        String allPath = fileLocation + "/" + path;
        OutputStream outputFile = new FileOutputStream(new File(allPath));
        workbook.write(outputFile);
        outputFile.close();
        workbook.close();
		return allPath;
	}
}
