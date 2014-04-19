package com.brennan_hzl.photovine.bean;

import java.util.List;

/**
 * GridView的每个item的数据对象
 * 
 * @author len
 *
 */
public class AlbumBean{
	/**
	 * 文件夹的第一张图片路径
	 */
	private List<ImageBean> images;
	/**
	 * 文件夹名
	 */
	private String folderName; 
	/**
	 * 文件夹中的图片数
	 */
	private int imageCounts;
	
	public List<ImageBean> getImages() {
		return images;
	}
	public void setImages(List<ImageBean> images) {
		this.images = images;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public int getImageCounts() {
		return imageCounts;
	}
	public void setImageCounts(int imageCounts) {
		this.imageCounts = imageCounts;
	}
	
}
