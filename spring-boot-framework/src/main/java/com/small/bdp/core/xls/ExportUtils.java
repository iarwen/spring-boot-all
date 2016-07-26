package com.small.bdp.core.xls;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.util.StringUtils;

/**
 * excel 工具类
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class ExportUtils<T> {
	private static ExportUtils excelUtils = null;

	/** 私有构造方法 */
	private ExportUtils() {
	}

	/** T 的 class对象 */
	private static Class<?> tClass;

	/** 单例模式 */
	public static <T> ExportUtils<T> getInstance(Class clz) {
		tClass = clz;
		if (null == excelUtils)
			excelUtils = new ExportUtils<T>();
		return excelUtils;
	}

	/** 表头、导出字段的方法 */
	private List<String> headers = null;
	private List<Method> methods = null;
	/** 声明一个工作薄 */
	private HSSFWorkbook workbook = new HSSFWorkbook();
	/** 生成一个表格 */
	private HSSFSheet sheet = null;
	private HSSFRow row = null;

	/** 设置excel表头 */
	private void setTitle(String title, String xlsTitle) {
		// 声明一个工作薄
		workbook = new HSSFWorkbook();
		// 生成一个表格
		sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);

		// 产生表格标题行
		row = sheet.createRow(0);
		row.setHeight((short) 400);// 设置高度
		// 设置内容头部
		if (!StringUtils.isEmpty(xlsTitle)) {
			HSSFCell cell = null;

			sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (headers.size() - 1)));

			cell = row.createCell(0);
			cell.setCellStyle(getStyle4(workbook));
			cell.setCellValue(xlsTitle);

			// 创建下一行
			row = sheet.createRow(1);
		}
		for (short i = 0; i < headers.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(getStyle1(workbook));
			HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
			cell.setCellValue(text);
		}
	}

	/** 设置excel表头 */
	private void setSheetTitle(String title, String xlsTitle) {
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 产生表格标题行
		row = sheet.createRow(0);
		// 设置内容头部
		if (!StringUtils.isEmpty(xlsTitle)) {
			HSSFCell cell = null;
			sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (headers.size() - 1)));
			cell = row.createCell(0);
			cell.setCellStyle(getStyle4(workbook));
			cell.setCellValue(xlsTitle);
			// 创建下一行
			row = sheet.createRow(1);
		}
		for (short i = 0; i < headers.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(getStyle1(workbook));
			HSSFRichTextString text = new HSSFRichTextString(headers.get(i));
			cell.setCellValue(text);
		}
	}

	/** 包含有集合列的导出 */
	public void multipleExport(Collection<T> coll, String title, OutputStream out) throws Exception {
		headers = new ArrayList<String>();

		if (null != coll && 0 != coll.size()) {
			// 设置表头
			setMultipleTitle(coll.iterator().next(), headers);
			// 设置excel表头
			setTitle(title, null);
			// 设置excel内容
			int ord = 0;
			Iterator<T> itor = coll.iterator();
			while (itor.hasNext()) {
				// 序号
				ord++;
				row = sheet.createRow(ord);
				T t = (T) itor.next();
				int index = 0;
				setContents(t, index);
			}
		}
		// 将workbook写入流out
		workbook.write(out);
		// 关闭流
		out.close();
	}

	/** 包含有集合列的导出,有多个Sheet的导出 */
	public void multipleSheetExport(List<Collection<T>> colls, String[] title, OutputStream out) throws Exception {
		// 声明一个工作薄
		workbook = new HSSFWorkbook();
		for (int i = 0, collSize = colls.size(); i < collSize; i++) {
			Collection<T> coll = colls.get(i);
			sheet = workbook.createSheet(title[i]);
			headers = new ArrayList<String>();
			// 生成一个表格
			if (null != coll && 0 != coll.size()) {
				setMultipleTitle(coll.iterator().next(), headers);
				// 设置excel表头
				setSheetTitle(title[i], null);
				// 设置excel内容
				int ord = 0;
				Iterator<T> itor = coll.iterator();
				while (itor.hasNext()) {
					// 序号
					ord++;
					row = sheet.createRow(ord);
					T t = (T) itor.next();
					int index = 0;
					setContents(t, index);
				}
			}
		}
		// 将workbook写入流out
		workbook.write(out);
		// 关闭流
		out.close();
	}

	/** 设置excel内容 */
	private void setContents(Object obj, int index) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		// HSSFFont font3 = workbook.createFont();
		// font3.setColor(HSSFColor.BLACK.index);
		HSSFCellStyle style = getStyle2(workbook);
		// 设置内容
		for (Field field : fields) {
			if (field.getAnnotations().length != 0 && field.getAnnotation(Export.class) != null) {
				String propertyName = field.getName();
				String methodName = "get" + StringUtils.capitalize(propertyName);
				Method method = obj.getClass().getMethod(methodName);
				Object value = method.invoke(obj, new Object[] {});
				// 特殊类型区分
				switch (field.getAnnotation(Export.class).type()) {
				case ZERO: {
					HSSFCell cell = row.createCell(index++);
					cell.setCellStyle(style);
					String val = null != value ? value.toString() : "";
					// 日期截取
					if (value instanceof java.util.Date)
						val = val.substring(0, 10);
					// 替换操作
					if (!StringUtils.isEmpty(val)) {
						String[] replaces = field.getAnnotation(Export.class).replace();
						if (0 != replaces.length) {
							for (int t = 0; t < replaces.length; t += 2) {
								if (val.equals(replaces[t])) {
									val = replaces[t + 1];
									break;
								}
							}
						}
					}
					HSSFRichTextString richString = new HSSFRichTextString(val);
					// richString.applyFont(font3);
					cell.setCellValue(richString);
					break;
				}
				case ONE: {
					if (null != value)
						setContents(value, index++);
					else
						index++;
					break;
				}
				case TWO: {
					if (Fetch.ONETOONE == field.getAnnotation(Export.class).fetch()) {
						Collection coll = (Collection) value;
						if (null != coll && 0 != coll.size()) {
							Iterator itor = coll.iterator();
							while (itor.hasNext()) {
								Object o = itor.next();
								if (null != value)
									setContents(o, index++);
								else
									index++;
							}
						}
					} else {
						Collection coll = (Collection) value;
						String textValue = "";
						if (null != coll && 0 != coll.size()) {
							Iterator itor = coll.iterator();
							String mn = "get" + StringUtils.capitalize(field.getAnnotation(Export.class).name());
							while (itor.hasNext()) {
								Object o = itor.next();
								Method m = o.getClass().getMethod(mn);
								Object oo = m.invoke(o, new Object[] {});
								textValue += (null != oo ? oo.toString() : "") + " ";
							}
						}
						HSSFCell cell = row.createCell(index++);
						cell.setCellStyle(style);
						HSSFRichTextString richString = new HSSFRichTextString(textValue);
						// richString.applyFont(font3);
						cell.setCellValue(richString);
					}
					break;
				}
				case THREE: {
					HSSFCell cell = row.createCell(index++);
					cell.setCellStyle(style);

					String mn = "get" + StringUtils.capitalize(field.getAnnotation(Export.class).name());
					for (int i = 0; i < fields.length; i++) {
						if (field.getAnnotation(Export.class).name().equals(fields[i].getName())) {
							field = fields[i];
						}
					}
					propertyName = field.getName();
					methodName = "get" + StringUtils.capitalize(propertyName);
					method = obj.getClass().getMethod(methodName);
					value = method.invoke(obj, new Object[] {});
					String val = null != value ? value.toString() : "";
					// 日期截取
					if (value instanceof java.util.Date)
						val = val.substring(0, 10);
					// 替换操作
					if (!StringUtils.isEmpty(val)) {
						String[] replaces = field.getAnnotation(Export.class).replace();
						if (0 != replaces.length) {
							for (int t = 0; t < replaces.length; t += 2) {
								if (val.equals(replaces[t])) {
									val = replaces[t + 1];
									break;
								}
							}
						}
					}
					HSSFRichTextString richString = new HSSFRichTextString(val);
					// richString.applyFont(font3);
					cell.setCellValue(richString);
					break;
				}
				case FOUR: {
					break;
				}
				default: {

				}
				}
			}
		}
	}

	/** 简单导出 */
	public void simpleExport(Collection<T> coll, String title, OutputStream out) throws Exception {
		simpleExport(coll, title, null, out);
	}

	/** 简单导出 */
	public void simpleExport(Collection<T> coll, String title, String xlsTitle, OutputStream out) throws Exception {
		// 设置标题和需求导出字段方法
		setSimpleTitle();
		// 设置excel表头
		setTitle(title, xlsTitle);
		// 设置内容
		if (null != coll && 0 != coll.size()) {
			HSSFFont font3 = workbook.createFont();
			font3.setColor(HSSFColor.BLACK.index);
			HSSFCellStyle style = getStyle2(workbook);
			int ord = 0;
			if (!StringUtils.isEmpty(xlsTitle))
				ord = 1;
			Iterator<T> itor = coll.iterator();
			while (itor.hasNext()) {
				// 序号
				ord++;
				row = sheet.createRow(ord);
				T t = (T) itor.next();
				int index = 0;
				for (Method method : methods) {
					sheet.setColumnWidth(index, 5000);
					sheet.setDefaultColumnStyle(index, style);

					HSSFCell cell = row.createCell(index++);

					cell.setCellStyle(style);
					Object obj = method.invoke(t, new Object[] {});
					HSSFRichTextString richString = new HSSFRichTextString(null != obj ? obj.toString() : "");
					richString.applyFont(font3);
					cell.setCellValue(richString);
				}
			}
		}
		workbook.write(out);
		// 关闭流
		out.close();
	}

	/** 简单导出+字段排序 */
	public void simpleExportWithOrder(Collection<T> coll, String title, String xlsTitle, OutputStream out) throws Exception {
		// 设置标题和需求导出字段方法
		setSimpleTitleWithOrder();
		// 设置excel表头
		setTitle(title, xlsTitle);
		// 设置内容
		if (null != coll && 0 != coll.size()) {
			HSSFFont font3 = workbook.createFont();
			font3.setColor(HSSFColor.BLACK.index);
			HSSFCellStyle style = getStyle2(workbook);
			int ord = 0;
			if (!StringUtils.isEmpty(xlsTitle))
				ord = 1;
			Iterator<T> itor = coll.iterator();
			while (itor.hasNext()) {
				// 序号
				ord++;
				row = sheet.createRow(ord);
				T t = (T) itor.next();
				int index = 0;
				for (Method method : methods) {
					HSSFCell cell = row.createCell(index++);
					cell.setCellStyle(style);
					Object obj = method.invoke(t, new Object[] {});
					HSSFRichTextString richString = new HSSFRichTextString(null != obj ? obj.toString() : "");
					richString.applyFont(font3);
					cell.setCellValue(richString);
				}
			}
		}
		workbook.write(out);
		// 关闭流
		out.close();
	}

	/** 复合导出设置表头 */
	private void setMultipleTitle(Class claz, List headers) throws Exception {
		Field[] fields = claz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotations().length != 0 && field.getAnnotation(Export.class) != null) {
				// 设置表头集合
				switch (field.getAnnotation(Export.class).type()) {
				case ZERO: {
					headers.add(field.getAnnotation(Export.class).title());
				}
				case TWO: {
					if (Fetch.ONETOONE == field.getAnnotation(Export.class).fetch()) {
						String propertyName = field.getName();
						String methodName = "get" + StringUtils.capitalize(propertyName);
						Method method = claz.getMethod(methodName);
						Class cla = method.getReturnType();
						// 递归调用
						setMultipleTitle(cla, headers);
					} else {
						headers.add(field.getAnnotation(Export.class).title());
					}
					break;
				}
				case THREE: {
					// headers.add(field.getAnnotation(Export.class).title());
					String propertyName = field.getName();
					String methodName = "get" + StringUtils.capitalize(propertyName);
					Method method = claz.getMethod(methodName);
					Object value = method.invoke(claz, new Object[] {});
					headers.add(value);
					break;
				}
				case FOUR: {
					break;
				}
				default: {
					String propertyName = field.getName();
					String methodName = "get" + StringUtils.capitalize(propertyName);
					Method method = claz.getMethod(methodName);
					Class cla = method.getReturnType();
					// 递归调用
					setMultipleTitle(cla, headers);
					break;
				}
				}
			}
		}
	}

	/** 复合导出设置表头 */
	private void setMultipleTitle(Object obj, List headers) throws Exception {
		Class claz = obj.getClass();
		Field[] fields = claz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotations().length != 0 && field.getAnnotation(Export.class) != null) {
				// 设置表头集合
				switch (field.getAnnotation(Export.class).type()) {
				case ZERO: {
					headers.add(field.getAnnotation(Export.class).title());
					break;
				}
				case TWO: {
					if (Fetch.ONETOONE == field.getAnnotation(Export.class).fetch()) {
						String propertyName = field.getName();
						String methodName = "get" + StringUtils.capitalize(propertyName);
						Method method = claz.getMethod(methodName);
						Collection coll = (Collection) method.invoke(obj, new Object[] {});
						if (coll != null) {
							Iterator itor = coll.iterator();
							while (itor.hasNext()) {
								Object o = itor.next();
								// 递归调用
								setMultipleTitle(o, headers);
							}
						}

					} else {
						headers.add(field.getAnnotation(Export.class).title());
					}
					break;
				}
				case THREE: {
					// headers.add(field.getAnnotation(Export.class).title());
					String propertyName = field.getName();
					String methodName = "get" + StringUtils.capitalize(propertyName);
					Method method = claz.getMethod(methodName);
					Object value = method.invoke(obj, new Object[] {});
					headers.add(value);
					break;
				}
				case FOUR: {
					break;
				}
				default: {
					String propertyName = field.getName();
					String methodName = "get" + StringUtils.capitalize(propertyName);
					Method method = claz.getMethod(methodName);
					Class cla = method.getReturnType();
					// 递归调用
					setMultipleTitle(cla, headers);
					break;
				}
				}
			}
		}
	}

	/** 简单表头设置 */
	private void setSimpleTitle() throws Exception {
		Field[] fields = tClass.getDeclaredFields();
		methods = new ArrayList<Method>();
		headers = new ArrayList<String>();
		for (Field field : fields) {
			if (field.getAnnotations().length != 0 && field.getAnnotation(Export.class) != null) {
				String propertyName = field.getName();
				String methodName = "get" + StringUtils.capitalize(propertyName);
				Method m = tClass.getMethod(methodName);
				headers.add(field.getAnnotation(Export.class).title());
				methods.add(m);
			}

		}
	}

	/** 简单表头设置+字段排序 */
	private void setSimpleTitleWithOrder() throws Exception {
		List<CompareableFiled> lstField = new ArrayList<CompareableFiled>();

		Field[] fields = tClass.getDeclaredFields();
		methods = new ArrayList<Method>();
		headers = new ArrayList<String>();
		for (Field field : fields) {
			if (field.getAnnotations().length != 0 && field.getAnnotation(Export.class) != null) {
				Export im = field.getAnnotation(Export.class);
				CompareableFiled cf = new CompareableFiled();
				cf.setField(field);
				cf.setOrder(im.order());
				lstField.add(cf);
			}
		}
		Collections.sort(lstField);
		for (CompareableFiled cf : lstField) {
			String propertyName = cf.getField().getName();
			String methodName = "get" + StringUtils.capitalize(propertyName);
			Method m = tClass.getMethod(methodName);
			headers.add(cf.getField().getAnnotation(Export.class).title());
			methods.add(m);
		}
	}

	/**
	 * 对日期格式处理
	 * 
	 * @param obj
	 * @return
	 */
	// private String dateFormat(Object obj){
	//
	// }
	/** 是否为特殊类型 */
	/*
	 * private boolean isSpecialType(Object value){ if(value instanceof String){
	 * return false; }else if (value instanceof Integer) { return false; } else
	 * if (value instanceof Float) { return false; } else if (value instanceof
	 * Double) { return false; } else if (value instanceof Long) { return false;
	 * } else if (value instanceof Boolean) { return false; } else if (value
	 * instanceof Date) { return false; } else if (value instanceof byte[]) {
	 * return false; } return true;
	 * 
	 * }
	 */

	/** 表头样式1 */
	private HSSFCellStyle getStyle1(HSSFWorkbook workbook) {
		// 定义样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 定义字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		style.setFont(font);
		return style;
	}

	/** 样式2 */
	public static HSSFCellStyle getStyle2(HSSFWorkbook workbook) {
		// 定义样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		// 字体
		// HSSFFont font2 = workbook.createFont();
		// font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		/**
		 * 文本格式
		 */
		HSSFDataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat("@"));

		// style.setFont(font2);
		return style;
	}

	/** 样式3 */
	public static HSSFCellStyle getStyle3(HSSFWorkbook workbook) {
		// 定义样式
		HSSFCellStyle style = workbook.createCellStyle();
		// style.setFillForegroundColor(HSSFColor.WHITE.index);
		// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		// style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		style.setFont(font2);
		return style;
	}

	/** 样式4 */
	public static HSSFCellStyle getStyle4(HSSFWorkbook workbook) {
		// 定义样式
		HSSFCellStyle style = workbook.createCellStyle();
		// style.setFillForegroundColor(HSSFColor.WHITE.index);
		// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		// style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		// style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

		style.setFont(font2);
		return style;
	}

	/** 样式5 */
	public static HSSFCellStyle getStyle5(HSSFWorkbook workbook) {
		// 定义样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font2.setFontHeight((short) 240);

		style.setFont(font2);
		return style;
	}
}
