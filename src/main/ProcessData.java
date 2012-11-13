package main;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.util.StringParseUtil;


public class ProcessData 
{
	private static int FILTER_LENGTH = 200;
	private static HashMap<String,Integer> wordMap = new HashMap<String,Integer>();
	private static String topic = "math";
	private static String topic_url = "mathematics";
	private static int minWordCount = 12;
	
	private static ArrayList<String> moduleSet = new ArrayList<String>();
	
	private static String[] bannedWords = {
			"and",
			"from",
			"in",
			"of",
			"to",
			"textbook",
			"economic",
			"economics",
			"additional",
			"mit",
			"other",
			"ii",
			"mw",
			"between",
			"such",
			"the",
			"ap",
			"these",
			"course",
			"i",
			"should",
			"not",
			"how",
			"v",
			"presents",
			"or",
			"econ",
			"at",
			"useful",
			"as",
			"a",
			"on",
			"be",
			"s",
			"-"
	};
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
			Elements a = doc.select("*");
			
			//System.out.println(startsOrEndsWith("and gains from"));
			
			//System.out.println(a.text());
			parseText(a.text());									
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
	
	public static boolean checkWikipedia(String query){
		
			query = URLEncoder.encode(query);
			//System.out.println(query);
			Document wiki;
			String urlString ="";
			try {
				urlString = "http://en.wikipedia.org/w/index.php?title=Special%3ASearch&profile=default&limit=3&search="+query;
				
				wiki = Jsoup.connect(urlString).get();
				
			} catch (IOException e) {
				return false;
			}
			String text = wiki.body().text();
			text.toLowerCase();
			
			if(text.contains(topic)){
				System.out.println(urlString);
				return true;
			}
			return false;	
	}
	
	public static String [] ignoreLinks = {
		"list",
		"lists",
		
	};
	
	public static void getWikiModules(){
		String query = topic_url;
		//query = URLEncoder.encode(query);
		Document wiki = null;
		String urlString ="";
		ArrayList<String> wikiModules = new ArrayList<String>();
		try {
			urlString = "http://en.wikipedia.org/wiki/Outline_of_"+query;
			
			wiki = Jsoup.connect(urlString).get();
			
		} catch (IOException e) {
			//return false;
		}
		Elements links = wiki.select("li:not([id])");		
		links = links.select("li:not([class])");		
		links = links.select("a");
		links = links.select("a:not([class])");
		links = links.select("a:not([href*=Category])");
		links = links.select("a:not([href*=Portal])");
		//links = links.select("div:not([class])");
		//links = links.select("div:not([style])");		
		for(Element link : links){			
			if(link.text().length() <30){				
				wikiModules.add(link.text());				
			}			
		}
		int constant = wikiModules.size();
		int restraint = constant /36;
		Random random = new Random();
		for (String module : wikiModules) {
			int rand = random.nextInt(restraint);
			if(rand == restraint - 1){
				//System.out.println(module);
				moduleSet.add(module);
			}
		}
		
		
		
		//String text = wiki.body().text();
		//text.toLowerCase();
	}
	
	public static String getWikiTitle(String query){
		query = URLEncoder.encode(query);
		//System.out.println(query);
		Document wiki =null;
		String urlString ="";
		String bettertitle = "";
		try {
			urlString = "http://en.wikipedia.org/w/index.php?title=Special%3ASearch&profile=default&limit=3&search="+query;
			
			wiki = Jsoup.connect(urlString).get();
			
		} catch (IOException e) {
			//return false;
		}
		Elements innerResults = wiki.select("ul[class=mw-search-results] li");
	//	Elements innerResults = searchResults.select(".searchResult");
		Element previousResult = null;
		for(Element result: innerResults){
			//System.out.println(result.text());
			//System.out.println("\n\n");
			if(result.text().contains(topic)){
				//System.out.println("did we get it?");
				//if(previousResult!=null){
				//	System.out.println(previousResult.text());	
				//}			
				Elements title= result.select("a[title]");
				
				//System.out.println(title.attr("title"));
				bettertitle = title.attr("title");
				//System.out.println(result.html());
				//Element thisElements = wiki.before(result.html());
				//System.out.println(thisElements.text());*/
			}
			//previousResult = result;
		}
		//String text = searchResults.text();		
		//text.toLowerCase();
		//System.out.println(text);
		
	//	if(text.contains(topic)){
		//	System.out.println(urlString);
			//return true;
	//	}
		//return false;	
		return bettertitle;
	}
	
