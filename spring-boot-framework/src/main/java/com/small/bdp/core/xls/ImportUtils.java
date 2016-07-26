package com.small.bdp.core.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.small.bdp.framework.exception.BaseException;
import com.small.bdp.framework.exception.BizException;

/**
 * 导入excel工具类
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class ImportUtils<T> {

	/**
	 * 导入、导出excle常量
	 */
	public final static int ROWS_HEAD_NORMAL = 1;
	public final static int COLUMN_HEAD_NORMAL = 0;

	private static ImportUtils<?> importUtils = null;

	/** 私有构造方法 */
	private ImportUtils() {
	}

	/** T 的 class对象 */
	private static Class<?> tClass;

	/** 单例模式 */
	public static <T> ImportUtils<T> getInstance(Class<T> t) {
		tClass = t;
		if (null == importUtils)
			importUtils = new ImportUtils<T>();
		return (ImportUtils<T>) importUtils;
	}

	/** 初始化 设置需要设置的字段 */
	public List<Field> init() {
		List<Field> lstField = new ArrayList<Field>();
		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotations().length != 0 && field.getAnnotation(Import.class) != null) {
				lstField.add(field);
			}
		}
		return lstField;
	}

	/** 初始化 按excel中列的顺序和domain(或entity)中定义的order排序 */
	public List<CompareableFiled> initWithOrder() {
		List<CompareableFiled> lstField = new ArrayList<CompareableFiled>();
		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotations().length != 0 && field.getAnnotation(Import.class) != null) {
				Import im = field.getAnnotation(Import.class);
				CompareableFiled cf = new CompareableFiled();
				cf.setField(field);
				cf.setOrder(im.order());
				cf.setScale(im.scale());
				cf.setDisplayName(im.displayName());
				lstField.add(cf);
			}
		}
		Collections.sort(lstField);
		return lstField;
	}

	private Workbook createWorkbook(File file) throws IOException, FileNotFoundException {
		Workbook workbook;
		try {
			workbook = new XSSFWorkbook(file);
		} catch (Exception ex) {
			// 创建工作流
			InputStream ins = new FileInputStream(file);
			// byte[] buf = org.apache.commons.io.IOUtils.toByteArray(ins);
			workbook = new HSSFWorkbook(new POIFSFileSystem(ins));
		}
		return workbook;
	}

	/**
	 * 
	 * excelToListWithOrder:导入文件返回List自定义的顺序 <br/>
	 * excel中每列的顺序与类中定义的列order顺序一致
	 *
	 * @see Import
	 * @author Administrator
	 * @param file
	 * @param titleRows
	 * @param columnCount
	 *            excel中列的个数
	 * @return
	 * @throws Exception
	 * @since JDK 1.6
	 */
	public List<T> excelToListWithOrder(File file, int titleRows, int columnCount) throws Exception {
		// 初始化
		List<CompareableFiled> lstField = initWithOrder();
		List<T> list = new ArrayList<T>();

		// 创建工作薄
		Workbook workbook = createWorkbook(file);
		// 获取sheet数量
		// int sheetCount = workbook.getNumberOfSheets();
		int i = 0;
		int j = 0;
		int k = 0;
		int rowsCount = 0;
		Sheet sheet = null;
		Row rows = null;
		Cell cells = null;
		T entity = null;
		CompareableFiled fc = null;
		try {
			// 迭代sheet
			for (i = 0; i < 1; i++) {
				sheet = workbook.getSheetAt(i);
				// 总行数
				rowsCount = sheet.getLastRowNum() + 1 - sheet.getFirstRowNum();
				// 迭代行
				for (j = sheet.getFirstRowNum() + titleRows; j < rowsCount; j++) {
					rows = sheet.getRow(j);
					if (rows == null)
						continue;
					entity = (T) tClass.newInstance();
					// 列迭代
					for (k = 0; k < columnCount; k++) {

						cells = rows.getCell(k);
						if (null != cells) {
							fc = lstField.get(k);
							Field field = fc.getField();
							String methodName = field.getAnnotation(Import.class).name();
							if (StringUtils.isEmpty(methodName)) {
								methodName = "set" + StringUtils.capitalize(field.getName());
							}
							String value = getValue(cells, fc.getScale());
							// 值替换操作
							String[] replaces = field.getAnnotation(Import.class).replace();
							if (0 != replaces.length)
								for (int t = 0; t < replaces.length; t += 2) {
									if (value.equals(replaces[t])) {
										value = replaces[t + 1];
										break;
									}
								}
							Method method = tClass.getMethod(methodName, field.getType());
							try {
								method.invoke(entity, new Object[] { value.trim() });
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					list.add(entity);
				}
			}

			if (workbook != null) {
				workbook.close();
			}
		} catch (Exception e) {
			throw new BaseException(e.getMessage());
		}
		return list;
	}

	/**
	 * 
	 * checkFileTitle:检查模板<br/>
	 *
	 * @see Import
	 * @author Administrator
	 * @param file
	 * @param titleRows
	 * @param columnCount
	 *            excel中列的个数
	 * @return
	 * @throws Exception
	 * @since JDK 1.6
	 */
	public void checkFileTitle(File file, int columnCount) throws Exception {
		// 初始化
		List<CompareableFiled> lstField = initWithOrder();

		// 创建工作薄
		Workbook workbook = createWorkbook(file);
		int i = 0;
		int k = 0;
		Sheet sheet = null;
		Row rows = null;
		Cell cells = null;

		try {
			// 迭代sheet
			for (i = 0; i < 1; i++) {
				sheet = workbook.getSheetAt(i);

				rows = sheet.getRow(0);
				if (rows == null)
					continue;

				// 列迭代
				for (k = 0; k < columnCount; k++) {

					cells = rows.getCell(k);
					String value = getValue(cells, 0);
					if (!value.trim().equals(lstField.get(k).getDisplayName())) {
						throw new BizException("模板不正确，请检查模板");
					}
				}
			}

			if (workbook != null) {
				workbook.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException("模板不正确，请检查模板");
		}
	}

	/** 获取cell中的内容 */
	@SuppressWarnings("static-access")
	private static String getValue(Cell cell, int scale) {
		if (cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
			}
			cell.setCellType(Cell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		} else {
			return cell.getStringCellValue();
		}
	}
}