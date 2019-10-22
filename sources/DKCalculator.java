import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class DKCalculator extends JFrame {

	private JPanel contentPane;
	private Stack<String> ops;
	private Stack<Double> data;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DKCalculator frame = new DKCalculator();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DKCalculator() {
		setResizable(false);
		setTitle("SmartCalculator by Daewon Kwon");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 429, 436);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		JTextArea expressionJ = new JTextArea();
		expressionJ.setFont(new Font("Tahoma", Font.PLAIN, 16));
        //statusJ.setFont(new Font("Serif", Font.ITALIC, 16));
		expressionJ.setLineWrap(true);
		expressionJ.setWrapStyleWord(true);

        JScrollPane expressionScrollJ = new JScrollPane(expressionJ);
        expressionScrollJ.setBounds(10, 10, 400,70);
        expressionScrollJ.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        expressionScrollJ.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        expressionScrollJ.setPreferredSize(new Dimension(430, 150));
        contentPane.add(expressionScrollJ);

        JTextArea resultJ = new JTextArea("Temp");
		resultJ.setFont(new Font("Tahoma", Font.PLAIN, 12));
        //statusJ.setFont(new Font("Serif", Font.ITALIC, 16));
		resultJ.setLineWrap(true);
		resultJ.setWrapStyleWord(true);

        JScrollPane resultScrollJ = new JScrollPane(resultJ);
        resultScrollJ.setBounds(10, 195, 400,200);
        resultScrollJ.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resultScrollJ.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultScrollJ.setPreferredSize(new Dimension(430, 150));
        contentPane.add(resultScrollJ);

        resultJ.setText(resetMessage());

        JButton btnCalculate = new JButton("Calculate");
        btnCalculate.setForeground(new Color(0, 100, 0));
        btnCalculate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try{
        			String retStr = expressionJ.getText()
        					+ "\n\n= " + evaluateExpression(expressionJ.getText());
            		resultJ.setText(retStr);
        		}catch(Exception ee){
        			resultJ.setText("Evaluation failed: " + ee.getMessage());
        		}

        		expressionJ.setText("");
        	}
        });
        btnCalculate.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCalculate.setBounds(10, 99, 298, 37);
        contentPane.add(btnCalculate);

        JButton btnNewButton = new JButton("Simple Stat");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		resultJ.setText(simpleStat(expressionJ.getText() + " "));
        		expressionJ.setText("");
        	}
        });
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnNewButton.setBounds(166, 147, 142, 37);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Reset");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		expressionJ.setText("");
        		resultJ.setText(resetMessage());
        	}
        });
        btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnNewButton_1.setBounds(318, 99, 92, 85);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Function Plot");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try{
        			functionPlot(expressionJ.getText());
            		resultJ.setText(expressionJ.getText());
            		expressionJ.setText("");
        		}catch(Exception ee){
        			resultJ.setText(ee.getMessage());
        		}
        	}
        });
        btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        btnNewButton_2.setBounds(10, 147, 142, 37);
        contentPane.add(btnNewButton_2);
	}

	private void functionPlot(String expression) throws Exception{
		expression = reshapeExpression(expression);

		String range = expression.substring(expression.indexOf('[')+1,expression.indexOf(']'));
		expression = expression.substring(0,expression.indexOf('['));
		StringTokenizer st = new StringTokenizer(range,",");
		double min = evaluateExpression(st.nextToken());
		double max = evaluateExpression(st.nextToken());
		int ndata = 1001;
		double dx = (max - min) / (ndata - 1);

		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();

		Double xx,yy;
		String expressionX;
		for(int i = 0; i < ndata; i++){
			expressionX = "";
			xx = min + dx * i;

			for(int j = 0; j < expression.length();j++){
				if(expression.charAt(j) != 'X') expressionX += expression.charAt(j);
				else expressionX += ("(" + xx.toString() + ")");
			}

			try{
				yy = evaluateExpression(expressionX);
				if(!yy.toString().equals("Infinity") && !yy.toString().equals("-Infinity")
						 && !yy.toString().equals("NaN")){
					x.add(xx);
					y.add(yy);
				}
			}catch(Exception ee){
			}
		}

		new DataPlotSingle((new double[][]{AL_Arr(x),AL_Arr(y)}),expression,null,null,null,null);
	}

	private double [] AL_Arr(ArrayList<Double> al){
		double [] arr = new double[al.size()];
		for(int i = 0;i<al.size();i++){
			arr[i] = al.get(i);
		}
		return arr;
	}

	private String simpleStat(String expression) {
		double value;
		double [] statresult = null;
		String tmpstr = "";

		StringTokenizer st = new StringTokenizer(expression," ,\n\t");
		while(st.hasMoreTokens()){
			try{
				tmpstr = st.nextToken();
				value = Double.parseDouble(tmpstr);
				statresult = Function.stat(value,statresult);
			}catch(NumberFormatException ee){
				tmpstr = "Data: \n" + expression + "\n\nError with this data: "+tmpstr;
				return tmpstr;
			}
		}

		tmpstr = "Average = " + statresult[0] + "\n";
		tmpstr += "STD = " + statresult[1] + "\n";
		tmpstr += "Min = " + statresult[2] + "\n";
		tmpstr += "Max = " + statresult[3] + "\n";
		tmpstr += "Number of data = " + (int)(statresult[4]) + "\n";
		return ("Data:\n" + expression + "\n\nSimple Stat Result: \n" + tmpstr);
	}

	private String reshapeExpression(String expression){
		expression = expression.toUpperCase();
		expression = expression.replaceAll("EXP","E");
		expression = expression.replaceAll(" ","");
		expression = expression.replaceAll("\n","");
		expression = expression.replaceAll("\t","");
		expression = expression.replaceAll(":",",");
		expression = expression.replaceAll(";",",");
		return expression;
	}

	private double evaluateExpression(String expression) throws Exception{

		expression = reshapeExpression(expression);

		ops = new Stack<String>();
		data = new Stack<Double>();

		char oneChar;
		int pos1 = 0,pos2,pos3 = 0,exprlength = expression.length();
		boolean sign = true;

		while( pos3 < exprlength ){
			pos3++;
			pos2 = pos3 - 1;
			oneChar = expression.charAt(pos2);
			int nvalues = 0;

			if(oneChar == '('){
				if(pos2 > pos1){
    				String op = expression.substring(pos1,pos2);
    				if(Function.contains(op)) ops.push(op);
    				else {
    					throw new Exception("Wrong operator or function name entered: " + op);
    				}
				}
				ops.push(oneChar+"");
				sign = true;
				pos1 = pos3;
			}
			else if (oneChar == '-' && sign){
				data.push(-1.0);
				ops.push("*");
				pos1 = pos3;
				sign = false;
			}
			else{
				sign = false;

    			if(oneChar == '+' || oneChar == '-'){
    				try{
    					data.push(Function.toDouble(expression.substring(pos1,pos2)));
    				}catch(NumberFormatException ee){ }

    				for(int i=0;i<2;i++){
	    				if(ops.isEmpty()) break;
	    				else if(ops.peek().charAt(0) == '+' || ops.peek().charAt(0) == '-'
								|| ops.peek().charAt(0) == '*' ||ops.peek().charAt(0) == '/'){
							performOperation();
						}
						else if(ops.peek().charAt(0) == '^'){
							performOperation();
							if(!ops.isEmpty()){
		    					if(ops.peek().charAt(0) == '*' || ops.peek().charAt(0) == '/'){
		    						performOperation();
		    					}
							}
						}
    				}

    				ops.push(oneChar+"");
    				pos1 = pos3;
    			}
    			else if (oneChar == '*' || oneChar == '/'){
    				try{
    					data.push(Function.toDouble(expression.substring(pos1,pos2)));
    				}catch(NumberFormatException ee){ }

    				if(!ops.isEmpty()){
	    				if(ops.peek().charAt(0) == '*' || ops.peek().charAt(0) == '/'){
							performOperation();
	    				}
						else if(ops.peek().charAt(0) == '^'){
							performOperation();
							if(!ops.isEmpty()){
		    					if(ops.peek().charAt(0) == '*' ||ops.peek().charAt(0) == '/'){
		    						performOperation();
		        				}
							}
						}
    				}

    				ops.push(oneChar+"");
    				pos1 = pos3;
    			}
    			else if (oneChar == '^'){
    				try{
    					data.push(Function.toDouble(expression.substring(pos1,pos2)));
    				}catch(NumberFormatException ee){ }
    				sign = true;
    				ops.push(oneChar+"");
    				pos1 = pos3;
    			}
    			else if (oneChar == ','){
    				try{
    					data.push(Function.toDouble(expression.substring(pos1,pos2)));
    				}catch(NumberFormatException ee){ }

    				while((ops.peek().charAt(0) != '(') && (ops.peek().charAt(0) != ',')){
						performOperation();
					}

    				sign = true;
    				ops.push(",");
    				pos1 = pos3;
    			}
    			else if (oneChar == '!'){
    				try{
    					data.push(Function.factorial(Function.toDouble(expression.substring(pos1,pos2))));
    				}catch(NumberFormatException ee){
    					data.push(Function.factorial(data.pop()));
    				}

    				pos1 = pos3;
    			}
    			else if(oneChar == ')'){
    				if(pos2 > pos1){
        				data.push(Function.toDouble(expression.substring(pos1,pos2)));
    				}

					while(ops.peek().charAt(0) != '('){
						if(performOperation()) nvalues++;
					}

					ops.pop(); // remove "("

					if(!ops.isEmpty()){
    					if(Function.contains(ops.peek())) evaluateFunction(nvalues+1);
					}

    				pos1 = pos3;
    			}

    			if(pos3 == exprlength){
    				if((pos3) > pos1){
    					data.push(Function.toDouble(expression.substring(pos1,pos3)));
    				}
					while(!ops.isEmpty()) performOperation();
					break;
    			}
			}
		}

		return data.pop();
	}

	private String resetMessage(){
    	return ("\nFunction plot expression: f(x) [xmin,xmax] --> press \"Function Plot\"\n"
    			+ "Simple Stat Data Separators: comma, space, new line, tab\n\n"
    			+ "------ Basic Functions -----------\n"
    			+ "^ - power     ! - factorial     pi\texp(x)\tsqrt(x)\n"
    			+ "cos(rad)\tsin(rad)\ttan(rad)\trad(deg): deg --> rad\n"
    			+ "acos(x)\tasin(x)\tatan(x)\tdeg(rad): rad --> deg\n"
    			+ "ln(x): natural         log(x): base 10\tlg(x): base 2\n\n"
				+ "pol(x:a0,a1, ... ,an): Polynomial function of order n\n"
    			+ "pol(x:3,0.5,2.1) is the same expression as (3+0.5*x+2.1*x^2)"
    			);
	}

	private void evaluateFunction(int ndata) throws Exception{
		if(Function.contains(ops.peek())){
			String op = ops.pop();

			double [] x = new double[ndata];
			for(int i = (ndata-1);i >= 0;i--){
				x[i] = data.pop();
			}
			data.push(Function.evaluate(op,x));
		}
	}

	private boolean performOperation() throws Exception{
		// returns true for comma, false for the rest
		try{
			char op = ops.pop().charAt(0);
			if (op == ',') return true;
			double val2 = data.pop(); // do not change the order val2 must come first before val1
			double val1 = data.pop();
			double result = 0.0;
			switch (op){
				case '+': result = val1 + val2; break;
				case '-': result = val1 - val2; break;
				case '*': result = val1 * val2; break;
				case '/': result = val1 / val2; break;
				case '^': result = Math.pow(val1,val2); break;
				default: throw new Exception(op + " is not one of basic operator\n");
			}
			data.push(result);
		}catch(Exception ee){
			throw new Exception("Exit from performOperation" + ee.getMessage());
		}
		return false;
	}
}

class Stack<Item>{
    private Node<Item> first;     // top of stack
    private int n;                // size of the stack

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    /**
     * Initializes an empty stack.
     */
    public Stack() {
        first = null;
        n = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void push(Item item) {
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        n++;
    }

    public Item pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item;        // save item to return
        first = first.next;            // delete first node
        n--;
        return item;                   // return the saved item
    }

    public Item peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return first.item;
    }
}