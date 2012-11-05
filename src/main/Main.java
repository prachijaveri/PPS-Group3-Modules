package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Main 
{
	private static LinkedList<String> course_urls = new LinkedList<String>();
	@SuppressWarnings("rawtypes")
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
		HashMap<String,Integer> unsorted_map = new HashMap<String,Integer>();
        ValueComparator bvc =  new ValueComparator(unsorted_map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
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
            String[] words = text.split("\\s+|\\W+");
            for (String w: words)
            {
            	String ww = w.toLowerCase();
                if (ww.length() < 3) 
                	continue;
                if(unsorted_map.get(ww) == null)
                {
                	unsorted_map.put(ww, 1);
                	}
                else
                {
                    unsorted_map.put(ww, (unsorted_map.get(ww))+1);
                }
            }
            System.out.println();
			System.out.println("---------------------------------------------------");
			System.out.println();
		}
		sorted_map.putAll(unsorted_map);
        Iterator it = sorted_map.entrySet().iterator();
        while (it.hasNext()) 
        {	
        	Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            it.remove();
        }
//		    System.out.println("\n\n\n\n");
//		    String[] splitString = text.split("(textbook|Textbooks)");
//		    for(int j = 0; j < splitString.length ; j ++)
//		    {
//		        System.out.println(splitString[j]+"\n"+"\n");
//		    }
	}
}

class ValueComparator implements Comparator<String> 
{
    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) 
    {
        this.base = base;
    }
    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) 
    {
        if (base.get(a) >= base.get(b)) 
        {
            return -1;
        } 
        else 
        {
            return 1;
        } // returning 0 would merge keys
    }
}
