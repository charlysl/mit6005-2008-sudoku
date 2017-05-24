package sudoku.latinsquares;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Var;

public abstract class LatinSquares {

	private int dim;
	
	public LatinSquares(int dim) {
		this.dim = dim;
	}
	
	protected final int getDim() {
		return dim;
	}
	
	protected final Formula makeCellVar(int col, int row, int n, boolean isCol) {
		return makeVar("C",col,row,n,isCol);
	}

	protected final  Formula makeZVar(int col, int row, int n, boolean isCol) {
		return makeVar("Z" + (isCol ? "c" : "r"),col,row,n,isCol);
	}

	protected final  Formula makeZCellVar(int col, int row, int n) {
		return makeVar("ZC",col,row,n,true);
	}
	
	public long nterminals;
	protected final  Formula makeVar(String prefix, int col, int row, int n, boolean isCol) {
		if (isCol) {
			return Formula.var(prefix + col + row + n);
		} else {
			return Formula.var(prefix + row + col + n);			
		}
	}
	
	public final String interpretResult(Env env) {
		
		StringBuilder buf = new StringBuilder();
		
		for (int i = 0; i != getDim(); i++) {
			// row
			for (int j = 0; j != getDim(); j++) {
				// column
				for (int n = 0; n != getDim(); n++) {
					// number
					Var var = (Var) makeCellVar(i, j, n, false);
					if (env.get(var).equals(Bool.TRUE)) {
//						System.out.println(var);
						buf.append(n+1);
					}
				}
			}
			buf.append('\n');
		}
		
		return buf.toString();
		
	}
	
}
