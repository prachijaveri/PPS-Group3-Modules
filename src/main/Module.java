package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
		
		//GETTING CONTENT OF EACH OF THE URLS 
		for(int i=0;i<course_urls.size();i++)
		{
			Document doc = Jsoup.connect(course_urls.get(i)).get();
		    String text = doc.body().text(); 
		    System.out.println("\n\n\n\n");
		    String[] splitString = text.split("(textbook|Textbooks)");
		    for(int j = 0; j < splitString.length ; j ++)
		    {
		        System.out.println(splitString[j]+"\n"+"\n");
		    }

//			Document doc = Jsoup.connect(course_urls.get(i)).get();
//			Elements body=doc.select(body);
//			System.out.println(course_urls.get(i).toUpperCase());
//			URL url = new URL(course_urls.get(i));
//			InputStream is = url.openConnection().getInputStream();
//			BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
//	        String line = "";
//			String html="";
//	        while( ( line = reader.readLine() ) != null )  
//	        {
//	        	html+=(line+"\n");
//	        }
//			Document doc = Jsoup.parseBodyFragment(html);
//			Element body = doc.body();
//			System.out.println(body.text());
//		    reader.close();
		}
	}
}
