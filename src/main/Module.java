package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Module 
{
	private static LinkedList<String> course_urls = new LinkedList<String>();
	public static void main(String args[])throws Exception
	{
		//READING ALL INPUT URLS FROM A TEXT FILE 
		BufferedReader br = new BufferedReader(new FileReader(new File("./input.txt")));
		String s="";
		while((s=br.readLine()) != null)
			course_urls.add(s);
		br.close();
		System.out.println("---------------------------------------------------");
		System.out.println();
		//GETTING CONTENT OF EACH OF THE URLS 
		for(int i=0;i<course_urls.size();i++)
		{
			System.out.println(course_urls.get(i).toUpperCase());
			Document doc = Jsoup.connect(course_urls.get(i)).get();
		    String text = doc.body().text();
		    Elements links = doc.select("a");
		    for(int j=0;j<links.size();j++)
		    {
		    	//System.out.println(links.get(j).text());
		    	System.out.println(links.get(j).attr("abs:href"));
		    }
		    System.out.println();
			System.out.println("---------------------------------------------------");
			System.out.println();
		    System.out.println("\n\n\n\n");
		    String[] splitString = text.split("(textbook|Textbooks)");
		    for(int j = 0; j < splitString.length ; j ++)
		    {
		        System.out.println(splitString[j]+"\n"+"\n");
		    }
		}
	}
}
