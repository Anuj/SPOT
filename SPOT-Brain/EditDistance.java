/**
 * A Levenshtein distance calculator.
 * @author tbrown126
 *
 */
public class EditDistance {
	
	public static void main(String[] args){
		System.out.println(calcDistance(args[0], args[1]));
	}
	
	/**
	 * Used to calculate the edit distance between two phrases
	 * @param s1 phrase 1
	 * @param s2 phrase 2
	 * @return the Levenshtein distance between the two phrases
	 */
	public static int calcDistance(String s1, String s2){
	    if (s1.equals(s2)){
		return 0;
	    }
	    else{
		int n = s1.length();
		int m = s2.length();
		int e[][] = new int[n+1][m+1]; 
		for (int i=0; i<=n; i++){
		    e[i][0] = i;
		}
		for (int j=1; j<=m; j++){
		    e[0][j] = j;
		}
		for (int i=1; i<=n; i++){
		    for (int j=1; j<=m; j++){
			int diff = 1;
			if (s1.charAt(i-1) == s2.charAt(j-1)){
			    diff = 0;
			}
			e[i][j] = Math.min(e[i-1][j]+1, Math.min(e[i][j-1] +1, e[i-1][j-1]+diff));
		    }
		}
		return e[n][m];
	    }
	}
}

