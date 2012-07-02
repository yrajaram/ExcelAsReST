package com.herakles.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

import com.herakles.service.api.Excel;
import com.herakles.service.domain.ExcelResponse;

/**
 * This is the API implementation.
 * 
 * @author yrajaram
 *
 */
@WebService(endpointInterface = "com.herakles.service.api.Excel")
@Path("/")
public class ExcelImpl implements Excel{
	private @Context ServletContext context;
	
	public ExcelImpl (@Context HttpHeaders headers){
		super();
	}
	
	public ExcelImpl (){
		super();
	}

	public ExcelResponse getData(String name, boolean debug) {
		return getData(name,"-1",debug);
	}

	public ExcelResponse getData(String name, String sheet, boolean debug) {
		return getData(name, sheet, "-1", "-1", debug);
	}
	
	/**
	 * If sheet == -1 get all sheets
	 * If row == -1 get entire row
	 * If column == -1 get entire column
	 */
	public ExcelResponse getData(String name, String sheetName, String column,	String row, boolean debug) {
		System.out.println("[Debug 1] XL Name:"+name+" Sheet:"+sheetName+" Column:"+column+" Row:"+row);
		// Checking for invalid inputs
		if (name.equalsIgnoreCase("") || name==null){
			name = "XL.xls"; 
		}
		if (sheetName.equalsIgnoreCase("") || sheetName==null){
			sheetName = "-1"; 
		}
		if (column.equalsIgnoreCase("") || column==null){
			column = "-1"; 
		}
		if (row.equalsIgnoreCase("") || row==null){
			sheetName = "-1"; 
		}

		ExcelResponse ret = new ExcelResponse();
		Map<String, String> data = new HashMap<String, String>();
		int requestedCol = -1;
		int requestedRow = -1;
		try {
			requestedCol = Integer.parseInt(column);
			requestedRow = Integer.parseInt(row);
		} catch (NumberFormatException e ) {
			System.err.println(e.getMessage());
			//hmmm... looks like we didn't get a number
			column="-1";
			row="-1";
		}

		System.out.println("[Debug 2] XL Name:"+name+" Sheet:"+sheetName+" Column:"+column+" Row:"+row+
				" requestedCol:"+requestedCol+" requestedRow:"+requestedRow);

		try {
			InputStream is = context.getResourceAsStream("WEB-INF/"+name);
			System.out.println("XL URL object:"+is);

			Workbook w = Workbook.getWorkbook(is);
			Sheet sheet, tmp = null;

			try {
				int sId = Integer.parseInt(sheetName);
				if (sId != -1) 
					tmp = w.getSheet(sId);
			} catch (NumberFormatException e ) {
				//hmmm... looks like we didn't get a number, so let it be
				tmp = w.getSheet(sheetName);
			}
			if (tmp!=null)
				System.out.println("[Debug] tmp:"+tmp.getName());

			for (int k=0; k < w.getNumberOfSheets(); k++) {
				sheet = w.getSheet(k);
				if (!sheetName.equalsIgnoreCase("-1")) {
					if ( !sheet.equals(tmp) )	continue;
				}

				for (int j = 0; j < sheet.getColumns(); j++) {
					if (!column.equalsIgnoreCase("-1")) {
						if ( requestedCol!=j) continue; 
					}
					for (int i = 0; i < sheet.getRows(); i++) {
						if (!row.equalsIgnoreCase("-1") ){
							if (requestedRow!=i) continue; 
						}
						fetchData(data, sheet, j, i);
					}
				}
			}
		} catch (Throwable e) {
			System.err.println("****Exception****");
			e.printStackTrace();
			data.put("Exception", e.toString());
		} finally {
			ret.setResponse(data);
			if (debug){
				System.out.println("Enabling debug instrumentation");
				Map<String, String> params = new HashMap<String, String>();
				// Do what you want to do if this flag is on
				params.put("Debug", "Enabled");
				ret.setDebugInfo(params);
				}
		}
		return ret;
	}
	
	private void fetchData(Map<String, String> data, Sheet sheet, int j, int i) {
		System.out.println("[Debug] fetchData invoked with sheet:"+sheet.getName()+" column:"+j+" row:"+i);
		Cell cell = sheet.getCell(j, i);
		CellType type = cell.getType();
		if (type == CellType.LABEL) {
			System.out.println("I got a label "	+ cell.getContents());
		}

		if (type == CellType.NUMBER) {
			System.out.println("I got a number " + cell.getContents());
		}
		data.put(sheet.getName()+" R"+i+" C"+j, cell.getContents());
	}
}
