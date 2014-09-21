/**
 * Author: Tim Brown
 * Last Update: 3/5/13
 * Right now the program currently determines whether something is a question or not.
 * It is set up to run with some test data or manual input.
 * Running the program with no arguments runs it with the test data.
 * Running the program with a single string as an argument will output whether or not that sentence is a question.
 * Notes:
 * In the general case of a question the verb precedes the subject.
 * Can, Do, Would are all auxiliary verbs but we will treat them as verbs
 * Currently there is a limited list of subjects and verbs that I am working from.
 * If a subject follows one of the 5 W's then it is a statement.
 * I ran into a problem with commands like "Do this" or "Do that" so right now these specific cases are handled in a trivial way.
 * I do not think that I have figured out how to handle the underlying issue of these command statements.
 * The program prints a list of all the entries from the test questions that were labeled improperly.
 * Sentences are labeled as a 1 if it is a question and a 0 if it is not.
 * Right now the code only looks at the first subject, verb pairing in the sentence which could lead to issues.
 */

import java.lang.System;
import java.util.*;
import java.io.*;

public class QuestionAnalysis {
	private static HashSet<String> fiveWs = new HashSet<String>(Arrays.asList(new String[] {"who", "what", "when", "where", "why"}));;
	private static HashSet<String> verbs = new HashSet<String>(Arrays.asList(new String[] {"is", "does", "can", "do", "would", "are", "did", "don't", "be", "could", "would"}));
	private static HashSet<String> subjects  = new HashSet<String>(Arrays.asList(new String[] {"it", "they", "you", "we", "i", "this", "that", "those", "their", "mine", "yours", "people"}));
	private static HashSet<String> prepositions = new HashSet<String>(Arrays.asList(new String[] {"about", "above", "after", "among", "at", "as", "for", "from", "in", "inside", "into", "like", "to", "toward", "with", "without", "on", "off", "of", "onto"}));
	private static HashSet<String> actions = new HashSet<String>(Arrays.asList(new String[] {"can", "do", "does", "could", "would"}));
	private static HashSet<String> negations = new HashSet<String>(Arrays.asList(new String[] {"no", "not", "none"}));
	private static HashSet<String> possessives = new HashSet<String>(Arrays.asList(new String[] {"my", "mine", "your", "our", "their"}));
	private static HashSet<String> fillers = new HashSet<String>(Arrays.asList(new String[] {"like", "some", "thing", "stuff", "something", "a", "an", "the", "umm", "um", "ah"}));


