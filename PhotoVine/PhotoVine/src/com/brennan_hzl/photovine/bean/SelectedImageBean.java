package com.brennan_hzl.photovine.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ImageµÄ
 * 
 * @author len
 *
 */
public class SelectedImageBean implements Serializable, Cloneable {

	  private static final long serialVersionUID = -1;
	  private List<ImageBean> selectedImages = new ArrayList<ImageBean>(); 
	  private static SelectedImageBean mInstance;
	  
	  public SelectedImageBean() {}
	  
	  public static SelectedImageBean getInstance() {
		  if (mInstance == null) {
			  mInstance = new SelectedImageBean();
		  }
		  
		  return mInstance;
	  }
	  
	  public void addAllImage(List<ImageBean> paramList) {
		  selectedImages.addAll(paramList);
	  }

	  public void addImage(ImageBean paramImage) {
		  paramImage.setSelectd(true);
		  this.selectedImages.add(paramImage);
	  }

	  public void clearAllImages() {
		  clearAllSelectedState();
		  this.selectedImages.clear();
	  }

	  public void clearAllSelectedState() {
		  Iterator<ImageBean> iterator = selectedImages.iterator();
		  while (iterator.hasNext()) {
			  iterator.next().setSelectd(false);
		  }
	  }

	  public List<ImageBean> getChoosedImages() {
		  return this.selectedImages;
	  }

	  public ImageBean getImage(int paramInt) {
		  return (ImageBean)this.selectedImages.get(paramInt);
	  }

	  public int getImagesCount() {
		  return this.selectedImages.size();
	  }

	  public void insertImage(int paramInt, ImageBean paramImage) {
		  this.selectedImages.add(paramInt, paramImage);
	  }

	  public void remove(ImageBean paramImage) {
		  paramImage.setSelectd(false);
		  this.selectedImages.remove(paramImage);
	  }
	  
	  public void changeItemState(ImageBean paramImage) {
		  if (paramImage.getSelectd()) {
			  remove(paramImage);
		  } else {
			  addImage(paramImage);
		  }
	  }
	
}
