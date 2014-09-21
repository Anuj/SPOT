import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class used to answer the questions supplied by the user in the PuppyBox project
 * @author tbrown126
 */
public class AnswerQuery {
    private static HashSet<String> locationIndicators = new HashSet<String>(Arrays.asList(new String[]{"find", "locate", "look at", "see", "go", "belong", "look", "put", "place", "live", "keep", "use"}));
	private ArrayList<String> properties;
	private ArrayList<String> locations;
    private ArrayList<String> otherProperties;
    private ArrayList<String> otherLocations;
	private String item;
    private String otherItem;
	private HashMap<String, ArrayList<String>> synonyms;
	private HashMap<String, String> commonErrors;
	private static HashSet<String> letterIndicators = new HashSet<String>(Arrays.asList(new String[]{"start", "starts", "end", "ends", "begin", "begins", "first", "second"}));
	private String content;
	private String prepContent;
	private boolean negate;

	/**
	 * Takes the first string from the command line as the item that questions are being asked about and the second string as the question.
	 * Used for testing the system without the flash component
	 */
	public static void main(String[] args){
		AnswerQuery test = new AnswerQuery(args[0]);
		System.out.println(test.reply(args[1]));
	}

	/**
	 * This method is used to answer questions about whether an item starts, ends, etc with a certain letter.
	 * @param indicator refers to the position of the letter in question
	 * @param content refers to the content which will contain the letter in question
	 * @param item is the item that the question is being asked about
	 * @return the answer to the question, true or false
	 */
	private static boolean letterHelper(String indicator, String content, String item){
		int index = 0;
		if (indicator.equals("end") || indicator.equals("ends") || indicator.equals("last")){
			index = item.length()-1;
		}
		if (indicator.equals("second")){
			index = 1;
		}
		String[] words = content.split("\\s+");
		String letter = "";
		for (int i=0; i<words.length; i++){
			if (words[i].length() == 1){
				letter = words[i];
			}
		}
		if (letter.equals(item.substring(index, index+1))){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * The initializer for the class. Sets up all the info for the object.
	 * Also sets up the table for common errors made by google speech.
	 * @param item, the item that is being hidden in the game
	 */
	public AnswerQuery(String item) {
		this.item = item;
		otherItem = XmlReader.getOtherItem(item);
		ArrayList<ArrayList<String>> parsed = XmlReader.getData(item.toLowerCase());
		properties = parsed.get(0);
		locations = parsed.get(1);
		ArrayList<ArrayList<String>> parsedOther = XmlReader.getData(otherItem.toLowerCase());
		otherProperties = parsedOther.get(0);
		otherLocations = parsedOther.get(1);
		synonyms = new HashMap<String, ArrayList<String>>();
		ArrayList<String> bikeSyn = new ArrayList<String>(Arrays.asList(new String[]{"bicycle", "bike"}));
		ArrayList<String> bootSyn = new ArrayList<String>(Arrays.asList(new String[]{"boot", "shoe"}));
		synonyms.put("boot", bootSyn);
		synonyms.put("bicycle", bikeSyn);
		commonErrors = new HashMap<String, String>();
		commonErrors.put("bills", "wheels");
		commonErrors.put("spring", "string");
		commonErrors.put("mean now", "meow");
		commonErrors.put("beaches", "pages");
		commonErrors.put("for", "fur");
		commonErrors.put("app", "have");
		commonErrors.put("asking", "skin");
		commonErrors.put("free", "furry");
		commonErrors.put("hurt", "purr");
		commonErrors.put("from me", "friendly");
	}

	/**
	 * Checks whether or not a question is asking specifically if it is an object (Ex: is it the cat?).
	 * @param s, contains the content of the question
	 * @param current, the item you're checking the reveal for
	 * @return whether or not we should send the reveal command
	 */
    public boolean checkReveal(String s, String current){
		if (synonyms.containsKey(current)){
			ArrayList<String> syns = synonyms.get(current);
			for (int i=0; i<syns.size(); i++){
				int objDist = EditDistance.calcDistance(syns.get(i), s);
				if (objDist <= s.length()/3){
					return true;
				}
			}
			return false;
		} else {
			int objDist = EditDistance.calcDistance(s, current);
			if (objDist <= s.length()/3){
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * @return the item for this instance of the class
	 */
	public String getItem(){
		return item;
	}
	
	/**
	 * @return the content of the question that was just asked
	 */
	public String getContent(){
		return content;
	}
	
	/**
	 * @return the content of the preposition of the last question
	 */
	public String getPrep(){
		return prepContent;
	}
	
	/**
	 * This method retries asking the question and replaces the commom errors made by google speech to text with what they actually are.
	 * @param q, the question
	 * @param prop, the list of properties for the item
	 * @param loc, the list of locations for the item
	 * @return the response with the changes made
	 */
    public String retry(String q, ArrayList<String> prop, ArrayList<String> loc){
		String newQ = q;
		Iterator<String> keys = commonErrors.keySet().iterator();
		while (keys.hasNext()){
			String current = keys.next();
			int index = q.indexOf(current);
			if (index != -1){
				newQ = q.replaceAll(current, commonErrors.get(current));
			}
		}
		return reply2(newQ.trim(), prop, loc);
	}

	/**
	 * attempts to answer the query through the usual means and then calls retry() if necessary
	 * @param q, takes a question
	 * @return the response to the question
	 */
	public String reply(String q){
	    String ans1 = reply2(q, properties, locations);
	    if (ans1.equals("edge") || ans1.equals("edge2") || (ans1.equals("no") && !negate) || (ans1.equals("yes") && negate)){
		ans1 =  retry(q, properties, locations);
	    }
	    if (ans1.equals("yes") || ans1.equals("edge") || ans1.equals("edge2")){
		return ans1;
	    } else {
		String ans2 = reply2(q, otherProperties, otherLocations);
		if (ans2.equals("edge") || ans2.equals("edge2") || (ans2.equals("no") && !negate) || (ans2.equals("yes") && negate)){
		    ans2 =  retry(q, otherProperties, otherLocations);
		}
		if (ans2.equals("no") && ans1.equals("no")){
		    return "edge2";
		} else {
		    return ans1;
		}
	    }
	}

	/**
	 * Attempts to answer the question, retry is called from this method and reply calls this method
	 * @param q, the question that is being asked
	 * @param prop, the list of properties for the item
	 * @param loc, the list of locations for the item
	 * @return the response to the question
	 */
    public String reply2(String q, ArrayList<String> prop, ArrayList<String> loc){
		negate = QuestionAnalysis.negate(q);
		List<String> qData = QuestionAnalysis.questionContent(q);
		content = qData.get(0).trim();
		prepContent = qData.get(1).trim();
		if (content.length() == 0 && prepContent.length() > 0) {
			content = prepContent;
			prepContent = "";
		}
		if (QuestionAnalysis.isQuestion(q) == 1 && content.length() > 0){
		    boolean reveal = checkReveal(content, item);
		    boolean reveal2 = checkReveal(content, otherItem);
			if (reveal){
				if (negate){
					return "no";
				} else {
					return "reveal";
				}
			} else if (reveal2){
			    if (negate){
					return "no";
				} else {
					return "reveal2";
				}
			} else if (letterIndicators.contains(content)){
				if (letterHelper(content, prepContent, item)){
					if (negate){
						return "no";
					} else {
						return "yes";
					}
				} else {
					if (negate){
						return "yes";
					} else {
						return "no";
					}
				}
			} else {
				for (int i=0; i<prop.size(); i++){
					int dist = EditDistance.calcDistance(content, prop.get(i));
					//Need to find out what an acceptable edit distance is based on the phrase
					if (dist <= content.length()/3 || locationIndicators.contains(content)){
						if (prepContent.length() > 0){
							for (int j=0; j<loc.size(); j++){
								int dist2 = EditDistance.calcDistance(prepContent, loc.get(j));
								if (dist2 <= prepContent.length()/3){
									if (negate){
										return "no";
									} else {
										return "yes";
									}
								}
							}
						} else {
							if (negate){
								return "no";
							} else {
								return "yes";
							}
						}
					}
				}
			}
			if (negate){
				return "yes";
			} else {
				return "no";
			}
		} else if (content.length() == 0) {
			return "edge2";
		} else {
		    return "edge";
		}
	}
}