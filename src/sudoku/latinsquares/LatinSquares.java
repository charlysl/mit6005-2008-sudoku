package sudoku.latinsquares;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Var;

public class LatinSquares {

	public static void main(String[] args) {
		LatinSquares l = new LatinSquares(2);
		
		Formula f = l.getFormula();
		
//		System.out.println(f);
//		System.out.println(f.vars());
		Env env = f.solve();
		System.out.println(l.interpretResult(env));
	}
	
	private int dim;
	
	public LatinSquares(int dim) {
		this.dim = dim;
	}

	public Env solve() {
		return null;
	}
	
	public Formula getFormula() {
		
		Formula formula = null;
		
		for (int n = 0; n != dim; n++) {
			
			formula = getAtLeastFormula(formula, n);
			formula = getAtMostFormula(formula, n, true);
			formula = getAtMostFormula(formula, n, false);
			
		}
		
		formula = getCellAtMostFormula(formula);
		
		return formula;
		
	}

	public Formula getAtLeastFormula(Formula formula, int n) {
		
		for (int i = 0; i != dim; i++) {
			
			Formula colOr = null;
			Formula rowOr = null;
			String colVarName;
			String rowVarName;
			Formula colVar;
			Formula rowVar;
			
			for (int j = 0; j != dim; j++) {
				
				colVarName = "C" + i + j + n;
				rowVarName = "C" + j + i + n;
				
				colVar = Formula.var(colVarName);
				rowVar = Formula.var(rowVarName);
				
				if (colOr == null) {
					colOr = colVar;
					rowOr = rowVar;
				} else {
					colOr = colOr.or(colVar);
					rowOr = rowOr.or(rowVar);
				}
				
			}
			
			if ( formula == null) {
				formula = colOr.and(rowOr); 
			} else {
				formula = formula.and(colOr).and(rowOr);
			}
			
		}
		
		return formula;
		
	}

	public Formula getAtMostFormula(Formula formula, int n, boolean isCol) {

		/**
		 * This method adds to the formula the constraint that each number (n)
		 * must be unique for each column and for each row.
		 * 
		 * For, say, the first column of a 2x2 latin square, with n=0,
		 * this constraint is more intuitively expressed like this:
		 * 
		 *   		(C000 ^ !C010) v (!C000 ^ C010)
		 * 
		 * where Cxyn is a Var that represents whether the cell
		 * at column x and row y has the value n.
		 * 
		 * A similar constraint has to be added for every
		 * cell's column and row, and all this for every n.
		 * 
		 * However, the constraint above is not in CNF form.
		 * There is a way to translate it to CNF, which is described
		 * in {@link https://en.wikipedia.org/wiki/Conjunctive_normal_form here}.
		 * 
		 * Applying the transformation as described in the article, 
		 * the above formula in CNF would be:
		 * 
		 * (Zc000 v Zc010) ^ (!Zc000 ^  C000) ^ (!Zc000 ^ !C010)
		 *                 ^ (!Zc010 ^ !C000) ^ (!Zc010 ^  C010)
		 *                 
		 * where Zcxyn is the auxiliary variable for the column (x) constraint
		 * that on that particular column only cell xy can have value n.
		 * 
		 * The auxiliary variable's prefix for row would be Zr.
		 * 
		 */
	
		Formula zColOr;			// z Or for column constraint
		Formula zCellColVar;		// a cell's z Var for column constraint
		Formula zCellColNotVar;	// a cell's z !Var for column constraint
		Formula cellVar;			// a cell's Var for a given n
				
		for (int i = 0; i != dim; i++) {
			// column or row

			zColOr = null;

			for (int j = 0; j != dim; j++) {
				// cell

				zCellColVar = makeZVar(i, j, n, isCol);

				if (zColOr == null) {
					zColOr = zCellColVar;
				} else {
					zColOr = zColOr.or(zCellColVar);
				}

				zCellColNotVar = zCellColVar.not();

				for (int k = 0; k != dim; k++) {
					// other cells (with respect to cell)

					cellVar = makeCellVar(i, k, n, isCol);

					if (k != j) {
						cellVar = cellVar.not();
					}

					if (formula == null) {
						// TODO this check is temporary, just for testing
						formula = zCellColNotVar.or(cellVar);
					} else {
						formula = formula.and(zCellColNotVar.or(cellVar));
					}

				}

			}

			formula = formula.and(zColOr);					

		}
		
		
		return formula;
	}

	/**
	 * There is the additional constraint that a cell can have only
	 * one number in the final solution; this is, the Var Cxyn, for a given
	 * x and y, can be true for only one n.
	 * 
	 * For cell at column 0 and row 0, for instance, to express
	 * that the cell must be either 0 or 1:
	 * 
	 * 			(C000 ^ !C001) v (!C000 ^ C001)
	 * 
	 * In CNF form it would be:
	 * 
	 * 			(ZC000 v ZC001) 	^ (!ZC000 v  ZC000) ^ (!ZC000 v !ZC001)
	 *  							^ (!ZC001 v !ZC000) ^ (!ZC001 v  ZC001)
	 * 
	 * ZCxyn is the auxiliary variable for each cell and number.
	 * 
	 * This constraint must be added to the formula for each cell and number.
	 * 
	 * @param formula
	 * @return formula
	 */
	private Formula getCellAtMostFormula(Formula formula) {
		
		Formula zOr;
		Formula zVar;
		Formula zVarNot;
		Formula nVar;
		
		for (int i = 0; i != dim; i++) {
			for (int j = 0; j != dim; j++) {
				
				zOr = null;
				
				for (int n = 0; n != dim; n++) {
					
					zVar = makeZCellVar(i,j,n);
					zVarNot = zVar.not();
					
					if (zOr == null) {
						zOr = zVar;
					} else {
						zOr = zOr.or(zVar);
					}
					
					for (int nn = 0; nn != dim; nn++) {

						nVar = makeCellVar(i,j,nn,true);
						
						if (n != nn) {
							nVar = nVar.not();
						}

						if (formula == null) {
							// TODO temporary, for dev
							formula = zVarNot.or(nVar);
						} else {
							formula = formula.and( zVarNot.or(nVar) );
						}
						
					}
										
				}
				
				formula = formula.and(zOr);
				
			}
		}
		
		return formula;
		
	}

	private Formula makeCellVar(int col, int row, int n, boolean isCol) {
		return makeVar("C",col,row,n,isCol);
	}

	private Formula makeZVar(int col, int row, int n, boolean isCol) {
		return makeVar("Z" + (isCol ? "c" : "r"),col,row,n,isCol);
	}

	private Formula makeZCellVar(int col, int row, int n) {
		return makeVar("ZC",col,row,n,true);
	}
	
	private Formula makeVar(String prefix, int col, int row, int n, boolean isCol) {
		if (isCol) {
			return Formula.var(prefix + col + row + n);
		} else {
			return Formula.var(prefix + row + col + n);			
		}
	}
	
	public String interpretResult(Env env) {
		
		StringBuilder buf = new StringBuilder();
		
		for (int i = 0; i != dim; i++) {
			// row
			for (int j = 0; j != dim; j++) {
				// column
				for (int n = 0; n != dim; n++) {
					// number
					Var var = (Var) makeCellVar(i, j, n, false);
					if (env.get(var).equals(Bool.TRUE)) {
						System.out.println(var);
						buf.append(n);
					}
				}
			}
			buf.append('\n');
		}
		
		return buf.toString();
		
	}
	
}

