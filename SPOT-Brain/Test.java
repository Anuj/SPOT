import java.lang.System;
import java.util.*;
import java.io.*;
import java.io.FileInputStream;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Test {
	private static HashSet<String> responses = new HashSet<String>(Arrays.asList(new String[] {"yes", "no", "edge", "reveal"}));
	
	public static void main(String[] args){
	    BufferedWriter writerAll = null;
	    BufferedWriter writerCorrect = null;
	    BufferedWriter writerWrong = null;
	    BufferedWriter writerAll2 = null;
	    BufferedWriter writerCorrect2 = null;
	    BufferedWriter writerWrong2 = null;

	    try{
		writerAll = new BufferedWriter( new FileWriter("/Users/tbrown126/Documents/Puppybox/results.txt"));
		writerCorrect = new BufferedWriter( new FileWriter("/Users/tbrown126/Documents/Puppybox/resultsCorrect.txt"));
		writerWrong = new BufferedWriter( new FileWriter("/Users/tbrown126/Documents/Puppybox/resultsIncorrect.txt"));
		writerAll2 = new BufferedWriter( new FileWriter("/Users/tbrown126/Documents/Puppybox/resultsImperfect.txt"));
		writerCorrect2 = new BufferedWriter( new FileWriter("/Users/tbrown126/Documents/Puppybox/resultsCorrectImperfect.txt"));
		writerWrong2 = new BufferedWriter( new FileWriter("/Users/tbrown126/Documents/Puppybox/resultsIncorrectImperfect.txt"));
	    
	    int errors = 0;
	    int total = 0;
	    System.out.println("-------------------------------------");
	    System.out.println("Test results for ideal inputs:");
	    System.out.println();
	    try{
		//The test questions are stored in testQuestions.txt
		FileReader input = new FileReader("/Users/tbrown126/Documents/Puppybox/testQuestions.txt");
		BufferedReader br = new BufferedReader(input);
		String line = null;
		AnswerQuery current = null;
		while((line = br.readLine()) != null){
		    if (!line.contains("?") && line.split("\\s+").length <= 1 && !responses.contains(line)){
			current = new AnswerQuery(line);
		    } else {
			String reply = current.reply(line);
			String nextLine = br.readLine();
			if (!reply.equals(nextLine) && nextLine != null){
			    System.out.println(line);
			    System.out.println("Answered: " + reply + " Should have been: " + nextLine + " for object: " + current.getItem());
			    writerWrong.write(current.getItem()+"\t"+line+"\t"+nextLine+"\t"+reply+"\t"+current.getContent()+"\t"+current.getPrep()+"\n");
			    errors++;
			} else{
			    writerCorrect.write(current.getItem()+"\t"+line+"\t"+nextLine+"\t"+reply+"\t"+current.getContent()+"\t"+current.getPrep()+"\n");
			}
			writerAll.write(current.getItem()+"\t"+line+"\t"+nextLine+"\t"+reply+"\t"+current.getContent()+"\t"+current.getPrep()+"\n");
			total++;
		    }
		}
		input.close();
		br.close();
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	    System.out.println("Errors: " + errors + " out of " + total);
	    System.out.println("-------------------------------------");
	    System.out.println("Test results for non-ideal inputs:");
	    System.out.println();
	    errors = 0;
	    total = 0;
	    try{
		//The test questions are stored in testQuestions.txt
		FileReader input = new FileReader("/Users/tbrown126/Documents/Puppybox/testQuestionsImperfect.txt");
		BufferedReader br = new BufferedReader(input);
		String line = null;
		AnswerQuery current = null;
		while((line = br.readLine()) != null){
		    if (!line.contains("?") && line.split("\\s+").length <= 1 && !responses.contains(line)){
			current = new AnswerQuery(line);
		    } else {
			String reply = current.reply(line);
			String nextLine = br.readLine();
			if (!reply.equals(nextLine) && nextLine != null){
			    System.out.println(line);
			    System.out.println("Answered: " + reply + " Should have been: " + nextLine + " for object: " + current.getItem());
			    errors++;
			    writerWrong2.write(current.getItem()+"\t"+line+"\t"+nextLine+"\t"+reply+"\t"+current.getContent()+"\t"+current.getPrep()+"\n");
			} else{
			    writerCorrect2.write(current.getItem()+"\t"+line+"\t"+nextLine+"\t"+reply+"\t"+current.getContent()+"\t"+current.getPrep()+"\n");
			}
			writerAll2.write(current.getItem()+"\t"+line+"\t"+nextLine+"\t"+reply+"\t"+current.getContent()+"\t"+current.getPrep()+"\n");
			total++;
		    }
		}
		input.close();
		br.close();
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	    System.out.println("Errors: " + errors + " out of " + total);
	    System.out.println("-------------------------------------");
	    writerAll.close();
	    writerCorrect.close();
	    writerWrong.close();
	    writerAll2.close();
	    writerCorrect2.close();
	    writerWrong2.close();
	    }catch ( IOException e){
	    }
	}
}