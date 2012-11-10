package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class Main 
{
	private static LinkedList<CollegeCatalog> college_course = new LinkedList<CollegeCatalog>();

	public static void main(String args[])throws Exception
	{
		//READING ALL INPUT URLS FROM A TEXT FILE 
		BufferedReader br = new BufferedReader(new FileReader(new File("./input.txt")));
		String s="";
		while((s=br.readLine()) != null)
		{
			CollegeCatalog cc = new CollegeCatalog(s);
			ProcessData.getContent(cc);
			college_course.add(cc);
		}
		System.out.print(college_course.size());	
		br.close();
		System.out.println("\n---------------------------------------------------");
		System.out.println(); 	
	}
}