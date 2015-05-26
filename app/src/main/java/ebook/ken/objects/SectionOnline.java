package ebook.ken.objects;

import java.io.Serializable;

public class SectionOnline implements Serializable {

	private int sectionId		= 0;
	private String sectionName	= "";
	
	
	////////////////////////////////////////////////////////////////////////////////
	
	public int getSectionId() {
		return sectionId;
	}
	
	public SectionOnline setSectionId(int sectionId) {
		this.sectionId = sectionId;
		return this;
	}
	
	public String getSectionName() {
		return sectionName;
	}
	
	public SectionOnline setSectionName(String sectionName) {
		this.sectionName = sectionName;
		return this;
	}

}
