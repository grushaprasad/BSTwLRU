import java.util.*;

public class WordCount
{
  /**
   * Returns the alphabetic characters in the given string in the order in
   * which they appeared.
   *
   * @param s a string
   * @return a string containing the alphabetic characters from s
   */
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
  
  
  
  
  public static void main(String[] args)
  {
    Scanner in = new Scanner(System.in);
    
    ListOfCountMapsWithLRU<String> counts = new ListOfCountMapsWithLRU<String>() ;
    
    
    while (in.hasNext())
    {
      String word = in.next();
      word = word.toLowerCase();
      word = stripNonalphabetic(word);
      
      if (word.length() > 0)
      {
        counts.add(word);
        System.out.println("adding");
      }
    }
    
    // get the (word, count) list
    List<Map.Entry<String, Integer>> countList = counts.entryList();
    
    Collections.sort(countList, new Comparator<Map.Entry<String, Integer>>()
                       {
      public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2)
      {
        if (e1.getKey() != e2.getKey())
        {
          return e2.getValue() - e1.getValue();
        }
        else
        {
          return e1.getKey().compareTo(e2.getKey());
        }
      }
    });
    
    // output the top 100 words
    System.out.println("=== TOP 100 FREQUENCY === ");
    for (Map.Entry<String, Integer> e : countList)
    {
      System.out.println(e);
    }
    
    //System.out.println(counts);
    //System.out.println(counts.size());
  }
}