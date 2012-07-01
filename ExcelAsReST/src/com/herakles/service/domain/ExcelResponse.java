package com.herakles.service.domain;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The response object that will be returned to the caller.
 * 
 * @author yrajaram
 */
@XmlRootElement
public class ExcelResponse{
	
	private Map<String,String> response;
	private Map<String,String> debugInfo;
	
	public void setDebugInfo(Map<String, String> map) {
		this.debugInfo = map;
	}
	public Map<String, String> getDebugInfo() {
		return debugInfo;
	}

	public Map<String,String> getResponse() {
		return response;
	}

	public void setResponse(Map<String,String> name) {
		this.response = name;
	}
}
