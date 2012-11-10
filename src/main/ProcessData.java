package main;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ProcessData 
{
	private static int FILTER_LENGTH = 200;
	
	static void getContent(CollegeCatalog c) throws IOException
	{
		Document doc = null;
		System.err.println(c.getUrl());
        try
		{
        	Thread.sleep(1000);
			doc = Jsoup.connect(c.getUrl()).get();
		}
		catch (Exception e)
		{
			System.out.println("Error with JSOUP "+ e);
		}
		if(doc != null)
		{
			//To get all tags names from the url
			//try and get the data from these tags
			//gets all heading
			//gets all pargraphs
			//parses through al the children pages linked to that webpage
			String headings = "h1 h2 h3 h4 h5 h6";
			String paragraph="p";
			Elements all_tags_elements = new Elements();
			all_tags_elements.add(doc);
			while(all_tags_elements.size() != 0) 
			{
				Elements all_links = new Elements();
				Iterator<Element> i = all_tags_elements.iterator();
				while(i.hasNext()) {
					Element element = i.next();
					String tag_name=element.tagName();
//					System.out.println(tag_name);
					if(headings.contains(tag_name))
					{
						String heading = element.text();
//						System.out.println(heading);
//						System.out.println(checkWikipedia(heading));
					}
					if(paragraph.contains(tag_name))
							System.out.println(element.text());
					all_links.addAll(element.children());
					i.remove();
				}
				all_tags_elements = all_links;
			}
		
//			String text = doc.body().text();
//			text=text.trim().toLowerCase();
//			text=text.substring(text.indexOf("course "));
//			c.setContent(text);
//
//			//System.out.println(text);
//			Elements links = doc.select("a");
//			
//			/*Get all headings*/
//			Elements headings = doc.select("h1, h2, h3, h4, h5, h6");
//			System.out.println(" ---   Headings  ---");
//			//System.out.println(headings.text());
//			Iterator <Element> headingIterator = headings.iterator();
//			while(headingIterator.hasNext()){
//				String heading = headingIterator.next().text();
//				
//				System.out.println(heading);
//			}
//			
//			
//			
//			/* Use tag length as a possible discriminatory feature. */
//			Elements allTags = doc.body().select("*");
//
//			for (Element tag : allTags) {
//				//System.out.println(tag.ownText());
//			    if(tag.ownText().length()> FILTER_LENGTH)
//			    {
//			    	//System.out.println(tag.ownText());
//			    }
//			}
//			
//			
//			
//			for(int j=0;j<links.size();j++)
//			{
//				//System.out.println(links.get(j).text());
//				//System.out.println(links.get(j).attr("abs:href"));
//			}
		}
	}

	static void getWordFrequency(CollegeCatalog c)
	{
		HashMap<String,Integer> unsorted_map = new HashMap<String,Integer>();
        ValueComparator bvc =  new ValueComparator(unsorted_map);
        TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		String text = c.getContent();
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
		sorted_map.putAll(unsorted_map);
		Iterator it = sorted_map.entrySet().iterator();
		while (it.hasNext()) 	
		{	
			Map.Entry pairs = (Map.Entry)it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			it.remove();
		}
	}
	
	public static boolean checkWikipedia(String query) throws IOException{
			query = URLEncoder.encode(query);
			Document wiki = Jsoup.connect("http://en.wikipedia.org/w/index.php?title=Special%3ASearch&profile=default&search="+query).get();
			String text = wiki.body().text();
			text.toLowerCase();
			return text.contains("economics");//Variable based on subject [TODO]	
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