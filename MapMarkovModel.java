import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class MapMarkovModel extends MarkovModel
{
	public Map<String, ArrayList<Character>> makeMap(int k)
	{	
		Map<String, ArrayList<Character>> myMap = new HashMap <String, ArrayList<Character>>();
		
		for (int i = 0; i <= myString.length()-k; i++) {
			
        	if (!myMap.containsKey(myString.substring(i, i+k))){
        		myMap.put(myString.substring(i, i+k), new ArrayList<Character>());
        	}
        	
        	ArrayList<Character> list = myMap.get(myString.substring(i, i+k));
        	if (i+k == myString.length()) {
        		list.add((char) 0);
        	}
        	else {
        		list.add(myString.charAt(i+k));
        	}
        }
		
		return myMap;
	}
	
	protected String makeNGram(int k, int maxLetters)
	{
		Map<String, ArrayList<Character>> markovMap = new HashMap <String, ArrayList<Character>>();
		markovMap = makeMap(k);
		
        int start = myRandom.nextInt(myString.length()-k+1);
        String seed = myString.substring(start, start+k);
        
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < maxLetters; i++) {
        	ArrayList<Character> list = markovMap.get(seed);
        	int pick = myRandom.nextInt(list.size());
        	if (list.get(pick) == 0) {
        		break;
        	}
        	seed = seed.substring(1) + list.get(pick);
        	text.append(seed.charAt(seed.length()-1));
        }	
        
        return text.toString();
	}
}
