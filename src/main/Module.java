package main;


import java.util.Comparator;
import java.util.LinkedList;


public class Module 
{
	protected static int num =1;
	private static int number = 1 ; //the ordering or lateness of a module
	private String module_name;
	private String module_description;
	private int count = 0; //how many times a module appears
	private int relevance; //estimates relevance to a given topic
	private LinkedList<Module> prerequsites = new LinkedList<Module>();
	
	Module(String s)
	{
		module_name = "Module-"+num;
		num++;
		module_description=s;
		relevance = number;
		number++;
	}
	
	void addPrerequisites(Module m)
	{
		prerequsites.add(m);
	}
	
	String getModuleName()
	{
		return module_name;
	}
	
	String getModuleDescription()
	{
		return module_description;
	}
	
	int getCount()
	{
		return count;
	}
	
	void plusCount()
	{
		count++;
	}
	
	void changeDescription(String newdesc)
	{
		module_description = newdesc;
	}
	
	int getRelevance()
	{
		return relevance;
	}
	
	int getNumber()
	{
		return relevance;
	}
	
	void resetNumber()
	{
		number = 1 ; // reset a number on a given page
	}
	
	public boolean equals(Object o){
		Module otherModule = (Module) o;
		if(this.module_description.equals(otherModule.getModuleDescription())){
			return true;
		}
		return false;
	}
	public String toString()
	{
		String s = module_name+"\n"+"Description : "+module_description+"\n";//+"Prerequisites : ";
//		for(int i=0;i<prerequsites.size();i++)
//		{
//			Module m = prerequsites.get(i);
//			s += m.getModuleName()+",";
//		}
		return s;
			
	}
	
	public static Comparator<Module> countComparator = new Comparator<Module>() 
	{
		public int compare(Module m1, Module m2)
		{
			Integer m1c = m1.getCount();
			Integer m2c = m2.getCount();
			return m1c.compareTo(m2c);
		}
	};
	
	public static Comparator<Module> relevanceComparator = new Comparator<Module>() 
			{
		public int compare(Module m1, Module m2)
		{
			Integer m1c = m1.getRelevance();
			Integer m2c = m2.getRelevance();
			return m1c.compareTo(m2c);
		}
	};
	
//	public static Comparator<Module> numComparator = new Comparator<Module>() {
//		public int compare(Module m1, Module m2){
//			Integer m1c = m1.getNumber();
//			Integer m2c = m2.getNumber();
//			System.out.println(m1c+"  "+m2c);
//			return m1c.compareTo(m2c);
//		}
//	};
}