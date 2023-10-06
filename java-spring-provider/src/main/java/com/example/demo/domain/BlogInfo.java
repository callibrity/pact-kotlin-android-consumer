package com.example.demo.domain;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BlogInfo {

	private String title;
	private String description;
	
	public BlogInfo(String title, String description) {
		this.title = title;
		this.description = description;
	}
}
