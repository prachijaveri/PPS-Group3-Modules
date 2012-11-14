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
	private static int FILTER_LENGTH = 2000;
	private static HashMap<String,Integer> wordMap = new HashMap<String,Integer>();
	private static String topic = "computer science";
	private static String topic_url = "computer science";
	private static int minWordCount = 12;
	private static ArrayList<String> moduleSet = new ArrayList<String>();
	public static String [] ignoreLinks = {
		"list",
		"lists",
	};
	public static String bannedWords[]={
			"a","able","about","above","accordance","according","accordingly","across","act","actually","additional","added","adj","affected","affecting","affects","after","afterwards","again","against","ah","all","almost","alone","along","already","also","although","always","am","among","amongst","an","and","announce","another","any","anybody","anyhow","anymore","anyone","anything","anyway","anyways","anywhere","ap","apparently","approximately","are","aren","aren’t","arise","around","as","aside","ask","asking","at","auth","available","away","awfully",
			"b","back","be","became","because","become","becomes","becoming","been","before","beforehand","begin","beginning","beginnings","begins","behind","being","believe","below","beside","besides","between","beyond","boil","born","both","brief","briefly","but","by",
			"c","ca","came","can","cannot","can't","cause","causes","certain","certainly","co","com","come","comes","contain","containing","contains","could","couldn’t","course","courses",
			"d","date","did","didn't","different","do","does","doesn't","doing","done","don't","down","downwards","due","during",
			"e","each","econ","ed","edu","effect","eg","eight","eighty","either","else","elsewhere","end","ending","enough","especially","et","etc","even","ever","every","everybody","everyone","everything","everywhere","ex","except",
			"f","far","few","ff","fifth","first","five","fix","followed","following","follows","for","former","formerly","forth","found","four","from","further","furthermore",
			"g","gave","get","gets","getting","give","given","gives","giving","go","goes","gone","got","gotten",
			"h","had","happens","hardly","has","hasn't","have","haven't","having","he","hed","hence","her","here","hereafter","hereby","herein","heres","hereupon","hers","herself","hes","hi","hid","him","himself","his","hither","home","how","howbeit","however","hundred",
			"i","id","ie","if","ii","i'll","im","immediate","immediately","importance","important","in","inc","indeed","index","information","instead","into","invention","inward","is","isn't","it","itd","it'll","its","itself","i've",
			"j","just",
			"k","keep","keeps","kept","kg","km","know","known","knows",
			"l","largely","last","lately","later","latter","latterly","least","less","lest","let","lets","like","liked","likely","line","little","'ll","look","looking","looks","ltd",
			"m","made","mainly","make","makes","many","may","maybe","me","mean","means","meantime","meanwhile","merely","mg","might","million","miss","mit","ml","more","moreover","most","mostly","mr","mrs","much","mug","must","mw","my","myself",
			"n","na","name","namely","nay","nd","near","nearly","necessarily","necessary","need","needs","neither","never","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","no","one","nor","normally","nos","not","noted","nothing","now","nowhere",
			"o","obtain","obtained","obviously","of","off","often","oh","ok","okay","old","omitted","on","once","one","ones","only","onto","or","ord","other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","owing","own",
			"p","page","pages","part","particular","particularly","past","per","perhaps","placed","please","plus","poorly","possible","possibly","potentially","pp","predominantly","present","presents","previously","primarily","probably","promptly","proud","provides","put",
			"q","que","quickly","quite","qv",
			"r","ran","rather","rd","re","readily","really","recent","recently","ref","refs","regarding","regardless","regards","related","relatively","research","respectively","resulted","resulting","results","right","run",
			"s","said","same","saw","say","saying","says","sec","section","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sent","seven","several","shall","she","shed","she'll","shes","should","shouldn't","show","showed","shown","showns","shows","significant","significantly","similar","similarly","since","six","slightly","so","some","somebody","somehow","someone","somethan","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specifically","specified","specify","specifying","still","stop","strongly","sub","substantially","successfully","such","sufficiently","suggest","sup","sure",
			"t","take","taken","taking","tell","tends","textbook","textbooks","th","than","thank","thanks","thanx","that","that'll","that’s","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","thered","therefore","therein","there'll","thereof","there’re","theres","thereto","thereupon","there've","these","they","theyd","they'll","theyre","they've","think","this","those","thou","though","thousand","throug","through","throughout","thru","thus","til","tip","to","together","too","took","toward","towards","tried","tries","truly","try","trying","ts","twice","two",
			"u","un","under","unfortunately","unless","unlike","unlikely","until","unto","up","upon","ups","us","use","used","useful","usefully","usefulness","uses","using","usually",
			"v","value","various","'ve","very","via","viz","vol","vols","vs",
			"w","want","wants","was","wasn't","way","we","wed","welcome","we'll","went","were","weren't","we've","what","whatever","what'll","whats","when","whence","whenever","where","whereafter","whereas","whereby","wherein","wheres","whereupon","wherever","whether","which","while","whim","whither","who","whod","whoever","whole","who'll","whom","whomever","whos","whose","why","widely","willing","wish","with","within","without","won't","words","world","would","wouldn't","www",
			"x",
			"y","yes","yet","you","youd","you'll","your","youre","yours","yourself","yourselves","you've",
			"z","zero",
			"-","!"," ","@","#","$","%","^","&","*","(",")","_","-","+","=","{","[","}","]","\"","'",":",";","<",",",">",".","?","/","|","\\","\t","\\s","\n",
			"1","2","3","4","5","6","7","8","9","10"
			};
	
	static void getContent(CollegeCatalog c) throws IOException
	{
		String content = ParseHtml.parsePage(c.getUrl());
		c.setContent(content);
		parseText(content);									
	}
	
	public static void parseText(String allText)
	{
		String[] wordArray1 = getWords(allText,1);
		String[] wordArray2 = getWords(allText,2);
		String[] wordArray3 = getWords(allText,3);
		//System.out.println(wordArray1.length);
		//System.out.println(wordArray3.length;
		//System.out.println(wordArray2.length);
		//addToHash(wordArray1);
		addToHash(wordArray1);
		addToHash(wordArray2);
		addToHash(wordArray3);	
	}
	
	public static String[] getWords(String fullString, int phraseLength)
	{
		//System.out.println(fullString);
		String[] wordArray = fullString.split("\\s+");
		String[] returnArray = new String[wordArray.length];
		for(int i = 0; i < wordArray.length -phraseLength; i++)
		{
			String phraseToAdd = "";
			//System.out.println(wordArray[wordArray.length- phraseLength -3]);
			for (int j = i; j < i+phraseLength; j++) 
			{
				//CHECK FOR PUNCTUATIONS AND END OF SENTENCES.IF POSSIBLE STOP AT END OF SENTENCES AS PHRASES CANNOT BE FROM DIFFERENT SENTENCES
				phraseToAdd =phraseToAdd.toLowerCase() + wordArray[j].toLowerCase() + " ";
			}
			phraseToAdd=phraseToAdd.trim().toLowerCase();
			returnArray[i]=phraseToAdd; 
		}
		return returnArray;
	}
	
	public static void addToHash(String[]words)
	{
		for (String word : words) 
		{
			//System.out.println(word);
			if(!isValid(word))
				continue;
			if(wordMap.containsKey(word))
			{
				Integer wordCount = wordMap.get(word);
				wordCount = wordCount + 1;
				//System.out.println("Wordcount should get more than one");
				wordMap.put(word, wordCount);
			}
			else
			{
				wordMap.put(word, 1);
			}
		}
	}
	
	public static boolean isValid(String word)
	{
		if(word == null)
			return false;
		if(startsOrEndsWith(word))
		{
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
	
	public static boolean startsOrEndsWith(String phrase)
	{
		String[] wordArray = phrase.split(" ");
		if(containsAny(wordArray[0])||containsAny(wordArray[wordArray.length-1]))
		{
			return true;
		}
		return false;
	}
	
	public static boolean containsAny(String searchString)
	{
		for (int i = 0; i < bannedWords.length; i++) 
		{
			if(searchString.contains(bannedWords[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void getWikiModules()
	{
		String query = topic_url;
		query= query.replace(" ", "_");
		//query = URLEncoder.encode(query);
		Document wiki = null;
		String urlString ="";
		ArrayList<String> wikiModules = new ArrayList<String>();
		try 
		{
			urlString = "http://en.wikipedia.org/wiki/Outline_of_"+query;
			wiki = Jsoup.connect(urlString).get();
		} 
		catch (IOException e) 
		{
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
		for(Element link : links)
		{			
			//if(link.text().length() <30)
			//{	
				wikiModules.add(link.text());				
			//}			
		}
		int constant = wikiModules.size();
		int restraint = constant /36;
		Random random = new Random();
		for (String module : wikiModules) 
		{
			int rand = random.nextInt(restraint);
			if(rand == restraint - 1 && !module.equals(topic_url))
			{
				moduleSet.add(module);
			}
		}
		//String text = wiki.body().text();
		//text.toLowerCase();
	}
	
	public static void printHash()
	{
		removeFromHash();
		System.out.println("Word count:" +wordMap.size());			
		for (String key : wordMap.keySet()) 
		{
			Integer count = wordMap.get(key);
			if(count >15 && checkWikipedia(key))
			{
				//System.out.println(key + " "+count);
				String betterKey =getWikiTitle(key); 
				if(!betterKey.equals("") && !betterKey.equals(topic_url))
				{
					moduleSet.add(betterKey);
					//System.out.println(betterKey);
				}					
				else if (!key.equals(topic_url))
				{
					moduleSet.add(key);
					//System.out.println(key);
				}
			}							
		}
	}
	
	public static void removeFromHash()
	{
	   Iterator it = wordMap.entrySet().iterator();
	   while (it.hasNext())
	   {
	      Entry item = (Entry) it.next();
	      if((Integer)item.getValue()<minWordCount)
	    	  it.remove();
	   }
	}
	
	public static boolean checkWikipedia(String query)
	{
		query = URLEncoder.encode(query);
		Document wiki;
		String urlString ="";
		try 
		{
			urlString = "http://en.wikipedia.org/w/index.php?title=Special%3ASearch&profile=default&limit=3&search="+query;
			wiki = Jsoup.connect(urlString).get();
		} 
		catch (IOException e) 
		{
			return false;
		}
		String text = wiki.body().text();
		text.toLowerCase();
		if(text.contains(topic))
		{
//			System.out.println(urlString);
			return true;
		}
		return false;	
	}
	
	public static String getWikiTitle(String query)
	{
		query = URLEncoder.encode(query);
		System.out.println(query);
		Document wiki =null;
		String urlString ="";
		String bettertitle = "";
		try 
		{
			urlString = "http://en.wikipedia.org/w/index.php?title=Special%3ASearch&profile=default&limit=3&search="+query;
			wiki = Jsoup.connect(urlString).get();
		}
		catch (IOException e) 
		{
			//return false;
		}
		Elements innerResults = wiki.select("ul[class=mw-search-results] li");
		Element previousResult = null;
		for(Element result: innerResults)
		{
			if(result.text().contains(topic))
			{
				Elements title= result.select("a[title]");
				bettertitle = title.attr("title");
			}
		}
		return bettertitle;
	}
	
	public static void printDotFile() throws IOException
	{
		PrintWriter out = new PrintWriter(new FileWriter(topic+"map.dot"));
		out.println("Graph{");
		for (String module : moduleSet) 
		{
			Main.addModule(new Module(module));
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