package main;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ProcessData 
{
	static void getContent(CollegeCatalog c)
	{
		Document doc = null;
        try
		{
			doc = Jsoup.connect(c.getUrl()).get();
		}
		catch (Exception e)
		{
			System.out.println("Error with JSOUP "+ e);
		}
		if(doc != null)
		{
			String text = doc.body().text();
			text=text.trim().toLowerCase();
			text=text.substring(text.indexOf("course "));
			c.setContent(text);

			System.out.println(text);
			Elements links = doc.select("a");
			for(int j=0;j<links.size();j++)
			{
				//System.out.println(links.get(j).text());
				//System.out.println(links.get(j).attr("abs:href"));
			}
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