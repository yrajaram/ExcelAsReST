package com.herakles.service.api;

import javax.jws.WebService;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.herakles.service.domain.ExcelResponse;

/**
 * Get data in a specific row or column or cell of an XL spread sheet which is in JXL supported format.
 * Usage: app-context/XL-Name/Sheet-Name-or-number/ColumnNumber/RowNumber?debug=true
 * 		  Sheet Name/Number can be -1 to fetch data from all sheets
 *        ColumnNumber can be -1 to fetch data from all columns
 *        RowNumber can be -1 to fetch data from all rows
 *        debug can take values true/false <optional>
 * 
 * @author yrajaram
 */
@WebService
public interface Excel {
	/**
	 * Get data in a specific row or column or cell of an XL spread sheet.
	 *        
	 * @param name
	 * @param sheet
	 * @param column
	 * @param row
	 * @param debug=true|false
	 * @return
	 */
	@Path("/{name}/{sheet}/{column}/{row}")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	ExcelResponse getData(
			@PathParam("name") String name,
			@PathParam("sheet") String sheet,
			@PathParam("column") String column,
			@PathParam("row") String row,
			@DefaultValue("false") @QueryParam("debug") boolean debug
			);
}
