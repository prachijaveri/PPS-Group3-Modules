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
	
	void addPrerequisites(Module m)
	{
		prerequsites.add(m);
	}
	
	String getModuleName()
	{
		return module_name;
	}
	
	public String toString()
	{
		String s = module_name+"\n"+"Description : "+module_description+"\n"+"Prerequisites : ";
		for(int i=0;i<prerequsites.size();i++)
		{
			Module m = prerequsites.get(i);
			s += m.getModuleName()+",";
		}
		return s;
			
	}
}