package main;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.bson.NewBSONDecoder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;


public class ProcessData 
{
	private static int FILTER_LENGTH = 2000;
	private static HashMap<String,Integer> wordMap = new HashMap<String,Integer>();
	protected static String topic = "math";
	protected static String topic_url = "mathematics";
	private static int minWordCount = 1;
	private static ArrayList<String> moduleSet = new ArrayList<String>();
	
	protected static ArrayList<Module> mSet= new ArrayList<Module>();
	
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
			"q","que","quickly","quite","qv","major","majors",
			"r","ran","rather","rd","re","readily","really","recent","recently","ref","refs","regarding","regardless","regards","related","relatively","research","respectively","resulted","resulting","results","right","run",
			"s","said","same","saw","say","saying","says","sec","section","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sent","seven","several","shall","she","shed","she'll","shes","should","shouldn't","show","showed","shown","showns","shows","significant","significantly","similar","similarly","since","six","slightly","so","some","somebody","somehow","someone","somethan","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specifically","specified","specify","specifying","still","stop","strongly","sub","substantially","successfully","such","sufficiently","suggest","sup","sure",
			"t","take","taken","taking","tell","tends","textbook","textbooks","th","than","thank","thanks","thanx","that","that'll","that’s","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","thered","therefore","therein","there'll","thereof","there’re","theres","thereto","thereupon","there've","these","they","theyd","they'll","theyre","they've","think","this","those","thou","though","thousand","throug","through","throughout","thru","thus","til","tip","to","together","too","took","toward","towards","tried","tries","truly","try","trying","ts","twice","two",
			"u","un","under","unfortunately","unless","unlike","unlikely","until","unto","up","upon","ups","us","use","used","useful","usefully","usefulness","uses","using","usually","undergraduate","degree","list","program","requirements","tutoring", "schedule", "club",
			"v","value","various","'ve","very","via","viz","vol","vols","vs","term","school","calculator",
			"w","want","wants","was","wasn't","way","we","wed","welcome","we'll","went","were","weren't","we've","what","whatever","what'll","whats","when","whence","whenever","where","whereafter","whereas","whereby","wherein","wheres","whereupon","wherever","whether","which","while","whim","whither","who","whod","whoever","whole","who'll","whom","whomever","whos","whose","why","widely","willing","wish","with","within","without","won't","words","world","would","wouldn't","www",
			"x",
			"y","yes","yet","you","youd","you'll","your","youre","yours","yourself","yourselves","you've",
			"z","zero",
			"-","!"," ","@","#","$","%","^","&","*","(",")","_","-","+","=","{","[","}","]","\"","'",":",";","<",",",">",".","?","/","|","\\","\t","\\s","\n",
			"1","2","3","4","5","6","7","8","9","10"
			};
	
	static void improveDatabase() throws UnknownHostException
	{
		Mongo mongoDB = new Mongo("dbw.cs.columbia.edu", 27017);
		DB courseDatabase = mongoDB.getDB("modules");
		if(!courseDatabase.collectionExists("banned_words"))
		{
			BasicDBObject newObject = new BasicDBObject();
			newObject.put("words", bannedWords);
			courseDatabase.createCollection("banned_words", newObject);
		}				
	}
	
	static void getContent(CollegeCatalog c) throws IOException, InterruptedException
	{
		System.out.println("Parsing: " + c.getUrl());
		String content = ParseHtml.parsePage(c.getUrl());		
		c.setContent(content);
		System.out.println(content);		
		parseText(content);
		
	}
	
	public static void parseText(String allText)
	{
		Module topicMod = new Module(topic);
		//String[] wordArray1 = getWords(allText,1);
		String[] wordArray2 = getWords(allText,2);
		String[] wordArray3 = getWords(allText,3);
//		for(int i =0;i<wordArray1.length;i++)
//			System.out.print(wordArray1[i]+"  ");
//		System.out.println();
//		for(int i =0;i<wordArray2.length;i++)
//			System.out.print(wordArray2[i]+"  ");
//		System.out.println();
//		for(int i =0;i<wordArray3.length;i++)
//			System.out.print(wordArray3[i]+"  ");
//		System.out.println();
//		System.out.println(wordArray1.length);
//		System.out.println(wordArray3.length);
//		System.out.println(wordArray2.length);
		//addToHash(wordArray1);
		//topicMod.resetNumber();
		//addToHash(wordArray1);
		topicMod.resetNumber();
		addToHash(wordArray2);
		topicMod.resetNumber();
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
				String h = wordArray[j].toLowerCase() ;
				//if(!containsAny(h)){					
					phraseToAdd =phraseToAdd.toLowerCase()+" "+wordArray[j].toLowerCase() +" " ;
					//System.out.println("phrase to add is:");
					//System.out.println(phraseToAdd);
				//}
				//else{
					//System.out.println("h is:");
					//System.out.println(h);
				//}
			}
			phraseToAdd=phraseToAdd.trim().toLowerCase();
			if(!phraseToAdd.equals(" ") && !phraseToAdd.equals("") && phraseToAdd != null)
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
			{
				//System.out.print("123456789");
				continue;
			}
			if(wordMap.containsKey(word))
			{
				Integer wordCount = wordMap.get(word);
				wordCount += wordCount + 1;
				
				for(Module m : mSet)
				{		
					if(m.getModuleDescription().equals(word))
					{
						m.plusCount();
					}
				}
				//System.out.println("Word count should get more than one");
				wordMap.put(word, wordCount);
			}
			else
			{
				mSet.add(new Module(word));
				wordMap.put(word, 1);
			}
		}
		//Main.printAllModules();
		//System.exit(0);
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
			if(searchString.equals(bannedWords[i]))
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
		if(wiki == null)
			return;
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
			//int rand = random.nextInt(restraint);
			if(/*rand == restraint - 1 && */!module.contains(topic))
			{
				moduleSet.add(module);
			}
		}
		//String text = wiki.body().text();
		//text.toLowerCase();
	}
	
	public static void printHash()
	{
		//removeFromHash();
		System.out.println("Word count:" +wordMap.size());			
		for (String key : wordMap.keySet()) 
		{
			Integer count = wordMap.get(key);
			Random rand = new Random();
			int randNum = rand.nextInt(10) +1;
			//randnum cuts out an order of magnitude in parsing a page
			if(randNum ==3 && count >30 && count <12000 && checkWikipedia(key))
			{				
				String betterKey =getWikiTitle(key);
				System.out.println(key + " "+count);
				if(!betterKey.equals("") && !betterKey.contains(topic))
				{
					moduleSet.add(betterKey);
					for(Module m : mSet)
					{
						if(m.getModuleDescription().equals(key))
						{
							System.out.println(m.getModuleDescription());
							m.changeDescription(betterKey);
							System.out.println(betterKey);
						}
					}
					//System.out.println(betterKey);
				}					
				else if (!key.contains(topic))
				{
//					System.out.println(key);
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
		//System.out.println(query);
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
			return "";
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
		//moduleSet
		
		
		Collections.sort(mSet, Module.countComparator);
		ArrayList<Module>mset2 = new ArrayList<Module>();
		int topic_size = 30;
		int numModules = 10;
		//take 30 most referenced topics
		//and add them to new array
		for (int i = 0; i < mSet.size(); i++) 
		{
			if(i<topic_size)
			{
				mset2.add(mSet.get(i));	
			}						
		}
		
		//Sort new set by order they came in, because that is a 
		//"weak" dependency relationship
		Collections.sort(mset2, Module.relevanceComparator);
		int moduleNumber=1;
		System.out.println("Module " + moduleNumber);
		for(int i =0; i <mset2.size(); i++)
		{			
			if(i%(topic_size/numModules) ==0)
			{
				System.out.println("Module " + moduleNumber);
				moduleNumber++;
			}
			System.out.println(mset2.get(i).getModuleDescription());			
		}
		
		createGraph(relationshipCreator(10),10);
		
		/*for (String module : moduleSet) 
			
		{
			//Main.addModule(new Module(module));
			/*Random random = new Random();
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
		}*/
		out.println("}");
		out.close();
	}
	
	public static ArrayList<Integer> relationshipCreator(int numModules)
	{
		Random random = new Random();
		ArrayList<Integer> mPerLevel = new ArrayList<Integer>();
		int sum=0;
		while(sum<numModules)
		{
			int randNum = random.nextInt(numModules)+1;
			sum+=randNum;
			mPerLevel.add(randNum);
		}
		if(sum>numModules)
		{
			Integer original = mPerLevel.get(mPerLevel.size()-1);
			Integer amountOver = sum-numModules;
			mPerLevel.set((mPerLevel.size()-1),original- amountOver);
		}
		return mPerLevel;
	}
	
	public static void createGraph(ArrayList<Integer>numPerLevel, int numModules)
	{
		int tc =1;
		for(int i =0; i<numPerLevel.size()-1; i++)
		{
			for (int j = 0; j < numPerLevel.get(i); j++) 
			{
				for (int j2 = 0; j2 < numPerLevel.get(i+1); j2++) 
				{
					int o =numPerLevel.get(i)+1; 
					System.out.println("mod"+tc+ "--" +"mod"+(o+j2));	
				}
				tc++;
			}
		}
	}
}