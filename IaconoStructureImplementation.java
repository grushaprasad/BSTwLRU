import java.util.*;

public class IaconoStructureImplementation {
  
  public static String stripNonalphabetic(String s)
  {
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < s.length(); i++)
    {
      if (Character.isLetter(s.charAt(i)))
      {
        buffer.append(s.charAt(i));
      }
    }
    return buffer.toString();
  }
  
  
  public static void main (String [] args) {
    Scanner scan = new Scanner(System.in);
    ListOfCountMapsWithLRU<String> list = new ListOfCountMapsWithLRU<String>();
    
    while (scan.hasNext())
    {
      String word = scan.next();
      word = word.toLowerCase();
      word = stripNonalphabetic(word);
      
      if (word.length() > 0)
      {
        list.add(word);
        //System.out.println("adding");
      }
    }
    
   /* list.add("Hi");
    list.add("what");
    list.add("Hi"); */
    
    System.out.println("First element " + list.get(0));
    System.out.println("Second element " + list.get(1));
    
   
    
    
    
    ListOfCountMapsWithLRU<String> entryList = list.entryList();
    System.out.println("The size of the entryList is " + entryList.size());
    for (Map.Entry<String, Integer> e : entryList)
    {
      System.out.println(e);
    }
  }
}