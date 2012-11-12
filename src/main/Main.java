package main;
import com.mongodb.DB;
import com.mongodb.Mongo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Set;

public class Main 
{
	private static LinkedList<CollegeCatalog> college_course = new LinkedList<CollegeCatalog>();

	public static void main(String args[])throws Exception
	{
		//READING ALL INPUT URLS FROM A TEXT FILE 
		BufferedReader br = new BufferedReader(new FileReader(new File("input.txt")));
		String s="";
		while((s=br.readLine()) != null)
		{
			CollegeCatalog cc = new CollegeCatalog(s);
			//ProcessData.getContent(cc);
			college_course.add(cc);
		}
		//System.out.print(college_course.size());	
		testDB();
		br.close();
		
		System.out.println("\n---------------------------------------------------");
		System.out.println(); 	
	}
	
	public static void testDB() throws UnknownHostException{
		Mongo mongoDB = new Mongo("dbw.cs.columbia.edu", 27017);
		DB courseDatabase = mongoDB.getDB("modules");
		Set<String> collections = courseDatabase.getCollectionNames();
		for(String thisCollection: collections){
			System.out.println(thisCollection);
		}
		
		
	}
}