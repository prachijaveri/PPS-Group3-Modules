package main;


import java.util.LinkedList;


public class Module 
{
	private static int number = 1 ;
	private String module_name;
	private String module_description;
	private LinkedList<Module> prerequsites = new LinkedList<Module>();
	
	Module(String s)
	{
		module_name = "Module-"+number;
		number++;
		module_description=s;
	}
	
	
}