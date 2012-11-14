package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class Main 
{
	private static LinkedList<CollegeCatalog> college_course = new LinkedList<CollegeCatalog>();
	protected static LinkedList<Module> modules = new LinkedList<Module>();
	
	public static void main(String args[])throws Exception
	{
		//READING ALL INPUT URLS FROM A TEXT FILE 
		BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")));
		String s="";
		while((s=br.readLine()) != null)
		{
			CollegeCatalog cc = new CollegeCatalog(s);
			//ProcessData.getWikiModules();
			ProcessData.getContent(cc);
			college_course.add(cc);
		}
		ProcessData.getWikiModules();
		ProcessData.printHash();
		//System.out.print(college_course.size());	
		//testDB();	
		br.close();
		
		//ProcessData.getWikiTitle("problems of free");
		
		
		ProcessData.printDotFile();
		System.out.println("-------------------FINAL OUTPUT-----------------");
		printAllModules();
		System.out.println("\n---------------------------------------------------");
		System.out.println(); 	
	}
	
	static void printAllModules()
	{
		
		for(int i=0;i<modules.size();i++)
		{
			Module m= modules.get(i);
			System.out.println(m);
			System.out.println();
			
		}
	}
	
	static void addModule(Module m)
	{
		modules.add(m);
	}
}