	public static void main(String[] args){
		LinkedList<Integer> testResults = new LinkedList<Integer>();
		LinkedList<Integer> actualValues = new LinkedList<Integer>();
		if (args.length > 0){
			if (args.length == 1){
				System.out.println(isQuestion(args[0]));
				System.out.println(questionContent(args[0]));
			} else {
				System.out.println("Please enter a single sentence as a single string.");
			}
		} else {
			try{
				//The test questions are stored in testQuestions.txt
				FileReader input = new FileReader("/Users/tbrown126/Documents/workspace/question_answering/testQuestions.txt");
				BufferedReader br = new BufferedReader(input);
				String line = null;
				while((line = br.readLine()) != null){
					testResults.add(isQuestion(line));
				}
				input.close();
				br.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			try{
				/* The file testLabels.txt contains a column of 0,1 values indicating whether the question at that same 
				 * line in testQuestions.txt is actually a question
				 */
				FileReader input = new FileReader("/Users/tbrown126/Documents/workspace/question_answering/testLabels.txt");
				BufferedReader br = new BufferedReader(input);
				String line = null;
				while((line = br.readLine()) != null){
					actualValues.add(Integer.parseInt(line));
				}
				input.close();
				br.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			LinkedList<Integer> incorrect = new LinkedList<Integer>();
			//A simple check to make sure that there is a label for every test question
			if (testResults.size() == actualValues.size()){
				for (int i=0; i < testResults.size(); i++){
					if (testResults.get(i) != actualValues.get(i)){
						incorrect.add(i);
					}
				}
				//Prints out a list of all the indices of the questions that did not match the labels
				System.out.println(incorrect);
			} else {
				System.out.println("Error in analysis. # of results != # of labels");
			}
		}
	}

	/** isQuestion takes in a string s as the argument to the function.
	 * It takes this string and analyzes it to determine whether or not it is a question.
	 * @param s, the statement being analyzed
	 * @return If s is a question, it outputs a 1. If s is not a question, it outputs a 0.
	 */
	public static Integer isQuestion(String s){
		s = s.toLowerCase();
		String[] words = s.split("\\s+");
		int indexOfSubject = -1;
		int indexOfVerb = -1;
		for (int i=0; i<words.length; i++){
			String currentWord = words[i].replaceAll("([a-z]+)[?:!.,;]*", "$1");
			if (verbs.contains(currentWord)){
				indexOfVerb = i;
				/*
				 * This section of determining whether or not the sentence is a question is a bit of a hack.
				 * The case is hard coded in. This is the command case (example: "Do this").
				 */
				if (currentWord.equals("do") && i < (words.length - 1) && (words[i+1].equals("this") || words[i+1].equals("that"))){
					return 0;
				}
			}
			if (subjects.contains(currentWord)){
				indexOfSubject = i;
				//Handles the case where the subject immediately follows one of the 5 W's
				if (i > 0 && fiveWs.contains(words[i-1].replaceAll("([a-z]+)[?:!.,;]*", "$1"))) {
					return 0;
				}
			}
			//Takes first verb, subject pairing
			if (indexOfSubject != -1 && indexOfVerb != -1){
				break;
			}
		}
		//This is the general case where the index of the verb is before that of the subject in a question
		if (indexOfSubject != -1 && indexOfVerb != -1 && indexOfSubject > indexOfVerb){
			return 1;
		}
		return 0;
	}

	/**
	 * Designed to take out trailing prepositional phrases from questions.
	 * I do not believe these portions of the question are necessary to return an answer.
	 * @param words
	 * @return array of strings to be used in questionContent without the prepositional phrase
	 */
	public static String[] removePrepositionalPhrase(String[] words){
		int cutOff = -1;
		for (int i=0; i<words.length; i++){
			String current = words[i].replaceAll("([a-z]+)[?:!.,;]*", "$1");
			if (prepositions.contains(current)){
				cutOff = i;
				for (int j=i+1; j<words.length; j++){
					if (verbs.contains(words[j].replaceAll("([a-z]+)[?:!.,;]*", "$1"))){
						cutOff = -1;
						break;
					}
				}
			}
		}
		if (cutOff != -1){
			String [] ret = new String[cutOff];
			for (int i=0; i<ret.length; i++){
				ret[i] = words[i];
			}
			return ret;
		} else {
			return words;
		}
	}

	/**
	 * Tries to extract the content of the prepositional phrase.
	 * @param words, an array of strings that represent the phrase to be analyzed
	 * @return the content of the prepositional phrase
	 */
	public static String prepositionContent(String[] words){
		int cutOff = -1;
		for (int i=0; i<words.length; i++){
			String current = words[i].replaceAll("([a-z]+)[?:!.,;]*", "$1");
			if (prepositions.contains(current)){
				cutOff = i;
				for (int j=i+1; j<words.length; j++){
					String w = words[j].replaceAll("([a-z]+)[?:!.,;]*", "$1");
					//contains color hack for case "... in color"
					if (verbs.contains(w) || w.equals("color")){
						cutOff = -1;
						break;
					}
				}
			}
		}
		if (cutOff != -1 && cutOff+1 < words.length){
			String ret = "";
			for (int i=cutOff+1; i<words.length; i++){
				String current = words[i].replaceAll("([a-z]+)[?:!.,;]*", "$1");
				if (!(fillers.contains(current) || subjects.contains(current) || possessives.contains(current))){
					ret += " " + current;
				}
			}
			return ret.trim();
		} else {
			return "";
		}
	}
	/**
	 * Method for getting the meat out of the question.
	 * @param s, the question as a string
	 * @return what the question is really addressing, as a string
	 */
	public static List<String> questionContent(String s){
		s = s.toLowerCase();
		s = removeContraction(s);
		String[] words = s.split("\\s+");
		String prepContent = prepositionContent(words);
		words = removePrepositionalPhrase(words);
		int indexOfSubject = -1;
		int indexOfW = -1;
		int indexOfVerb = -1;
		for (int i=0; i<words.length; i++){
			String currentWord = words[i].replaceAll("([a-z]+)[?:!.,;]*", "$1");
			if (subjects.contains(currentWord)){
				indexOfSubject = i;
				if (i > 0 && actions.contains(words[i-1].replaceAll("([a-z]+)[?:!.,;]*", "$1"))){
					break;
				}
			}
			if (verbs.contains(currentWord)){
				indexOfVerb = i;
			}
			if (fiveWs.contains(currentWord)){
				indexOfW = i;
			}
		}
		//
		words = removePrepositionalPhrase(words);
		int startIndex = indexOfSubject;
		int endIndex = words.length;
		
		if (indexOfW != -1 && indexOfW < indexOfSubject){
			startIndex = indexOfW;
			endIndex = indexOfVerb;
		}
		String ret = "";
		for (int i = startIndex+1; i<endIndex; i++){
			String current = words[i].replaceAll("([a-z]+)[?:!.,;]*", "$1");
			if (!current.equals("have")&& !current.equals("has") && !fillers.contains(current) && !verbs.contains(current) && !negations.contains(current) && !subjects.contains(current)){
				//color & it hack
				if (!(i==endIndex-1 && current.equals("color")) && !current.equals("it")){
					ret += current + " ";
				}
			}
		}
		LinkedList<String> retList = new LinkedList<String>();
		retList.add(ret);
		retList.add(prepContent);
		return retList;
	}

	/**
	 * Determines whether or not the response to the question should be negated by counting the number of negatives.
	 * @param s, the question unaltered
	 * @return true or false, whether we should negate the response or not
	 */
	public static boolean negate(String s){
		s = s.toLowerCase();
		String[] words = s.split("\\s+");
		int count = 0;
		for (int i=0; i<words.length; i++){
			String currentWord = words[i].replaceAll("([a-z]+)[?:!.,;]*", "$1");
			if (negations.contains(currentWord)){
				count++;
			}
		}
		if (count % 2 == 0){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Takes a question and removes the contractions and replaces them with the two words they represent.
	 * @param s, the sentence
	 * @return the sentence without any contractions
	 */
	public static String removeContraction(String s){
		String[] words = s.split("\\s+");
		String output = "";
		for (int i=0; i<words.length; i++){
			String current = words[i];
			if (current.contains("'")){
				int index = current.indexOf("'");
				if (current.charAt(index+1) == 's'){
					output += current.substring(0, index) + " is";
				} else if (current.charAt(index+1) == 't'){
					if (current.equals("won't")){
						output += "will not";
					} else if(current.equals("can't")){
						output += "can not";
					} else if (current.equals("don't")){
						output += "do not";
					} else {
						output += current.substring(0, index) + " not";
					}
				} else {
					output += current;
				}
			} else {
				output += current;
			}
			output += " ";
		}
		return output;
	}
}