	public static boolean startsOrEndsWith(String phrase){
		String[] wordArray = phrase.split(" ");
		if(containsAny(wordArray[0])||containsAny(wordArray[wordArray.length-1])){
			return true;
		}
		return false;
	}
	
	public static boolean containsAny(String searchString){
		for (int i = 0; i < bannedWords.length; i++) {
			if(searchString.contains(bannedWords[i])){
				return true;
			}
		}
		return false;
	}
	
	public static String[] getWords(String fullString, int phraseLength){
		//System.out.println(fullString);
		String[] wordArray = fullString.split("\\s+");
		String[] returnArray = new String[wordArray.length];
		for(int i = 0; i < wordArray.length -phraseLength; i++){
			String phraseToAdd = "";
			//System.out.println(wordArray[wordArray.length- phraseLength -3]);
			for (int j = i; j < i+phraseLength; j++) {
				phraseToAdd =phraseToAdd.toLowerCase() + wordArray[j].toLowerCase() + " ";
			}			
			returnArray[i]=phraseToAdd; 
		}
		return returnArray;
	}
	
	
	public static void parseText(String allText){
		//String[] wordArray1 = getWords(allText,1);
		String[] wordArray2 = getWords(allText,2);
		String[] wordArray3 = getWords(allText,3);
		
		//System.out.println(wordArray1.length);
		System.out.println(wordArray3.length);
		//System.out.println(wordArray2.length);
		
		//addToHash(wordArray1);
		addToHash(wordArray2);
		addToHash(wordArray3);
		
	}
	
	public static void addToHash(String[]words){
		for (String word : words) {
			//System.out.println(word);
			if(!isValid(word))
				continue;
			
			if(wordMap.containsKey(word)){
				Integer wordCount = wordMap.get(word);
				wordCount = wordCount + 1;
				//System.out.println("Wordcount should get more than one");
				wordMap.put(word, wordCount);
			}
			else{
				wordMap.put(word, 1);
			}
		}
	}
	
	public static void removeFromHash(){
	   Iterator it = wordMap.entrySet().iterator();
	   while (it.hasNext())
	   {
	      Entry item = (Entry) it.next();
	      if((Integer)item.getValue()<minWordCount)
	    	  it.remove();
	   }
		/*for(String word:wordMap.keySet()){
			if(wordMap.get(word)<20){
				wordMap.remove(word);
			}
		}*/
	}
	
	
	public static boolean isValid(String word){
		boolean isValid = true;
		if(word == null)
			return false;
		if(startsOrEndsWith(word)){
			return false;
		}
		if(!word.matches(".*[0-9].*") && !word.matches("[^\\p{L}\\p{N}]") &&
			!word.contains(".") && !word.contains(",") && !word.contains("(")
			&& !word.contains(")") && !word.contains(":")  && !word.contains(";")
			&& !word.contains("]") && !word.contains("\"") && !word.contains("|") ) 
		{
			return true;
		}

		return false;
		
	}
	
	public static void printHash(){
		removeFromHash();
		System.out.println("Word count:" +wordMap.size());			
		for (String key : wordMap.keySet()) {
			Integer count = wordMap.get(key);
			if(count >15 && checkWikipedia(key)){
				//System.out.println(key + " "+count);
				String betterKey =getWikiTitle(key); 
				if(!betterKey.equals("")){
					moduleSet.add(betterKey);
					System.out.println(betterKey);
				}					
				else {
					moduleSet.add(key);
					System.out.println(key);
				}
				
			}							
		}
	}
	
	public static void printDotFile() throws IOException{
		PrintWriter out = new PrintWriter(new FileWriter(topic+"map.dot"));
		out.println("Graph{");
		for (String module : moduleSet) {
			Random random = new Random();
			int index1 = random.nextInt(moduleSet.size()-1);
			int index2 =random.nextInt(moduleSet.size()-1);
			int index3 = random.nextInt(moduleSet.size()-1);
			String mod1 = moduleSet.get(index1);
			String mod2 = moduleSet.get(index2);
			String mod3 = moduleSet.get(index3);
			mod1=mod1.replace(' ', '_');
			mod2=mod2.replace(' ', '_');
			mod3=mod3.replace(' ', '_');
			out.println(mod1+"--"+mod2);
			out.println(mod2+"--"+mod3);
			out.println(mod3+"--"+mod1);			
		}
		out.println("}");
		out.close();
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