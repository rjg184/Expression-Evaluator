package apps;

import java.io.*;
import java.util.*;

import structures.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                
    
	/**
	 * Scalar symbols in the expression 
	 */
	ArrayList<ScalarSymbol> scalars;   
	
	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
    }

    /**
     * Populates the scalars and arrays lists with symbols for scalar and array
     * variables in the expression. For every variable, a SINGLE symbol is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadSymbolValues method.
     */
    public void buildSymbols() {
    	
    	// Initializing ArrayList Variables
        scalars = new ArrayList <ScalarSymbol>();
        arrays = new ArrayList <ArraySymbol>();
        
        // Initializing Variables
        String symbol = "";
        String empty = "";
        String expression = expr;

        //System.out.println("- - - - - - - - - -");
        //System.out.println("-> Orig. Expression: " + expression);
        //expression = expression.replaceAll(" ", "");
        //System.out.println("-> Fixed Expression: " + expression);
        //System.out.println("- - - - - - - - - -");
        
        // Iterating through given expression (character by character)
        for(int i = 0; i < expression.length(); i++){
        	
        	// Character i is a letter (a-z or A-Z)
        	if(Character.isLetter(expression.charAt(i))){
        		
        		// Initializing Variables
        		symbol = empty; //empty string
        						//either the current ArraySymbol or ScalarSymbol
        							
        		// Assigns the scalar or array symbol to Symbol string
        		for(; i < expression.length(); i++){
        		  if(Character.isLetter(expression.charAt(i))){
        		    symbol = symbol + expression.charAt(i);
        		  } else {
        		    break;
        		  }
        		}
        		
        		// Array Symbol
        		if(i < expression.length() && expression.charAt(i) == '['){
        			
        			if(this.arr(symbol) == false){
        				arrays.add(new ArraySymbol(symbol)); //adds array variable to ArraySymbol
        				//System.out.println("ArraySymbol: " + symbol); //prints array symbol
        				//System.out.println("- - - - - - - -");
        			} else {
        				//System.out.println("*Did not add: " + symbol + " -> array already exists in list");
        				//System.out.println("- - - - - - - -");
        			}
        			
        		// Scalar Symbol
        		} else {
        			
        			if(this.scalr(symbol) == false){
        				scalars.add(new ScalarSymbol(symbol)); //adds scalar variable to ScalarSymbol
        				//System.out.println("ScalarSymbol: " + symbol); //prints scalar symbol
        				//System.out.println("- - - - - - - -");
        			} else {
        				//System.out.println("*Did not add: " + symbol + " -> scalar already exists in list");
        				//System.out.println("- - - - - - - -");
        			}
        			
        		}
        		
        	} //end of if statement
        	
        } //end of for loop
        
    } //end of method
    
    /**
     * Loads values for symbols in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadSymbolValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String sym = st.nextToken();
            ScalarSymbol ssymbol = new ScalarSymbol(sym);
            ArraySymbol asymbol = new ArraySymbol(sym);
            int ssi = scalars.indexOf(ssymbol);
            int asi = arrays.indexOf(asymbol);
            if (ssi == -1 && asi == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                scalars.get(ssi).value = num;
            } else { // array symbol
            	asymbol = arrays.get(asi);
            	asymbol.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    asymbol.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Performs matched operation.
     * 
     * (multiplication, division, addition, subtraction)
     * 
     * @return Evaluated answer based on operation (float)
     */
    private float opr(String num1, String num2, String operation) {
    	
    	// Initializing Variables
    	float number1 = 0;
    	float number2 = 0;
    	
    	if(operation.equalsIgnoreCase("*")){
    		
    		number1 = Float.parseFloat(num1);
    		number2 = Float.parseFloat(num2);
    		
    		float multAnswer = number1 * number2;
    		
    		return multAnswer; //multiplication -> answer
    		
    	} else if(operation.equalsIgnoreCase("/")){
    		
    		number1 = Float.parseFloat(num1);
    		number2 = Float.parseFloat(num2);
    		
    		float divAnswer = number1 / number2;
    		
        	return divAnswer; //division -> answer
        	
        } else if(operation.equalsIgnoreCase("+")){
        	
        	number1 = Float.parseFloat(num1);
    		number2 = Float.parseFloat(num2);
    		
    		float addAnswer = number1 + number2;
        	
        	return addAnswer; //addition -> answer
        	
        } else if(operation.equalsIgnoreCase("-")){
        	
        	number1 = Float.parseFloat(num1);
    		number2 = Float.parseFloat(num2);
    		
    		float subAnswer = number1 - number2;
    		
    		return subAnswer; //subtraction -> answer
    		
        }
    	
    	return 0; //returns zero if everything fails
    }
    
    /**
     * Searches array ArrayList for symbol.
     * 
     * @return True if symbol found; false otherwise (boolean)
     */
    private boolean arr(String symbol) {
    	
    	boolean arry = false;
    	
    	// Iterating through array ArrayList
    	for(int index = 0; index < arrays.size(); index++){

    		// Initializing Variables
    		String ArrayName = this.arrays.get(index).name;
    		
    		// Searching for target (symbol)
    		if(ArrayName.equals(symbol)){ //returns true if array name matches symbol
    			
    			arry = true;
    			
    			return arry; //symbol is found -> returns true
    		}
    	}
    	
    	return arry; //symbol is NOT found -> returns false
    }
    
    /**
     * Gets the array's variable value at position j.
     * 
     * @return Variable value at index of array position j (integer)
     */
    private int getArrayValue(String symbol, int j) {
    	
    	// Iterating through array ArrayList
    	for(int index = 0; index < arrays.size(); index++){
    		
    		// Initializing Variables
    		String ArrayName = this.arrays.get(index).name;
    		int ArrayValue = this.arrays.get(index).values[j];
    		
    		// Searching for target (symbol)
    		if(ArrayName.equals(symbol)){
    			
    			return ArrayValue; //certain array value
    		}
    	}
    	
    	return 0; //returns zero if everything fails
    }
    
    /**
     * Searches scalar ArrayList for symbol.
     * 
     * @return True if symbol found; false otherwise (boolean)
     */
    private boolean scalr(String symbol) {
    	
    	boolean scal = false;
    	
    	// Iterating through scalar ArrayList
    	for(int index = 0; index < scalars.size(); index++){
    		
    		// Initializing Variables
    		String ScalarName = this.scalars.get(index).name;
    		
    		// Searching for target (symbol)
    		if(ScalarName.equals(symbol)){
    			
    			scal = true;
    			
    			return scal; //symbol is found -> returns true
    		}
    	}
    	
    	return scal; //symbol is NOT found -> returns false
    }
    
    /**
     * Gets the scalar's variable value.
     * 
     * @return Variable value at index (integer)
     */
    private int getScalarValue(String symbol) {
    	
    	// Iterating through scalar ArrayList
    	for(int index = 0; index < scalars.size(); index++){
    		
    		// Initializing Variables
    		String ScalarName = this.scalars.get(index).name;
    		int ScalarValue = this.scalars.get(index).value;
    		
    		// Searching for target (symbol)
    		if(ScalarName.equals(symbol)){
    			
    			return ScalarValue; //certain scalar value
    		}
    	}
    	
    	return 0;
    }
    
    /**
     * Gets the scalar's variable name.
     * 
     * @return Variable name at index (String)
     */
    private String getScalarNameAtIndex(int index) {
    	
    	String ScalarName = this.scalars.get(index).name;
    	
    	return ScalarName;
    }
    
    /**
     * Gets the scalar's variable value.
     * 
     * @return Variable value at index (float)
     */
    private float getScalarValueAtIndex(int index) {
    	
    	float ScalarValue = this.scalars.get(index).value;
    	
    	return ScalarValue;
    }
    
    /**
     * Reverses items in the stack that was passed in.
     * 
     * @return New stack containing reversed items from the stack passed in (Stack)
     */
    private Stack<String> reverse(Stack<String> origStack) {
    	
    	// Initializing New Stack (reverse)
    	Stack<String> reverse = new Stack<String>();
    	
    	// Stack isn't empty
    	while(origStack.isEmpty() == false){
    		
    		// Reverses items from original stack into another stack
    		//   -> top items become bottom items
    		//   -> bottom items become top items
    		reverse.push(origStack.pop());
    		
    	}
    	
    	return reverse; //reversed stack (e.g. "1, 2, 3" -> "3, 2, 1")
    }

    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate() {
    	
    	// Initializing Variables
    	String expression = expr;
    	float evalExpression;
    	
    	// Removing spaces from the expression
    	expression = expression.replace(" ", "").trim();
    	
    	// StringTokenizer
    	StringTokenizer stkExpression = new StringTokenizer(expression, delims, true);
    	
    	// Calling the Recursive Evaluate method
    	evalExpression = recursEvaluate(stkExpression);
    	
    	return evalExpression; //evaluated expression (float)
    	
    }
    
    /**
     * Recursively evaluates the given expression utilizing stacks (works "inside-out").
     * 
     * @return Result of evaluation (float)
     */
    private float recursEvaluate(StringTokenizer stkExpression) {
    	
    	// Initializing Variables
    	String tkn = null;
    	float evalExpression = 0;
    	
// ------- Block 1 (Stack 1) ------- 
    	
    	// Initializing Stack 1 (stack1)
    	Stack<String> stack1 = new Stack<String>();
    	
    	// Iterating through the expression -> going through each token
    	while(stkExpression.hasMoreTokens()){
    		
    		// Token by token
    		tkn = stkExpression.nextToken();
    		//System.out.println("Current token: " + tkn);
    		
    		// Open Parentheses and/or Brackets -> evaluate and add value to stack
    		if(tkn != null && tkn.equals("") == false){
    		
    			// Operations inside parentheses and brackets - ( ) or [ ]
	    		if(tkn.equalsIgnoreCase("(") || tkn.equalsIgnoreCase("[")){
	    			
	    			// STACK AND RECURSION
	    			String parenOps = Float.toString(this.recursEvaluate(stkExpression));
	    			stack1.push(parenOps);
	    			
	    			// Prints value of operation (states whether parentheses or bracket)
	    			if(tkn.equalsIgnoreCase("(")){ //parenthesis
	    				//System.out.println("- - - - - - - - - -");
		    			//System.out.println("-> Value of operation inside parenthesis: " + parenOps);
		    			//System.out.println("- - - - - - - - - -");
	    			}
	    			if(tkn.equalsIgnoreCase("[")){ //bracket
	    				//System.out.println("- - - - - - - - - -");
		    			//System.out.println("-> Value of operation inside bracket: " + parenOps);
		    			//System.out.println("- - - - - - - - - -");
	    			}
	    			
	    		// Closing Parentheses and/or Brackets -> do nothing and break out of loop
	    		} else if(tkn.equalsIgnoreCase(")") || tkn.equalsIgnoreCase("]")){
	    			
	    			break; //exits the while loop
	    			
	    		// Token is NOT a bracket or parenthesis -> add to stack
	    		} else {
	    			
	    			stack1.push(tkn);
	    			//System.out.println("- - - - - - - - - -");
	    			//System.out.println("*Token -> no [ ] or ( ): " + tkn);
	    			//System.out.println("- - - - - - - - - -");
	    			
	    		}
	    		
    		} //end of if statement
    		
    	} //end of while loop
    	
// ------- Block 2 (Stack 2) ------- 
    	
    	// Initializing Stack 2 (stack2)
    	Stack<String> stack2 = new Stack<String>();
    	
    	// Reversing Stack 1 (stack1) -> Inside-Out = Top to bottom
    	stack1 = this.reverse(stack1);
    	
    	// Stack 1 -> Not Empty (while loop -> iterates through stack)
    	while(stack1.isEmpty() == false){
    		
    		// Initializing Empty Strings -> to be used in if statements
    		String poppedItemS1 = "";
    		poppedItemS1 = stack1.pop();
    		
    		//System.out.println("stack pop (block 2): " + poppedItemS1);
    		
    		// Scalar
    		if(this.scalr(poppedItemS1)){
    			
    			// Obtaining Scalar's Value -> pushing into stack2
    			int ScalarValue = this.getScalarValue(poppedItemS1); //obtains the scalar's value
    			String poppedScalarValue = Float.toString(ScalarValue); //converts to string
    			stack2.push(poppedScalarValue); //pushes scalar value into stack 2
    			
    		// Array	
    		} else if(this.arr(poppedItemS1)){
    			
    			// Obtaining Array's Value -> pushing into stack2
    			String j = stack1.pop();
    			int j2 = (int) Float.parseFloat(j);
    			int ArrayValue = this.getArrayValue(poppedItemS1, j2); //obtains the array's value
    			String poppedArrayValue = Float.toString(ArrayValue); //converts to string
    			stack2.push(poppedArrayValue); //pushes array value into stack 2
    			
    		// Operators and numbers w/o variables
    		} else {
    			
    			// Pushing popped item onto Stack 2 (stack2)
    			stack2.push(poppedItemS1);
    			//System.out.println("- popped s1 item: " + poppedItemS1);
    			
    		}
    		
    	} //end of while loop
    	
// ------- Block 3 (Stack 3) ------- 
    	
    	// Initializing Stack 3 (mdStack3) - Multiplication and Division
    	Stack<String> mdStack3 = new Stack<String>();
    	
    	// Reversing Stack 2 (stack2)
    	stack2 = this.reverse(stack2);
    	
    	// Stack 2 -> Not Empty (while loop -> iterates through stack)
    	while(stack2.isEmpty() == false){
    		
    		// Initializing Empty Strings -> to be used in if statements
    		String poppedItemS2 = "";
    		poppedItemS2 = stack2.pop();
    		
    		//System.out.println("stack pop (block 3): " + poppedItemS2);
    		
    		// Multiplication or Division
    		if(poppedItemS2.equalsIgnoreCase("*") || poppedItemS2.equalsIgnoreCase("/")){
    			
    			// Initializing Variables for Operations
    			String num1 = mdStack3.pop(); //number 1
    			String num2 = stack2.pop(); //number 2
    			String mdOperator = poppedItemS2; //operator
    			
    			// Performing Operations (* and /) -> passes to operation method
    			// 	-> num1*num2 = mdAnswer
    			//	-> num1/num2 = mdAnswer
    			//System.out.println("Operation: " + num1 + mdOperator + num2);
    			String mdAnswer = Float.toString(this.opr(num1, num2, mdOperator));
    			mdStack3.push(mdAnswer); //pushing evaluated answer
    			
    			//System.out.println("- - - - - - - -");
    		
    		// Otherwise -> add s2 item onto stack
    		} else {
    			
    			// Pushing popped item onto Stack 3 (mdStack3)
    			mdStack3.push(poppedItemS2);
    			//System.out.println("- popped s2 item: " + poppedItemS2);
    			
    		}
    		
    	} //end of while loop
    	
// ------- Block 4 (Stack 4) ------- 
    	
    	// Initializing Stack 4 (asStack4) - Addition and Subtraction
    	Stack<String> asStack4 = new Stack<String>();
    	
    	// Reversing Stack 3 (mdStack3)
    	mdStack3 = this.reverse(mdStack3);
    	
    	// Stack 3 -> Not Empty (while loop -> iterates through stack)
    	while(mdStack3.isEmpty() == false){
    		
    		// Initializing Empty Strings -> to be used in if statements
    		String poppedItemS3 = "";
    		poppedItemS3 = mdStack3.pop();
    		
    		//System.out.println("stack pop (block 4): " + poppedItemS3);
    		
    		// Addition or Subtraction
    		if(poppedItemS3.equalsIgnoreCase("+") || poppedItemS3.equalsIgnoreCase("-")){
    			
    			// Initializing variables for operations
    			String num1 = asStack4.pop();
    			String num2 = mdStack3.pop();
    			String asOperator = poppedItemS3;
    			
    			// Performing Operations (+ and -) -> passes to operation method
    			// 	-> num1+num2 = asAnswer
    			//	-> num1-num2 = asAnswer
    			//System.out.println("Operation: " + num1 + asOperator + num2);
    			String asAnswer = Float.toString(this.opr(num1, num2, asOperator));
    			asStack4.push(asAnswer); //pushing evaluated answer
    			
    			//System.out.println("- - - - - - - -");
    		
    		// Otherwise -> add s3 item onto stack
    		} else {
    			
    			// Pushing popped item onto Stack 4 (asStack4)
    			asStack4.push(poppedItemS3);
    			//System.out.println("- popped s3 item: " + poppedItemS3);
    			
    		}
    		
    	} //end of while loop
    	
// ------- Returning Answer (float) ------- 
    	
    	// Parsing 
    	evalExpression = Float.parseFloat(asStack4.pop());
    	
    	return evalExpression; //evaluated expression (float) -> end of recursive method
    }

    /**
     * Utility method, prints the symbols in the scalars list
     */
    public void printScalars() {
        for (ScalarSymbol ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the symbols in the arrays list
     */
    public void printArrays() {
    	for (ArraySymbol as: arrays) {
    		System.out.println(as);
    	}
    }

}
