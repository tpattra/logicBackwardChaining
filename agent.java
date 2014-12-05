import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class agent {
	
	public static ArrayList<String> tmpfacts, tmpclauses, goals, entail;
	public static Map<String, String[]> splitter;
	public static String query;
	public static int numClause;
	public static String var;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 // read file input.txt
		tmpfacts = new ArrayList<String>();
		tmpclauses = new ArrayList<String>();
		
        FileReader fileReader = new FileReader("input.txt");
        BufferedReader br = new BufferedReader(fileReader);
        
        query = br.readLine();
        System.out.println(query);
        numClause = Integer.parseInt(br.readLine());
        
        String line = "";
        while((line = br.readLine()) != null){
        	if(!line.contains("=>")){
        		tmpfacts.add(line);		
        	}
        	else{
        		tmpclauses.add(line);
        		
        	}
        	
        }
        // get the variable x sub
        var = query.substring(query.indexOf("(")+1, query.indexOf(","));
  
        output();
        
	}

	private static void output() {
		// TODO Auto-generated method stub
		String decision = "";
		if (backwardChaining()){
			decision = "TRUE";
		}
		else{
			decision = "FALSE";
		}
	 	
		PrintWriter writer;
		try {
			writer = new PrintWriter("output.txt", "UTF-8");
			writer.write(decision);
	    	writer.close();
	    	System.out.println(decision);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
	
	public static String[] splitter(String function){
		String[] literal = new String[3];
		String []tmp = function.split("\\(");
		literal[0] = tmp[0];
		
		if(tmp[1].contains(",")){
			literal[1] = tmp[1].substring(0, tmp[1].indexOf(","));
			literal[2] = tmp[1].substring(tmp[1].indexOf(",")+1, tmp[1].lastIndexOf(")"));
		}
		else{
			literal[1] = literal[1] = tmp[1].substring(0, tmp[1].indexOf(")"));
			literal[2] = "N/A";
		}
		//System.out.println("function: " + function+ " literal: " + Arrays.deepToString(literal));
		return literal;
	}

	private static boolean backwardChaining() {
		// TODO Auto-generated method stub
		goals = new ArrayList<String>();
		entail = new ArrayList<String>();
		goals.add(query);
		
		while(!goals.isEmpty()){
		
			String q = goals.remove(goals.size()-1);
				
			System.out.println("q = " + q);
			entail.add(q);
			System.out.println("entail = " + entail);
			
			if(!tmpfacts.contains(q)){
				ArrayList<String> pres = new ArrayList<String>();
				
				for(int i=0; i<tmpclauses.size(); i++){
					System.out.println("tmpcl="+tmpclauses.get(i));
				
					boolean[] conchk = checkConcurrence(tmpclauses.get(i), q);
					if(conchk[0]){//&& getConcurrence(tmpclauses.get(i)).contains("x")
						String[] preced = getPrecedences (tmpclauses.get(i));
						
						for(int j=0; j<preced.length; j++){
							if(getVariable(getConcurrence(tmpclauses.get(i))).equals("x")){
								pres.add(preced[j].replace("x", getVariable(q)));
							}
							else{
								if(conchk[1] && conchk[2]){
									for (int k=0; k<tmpfacts.size(); k++){
										String replaced = preced[j].replace("x",getVariable(tmpfacts.get(k)));
										if(replaced.equals(tmpfacts.get(k))){
										pres.add(replaced);
									}
									}
								}
							}
						}
						System.out.println("-----PRES= "+ pres);
						
					}
				}
			
				if(pres.size() == 0){
					return false;
				}
				else{
					for(int i=0; i<pres.size(); i++){
						if(!entail.contains(pres.get(i))){
							goals.add(pres.get(i));
						}
					}
					System.out.println("GOALS = "+ goals);
				}
			}
		}
	//	}
		return true;
	
	}
	
	private static String[] getPrecedences (String clause){
		String prec = clause.split("=>")[0];
		String [] atom;
		if(prec.contains("&")){
			atom = new String[2];
			atom = prec.split("&");
		}
		else{
			atom = new String[1];
			atom[0] = prec;
		}
		return atom;
		
	}
	
	
	private static boolean[] checkConcurrence (String clause, String sub){
		// check at function name as main stand point
		String cl = clause.split("=>")[1];
		boolean [] flag = new boolean [3];
		flag[0] = true;
		flag[1] = true; 
		flag[2] = true; // same for all function, var, and info
		if(!cl.equals(sub)){	
		 if(!getFunction(cl).equals(getFunction(sub))) flag[0] = false;
		 if(!getVariable(cl).equals(getVariable(sub))) flag[1] = false;
		 if(!getInfo(cl).equals(getInfo(sub))) flag[2] = false;
		}
		return flag;
	}
	
	private static String getConcurrence (String cluase){
		String rhs = cluase.split("=>")[1];
		return rhs;
	}
	
	// get function(VAR,info)
	private static String getVariable (String function){
		String [] value = splitter(function);
		String varname = value[1]; 
		return varname;
	}
	
	// get FUNCTION(var,info)
	private static String getFunction (String function){
		String [] value = splitter(function);
		String fname = value[0];
		return fname;
	}
	
	// get function(var, INFO)
	private static String getInfo (String function){
		String [] value = splitter(function);
		String info = value[2];
		return info;
	}
	

}
