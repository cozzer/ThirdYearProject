package com.example.geopic;

public class ImageEntry {
	
	private String title, path, author;
	
	public ImageEntry(String title, String path, String author){
		
		this.title = title;
		this.path = path;
		this.author = author;
	}
	
	public String getTitle(){
		return this.title;
	}
	public String getPath(){
		return this.path;
	}
	public String getAuthor(){
		return this.author;
	}
	
	public String toString(){
		return "Image Title is...." + title;
	}

}
