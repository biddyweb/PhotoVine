package com.brennan_hzl.photovine.bean;

import java.io.Serializable;


/**
 * ImageµÄ
 * 
 * @author len
 *
 */
public class ImageBean implements Serializable, Cloneable {

	  private static final long serialVersionUID = -1308498613439106644L;
	  private static String SCHEME = "file://";
	  public boolean original = true;
	  public String imageUri;
	  public String imagePath;
	  public boolean seleted = false;
	  
	  public ImageBean(String path) {
		  imagePath = path;
		  imageUri = SCHEME + imagePath;
	  }
	  
	  public boolean getoriginal() {
		  return original;
	  }
	  
	  public void setFlag(boolean original) {
		  this.original = original;
	  }
	  
	  public String getImageUri() {
		  return this.imageUri;
	  }
	  
	  public void setIamgeUri(String uri) {
		  imageUri = uri;
	  }
	  
	  public String getImagePath() {
		  return this.imagePath;
	  }
	  
	  public void setIamgePath(String path) {
		  imagePath = path;
	  }
	  
	  public Boolean getSelectd() {
		  return this.seleted;
	  }
	  
	  public void setSelectd(boolean flag) {
		  seleted = flag;
	  }
	  
	  public void changeSelectState() {
		  seleted = !seleted;
	  }
	  
	  public Object clone() throws CloneNotSupportedException {
	      return super.clone();
	  }
	
}
