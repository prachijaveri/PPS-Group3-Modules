package main;

import java.util.LinkedList;


public class Module 
{
	private String module_name;
	private String module_description;
	private LinkedList<Module> prerequsites = new LinkedList<Module>();
}