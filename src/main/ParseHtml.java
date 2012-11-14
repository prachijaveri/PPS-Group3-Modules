package main;

import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHtml 

{
	private static String[] course_related_words={
		"course",
		"prerequisite",
		"instructor",
		"textbook",
		"description",
		"level",
		"credit",
		"unit",	
		"course number",
		"course name",
		
	};
	static boolean checkRelevance(String s, int x)
	{
		boolean f =false;
		int cnt =0;
		for(int i=0;i<course_related_words.length;i++)
		{
			int index = s.indexOf(course_related_words[i]);
			if(index > 0)
			{
				cnt ++;
				if(course_related_words[i].endsWith("course"))
					f= true;
			}
		}
		if(x == 1)
		{
			if(cnt < 3 )
				return false;
			else
				return true;
		}
		else
		{
			if(cnt == 1 && f)
				return false;
			else if (cnt > 0)
				return true;
			else
				return false;
		}
	}
	
	static String parsePage(String u)
	{
		String first_page="";
		String all_text="";
		LinkedList<String> urls=new LinkedList<String>();
		boolean first_page_flag =true;
		urls.add(u);
		while(!urls.isEmpty())
		{
//			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
			Document doc = null;
			try
			{
				Thread.sleep(1000);
				//doc = Jsoup.connect(c.getUrl()).get();
				doc = Jsoup.connect(urls.removeFirst()).get();
			}
			catch (Exception e)
			{	
//				System.out.println("error : "+e);
			}		
			if(doc != null)
			{
				boolean flag = true;
				String headings = "h1 h2 h3 h4 h5 h6";
				String paragraph="p";	
				String ahref="a";
				String previoustype="";
				int para_count=0;
				Elements all_tags_elements = new Elements();
				if(first_page_flag)
					first_page = doc.body().text(); 
				all_tags_elements.add(doc);
				if(! checkRelevance(doc.body().text() ,1))
					flag = false;
				while(all_tags_elements.size() != 0 && flag) 	
				{
					Elements all_links = new Elements();
					Iterator<Element> i = all_tags_elements.iterator();
					while(i.hasNext())		 
					{
						Element element = i.next();
						String tag_name=element.tagName();
						if(headings.contains(tag_name))
						{
							String heading = element.text().trim().toLowerCase();
							if(!heading.equals(" ") && !heading.equals("") && (checkRelevance(heading,2)) )//!! checkWithWiki()))
							{
								previoustype = "heading";
								para_count=0;
//								System.out.println("HEADING\t"+heading);
								all_text = all_text +" "+heading;
							}
						}
						else if(paragraph.contains(tag_name))
						{	
							String p = element.text().toLowerCase().trim();
							if(!p.equals(" ") && !p.equals("")  && previoustype.equals("heading") && p.split(" ").length > 10) 
							{
								para_count++;
//								System.out.println("PARAGRAPH\t"+p);
								all_text = all_text +" "+p;
								if(para_count == 3)
									previoustype="";
							}
						}
						else if(ahref.contains(tag_name))
						{
							previoustype = "";
							para_count=0;
							String l = element.attr("abs:href");
							String s = element.text().toLowerCase().trim();
							if(first_page_flag && !s.equals(" ") && !s.equals(""))
							{
								urls.add(l);
							}
						}
						all_links.addAll(element.children());
						i.remove();
					}
					all_tags_elements = all_links;
				}							
			}
		}
		System.out.println("*********************************************************");
		System.out.println(all_text);
		if(all_text.split(" ").length < 500)
			return first_page;
		return all_text;
	}
}
