package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class Main 
{
	private static LinkedList<CollegeCatalog> college_course = new LinkedList<CollegeCatalog>();
	protected static LinkedList<Module> modules = new LinkedList<Module>();
	
	public static void main(String args[])throws Exception
	{
		//READING ALL INPUT URLS FROM A TEXT FILE 
		BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")));
		String s="";
		
		ProcessData.improveDatabase();
		while((s=br.readLine()) != null)
		{
			CollegeCatalog cc = new CollegeCatalog(s);
			//ProcessData.getWikiModules();
			ProcessData.getContent(cc);
			college_course.add(cc);
		}
		ProcessData.getWikiModules();
		ProcessData.printHash();
		//testDB();	
		br.close();
		System.out.println("-------------------------------------------------");

		ProcessData.printDotFile();
		System.out.println(Module.num);
	}
	
	static void printAllModules()
	{
		System.out.println(ProcessData.mSet.size());
		for(int i=0;i<ProcessData.mSet.size();i++)
		{
			Module m= ProcessData.mSet.get(i);
			System.out.println(m);
			System.out.println();
		}
	}
	
	static void addModule(Module m)
	{
		modules.add(m);
	}
	

	
	
}