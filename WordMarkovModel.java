import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class WordMarkovModel extends AbstractModel
{
	protected String myString;
	protected Random myRandom;
	public static final int DEFAULT_COUNT = 100; // default # random letters generated
	public static int RANDOM_SEED = 1234; 
	protected String[] words;
	 
	public WordMarkovModel() 
	{
		myRandom = new Random(RANDOM_SEED);
	}
	
	public void initialize(Scanner s) 
	{
        double start = System.currentTimeMillis();
        int count = readChars(s);
        double end = System.currentTimeMillis();
        double time = (end - start) / 1000.0;
        super.messageViews("#read: " + count + " chars in: " + time + " secs");
    }
	
	protected int readChars(Scanner s) 
	{
        myString = s.useDelimiter("\\Z").next();
        words = myString.split("\\s+");
        s.close();
        return myString.length();
    }
	
	public void process(Object o) 
	{
        String temp = (String) o;
        String[] nums = temp.split("\\s+");
        int k = Integer.parseInt(nums[0]);
        int maxLetters = DEFAULT_COUNT;
        if (nums.length > 1) 
        {
            maxLetters = Integer.parseInt(nums[1]);
        }
        
        double stime = System.currentTimeMillis();
        String text = makeNGram(k, maxLetters);
        double etime = System.currentTimeMillis();
        double time = (etime - stime) / 1000.0;
        this.messageViews("time to generate: " + time +" | chars generated:" + 
        		text.length()); //For benchmarking purposes
        this.notifyViews(text);
        
    }
	 
	public Map<WordNgram, ArrayList<WordNgram>> makeMap(int k)
	{
		Map<WordNgram, ArrayList<WordNgram>> myMap = new HashMap<WordNgram, ArrayList<WordNgram>>();
		for (int i = 0; i < words.length-k; i++) 
        {
			WordNgram key = new WordNgram(words, i, k);
        	if (!myMap.containsKey(key))
        	{
        		myMap.put(key, new ArrayList<WordNgram>());
        	}
        	
        	ArrayList<WordNgram> list = myMap.get(key);
        	if (i+k == words.length)
        	{
        		list.add(null);
        	}
        	else
        	{
        		WordNgram value = new WordNgram(words, i+1, k);
        		list.add(value);
        	}
        }
		
		return myMap;
	}
	
	protected String makeNGram(int k, int maxLetters)
	{
		Map<WordNgram, ArrayList<WordNgram>> markovMap = new HashMap<WordNgram, ArrayList<WordNgram>>();
		markovMap = makeMap(k);
		System.out.println(markovMap.keySet().size());
		
        int start = myRandom.nextInt(words.length-k+1);
        WordNgram seed = new WordNgram(words, start, k);
        //System.out.println(seed);
        
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < maxLetters; i++)
        {
        	ArrayList<WordNgram> values = markovMap.get(seed);
        	
        	if (values != null)
        	{
        		int pick = myRandom.nextInt(values.size());
        		if (values.get(pick) == null)
            	{
            		break;
            	}
            	seed = values.get(pick);
            	text.append(seed.getLastWord(k));
            	text.append(" ");
        	}
        }	
        //text.deleteCharAt(text.length()-1);
        System.out.println(text);
        return text.toString();
	}
}
