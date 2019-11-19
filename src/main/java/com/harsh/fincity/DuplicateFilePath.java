package com.harsh.fincity;

import java.util.ArrayList;
import java.util.List;

public class DuplicateFilePath {
	private String OrgFilePath;
	private List <String> duplicateFilePath = new ArrayList<String>();
	
	public String getOrgFilePath() {
		return OrgFilePath;
	}
	public void setOrgFilePath(String orgFilePath) {
		OrgFilePath = orgFilePath;
	}
	public List<String> getDuplicateFilePath() {
		return duplicateFilePath;
	}
	public void setDuplicateFilePath(List<String> duplicateFilePath) {
		this.duplicateFilePath = duplicateFilePath;
	}
	

}
