package com.brennan_hzl.photovine.bean;

import java.util.List;

/**
 * GridView��ÿ��item�����ݶ���
 * 
 * @author len
 *
 */
public class SlideShowEditorBean{
	/**
	 * �ļ��еĵ�һ��ͼƬ·��
	 */
	private List<ImageBean> images;
	/**
	 * �ļ�����
	 */
	private String folderName; 
	/**
	 * �ļ����е�ͼƬ��
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
