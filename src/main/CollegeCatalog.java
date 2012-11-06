package main;

import java.util.LinkedList;

public class CollegeCatalog 
{
	private String course_url;
	private String content;
	private LinkedList<String> related_links = new LinkedList<String>();
	private LinkedList<Course> courses = new LinkedList<Course>(); 

	CollegeCatalog()
	{
		course_url = "";
	}

	CollegeCatalog(String url)
	{
		course_url = url;                                                                                                                                                                                                                                                                                                                                                                                                                           
	}

	void setContent(String c)
	{
		content = c;
	}

	String getContent()
	{
		return content;
	}

	String getUrl()
	{
		return course_url;
	}
}