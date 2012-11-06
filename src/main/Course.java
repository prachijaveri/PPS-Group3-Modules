package main;

import java.util.LinkedList;

public class Course 
{
	private String name;
	private String id;
	private String dept;
	private String description;
	private LinkedList<Course> prerequisites = new LinkedList<Course>();
}
