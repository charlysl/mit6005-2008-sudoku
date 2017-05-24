package sudoku.latinsquares;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Var;

public class LatinSquaresFormula extends LatinSquares {

	public static void main(String[] args) {
		LatinSquaresFormula l = new LatinSquaresFormula(8);
		Formula f = l.getFormula();
		
		System.out.println("nvars: " + f.vars().size());
		System.out.println("nterminals:" + l.nterminals);
		
		Env partial = new Env();
		
		// test full solution
		//2x2
//		Var C000 = Var.makeVar("C000");
//		Var C011 = Var.makeVar("C011");
//		Var C101 = Var.makeVar("C101");
//		Var C110 = Var.makeVar("C110");
//		partial = partial.put(C000, Bool.TRUE);
//		partial = partial.put(C011, Bool.TRUE);
//		partial = partial.put(C101, Bool.TRUE);
//		partial = partial.put(C110, Bool.TRUE);
		//3x3
		//012
		//120
		//201
		Var[] vars = {
				Var.makeVar("C000"),
				Var.makeVar("C011"),
				Var.makeVar("C022"),
				Var.makeVar("C101"),
				Var.makeVar("C112"),
				Var.makeVar("C120"),
				Var.makeVar("C202"),
				Var.makeVar("C210"),
				Var.makeVar("C221"),
		};
		for (Var v : vars) {
			partial = partial.put(v, Bool.TRUE);

		}
				
//		Env env = f.solve(partial);
//		System.out.println(l.interpretResult(env));
	}
		
	public LatinSquaresFormula(int dim) {
		super(dim);
	}

	public Formula getFormula() {
		
		Formula formula = null;
		
		for (int n = 0; n != getDim(); n++) {
			
			formula = getAtLeastFormula(formula, n);
			formula = getAtMostFormula(formula, n, true);
			formula = getAtMostFormula(formula, n, false);
			
		}
		
		formula = getCellAtMostFormula(formula);
		
		return formula;
		
	}

	public Formula getAtLeastFormula(Formula formula, int n) {
		
		for (int i = 0; i != getDim(); i++) {
			
			Formula colOr = null;
			Formula rowOr = null;
			Formula colVar;
			Formula rowVar;
			
			for (int j = 0; j != getDim(); j++) {
				
				colVar = makeCellVar(i,j,n,true);
				rowVar = makeCellVar(i,j,n,false);
				
				if (colOr == null) {
					nterminals++;
					colOr = colVar;
					nterminals++;
					rowOr = rowVar;
				} else {
					nterminals++;
					colOr = colOr.or(colVar);
					nterminals++;
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
	public Formula getAtMostFormula(Formula formula, int n, boolean isCol) {
	
		Formula zOr;			// z Or for column or roe constraint
		Formula zCellVar;	// a cell's z Var for column or row constraint
		Formula zCellNotVar;	// a cell's z !Var for column or row constraint
		Formula cellVar;		// a cell's Var for a given n
				
		for (int i = 0; i != getDim(); i++) {
			// column or row

			zOr = null;

			for (int j = 0; j != getDim(); j++) {
				// cell

				zCellVar = makeZVar(i, j, n, isCol);

				if (zOr == null) {
					nterminals++;
					zOr = zCellVar;
				} else {
					nterminals++;
					zOr = zOr.or(zCellVar);
				}

				zCellNotVar = zCellVar.not();

				for (int k = 0; k != getDim(); k++) {
					// other cells (with respect to cell)

					cellVar = makeCellVar(i, k, n, isCol);

					if (k != j) {
						cellVar = cellVar.not();
					}

					if (formula == null) {
						// TODO this check is temporary, just for testing
						nterminals++;
						formula = zCellNotVar.or(cellVar);
					} else {
						nterminals++;
						formula = formula.and(zCellNotVar.or(cellVar));
					}

				}

			}

			formula = formula.and(zOr);					

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
		
		for (int i = 0; i != getDim(); i++) {
			for (int j = 0; j != getDim(); j++) {
				
				zOr = null;
				
				for (int n = 0; n != getDim(); n++) {
					
					zVar = makeZCellVar(i,j,n);
					zVarNot = zVar.not();
					
					if (zOr == null) {
						nterminals++;
						zOr = zVar;
					} else {
						nterminals++;
						zOr = zOr.or(zVar);
					}
					
					for (int nn = 0; nn != getDim(); nn++) {

						nVar = makeCellVar(i,j,nn,true);
						
						if (n != nn) {
							nVar = nVar.not();
						}

						if (formula == null) {
							// TODO temporary, for dev
							nterminals++;
							formula = zVarNot.or(nVar);
						} else {
							nterminals++;
							formula = formula.and( zVarNot.or(nVar) );
						}
						
					}
										
				}
				
				formula = formula.and(zOr);
				
			}
		}
		
		return formula;
		
	}
	
}

