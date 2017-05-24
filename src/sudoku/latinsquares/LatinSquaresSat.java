package sudoku.latinsquares;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.sat.Clause;
import sudoku.sat.PosLiteral;
import sudoku.sat.SatFormula;
import sudoku.set.Set;

public class LatinSquaresSat extends LatinSquares {

	public static void main(String[] args) {
		
		// The speedup due to formula reduction
		// and unit propagation is amazing:
		//
		// Before, 2x2 didn't terminate.
		//
		// Now, 8x8 without a partial solution is solved in ~2mins:
		//
		// 67543210
		// 76452301
		// 45761032
		// 54670123
		// 23107654
		// 32016745
		// 01325476
		// 10234567
		
		LatinSquaresSat l = new LatinSquaresSat(9);
		SatFormula sat = l.getFormula();
		Env env = sat.solve();
		System.out.println(l.interpretResult(env));
	}

	public LatinSquaresSat(int dim) {
		super(dim);
	}

	public SatFormula getFormula() {
		
		SatFormula sat = new SatFormula();
		
		for (int n = 0; n != getDim(); n++) {
			
			sat = getAtLeastFormula(sat, n, true);
			sat = getAtLeastFormula(sat, n, false);
			sat = getAtMostFormula(sat, n, true);
			sat = getAtMostFormula(sat, n, false);
			
		}
		
		sat = getCellAtMostFormula(sat);
		
		return sat;
		
	}

	SatFormula getAtLeastFormula(SatFormula sat, int n, boolean isCol) {
		
		for (int i = 0; i != getDim(); i++) {
			
			Var var;
			
			// one clause per column (or row)
			sat = sat.clause();
			
			for (int j = 0; j != getDim(); j++) {
				
				var = (Var) makeCellVar(i,j,n,isCol);
				
				nterminals++;
				sat = sat.pos(var.getName());
				
			}
						
		}
		
		return sat;
		
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
	SatFormula getAtMostFormula(SatFormula sat, int n, boolean isCol) {
	
		Clause zClause;		// z clause for column or row constraint
		Var zCellVar;	// a cell's z Var for column or row constraint
		Var cellVar;		// a cell's Var for a given n
				
		for (int i = 0; i != getDim(); i++) {
			// column or row

			zClause = new Clause();

			for (int j = 0; j != getDim(); j++) {
				// cell

				zCellVar = (Var) makeZVar(i, j, n, isCol);

				nterminals++;
				zClause = zClause.add( new PosLiteral(zCellVar.getName()) );

				for (int k = 0; k != getDim(); k++) {
					// other cells (with respect to cell)

					cellVar = (Var) makeCellVar(i, k, n, isCol);

					sat = sat.clause().neg(zCellVar.getName());

					if (k == j) {
						sat = sat.pos(cellVar.getName());
					} else {
						sat = sat.neg(cellVar.getName());
					}

					nterminals++;

				}

			}

			sat = sat.add(zClause);					

		}
		
		return sat;
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
	SatFormula getCellAtMostFormula(SatFormula sat) {
		
		Clause zClause;
		Var zVar;
		Var nVar;
		
		for (int i = 0; i != getDim(); i++) {
			for (int j = 0; j != getDim(); j++) {

				zClause = new Clause();
				
				for (int n = 0; n != getDim(); n++) {
					
					zVar = (Var) makeZCellVar(i,j,n);
					
					nterminals++;
					zClause = zClause.add(new PosLiteral(zVar.getName()));
					
					for (int nn = 0; nn != getDim(); nn++) {

						nVar = (Var) makeCellVar(i,j,nn,true);
						
						nterminals++;
						sat = sat.clause().neg(zVar.getName());
						
						nterminals++;
						if (n == nn) {
							sat = sat.pos(nVar.getName());
						} else {
							sat = sat.neg(nVar.getName());
						}

					}
										
				}
				
				sat = sat.add(zClause);					
				
			}
		}
		
		return sat;
		
	}

	/**
	 * Creates Env from string. No string validation is performed.
	 * 
	 * @param string A partial solution in format "1./n.1"
	 * @return
	 */
	public Env envFromSolution(String string) {
		
		Env env = new Env();
		
		BufferedReader reader = new BufferedReader(new StringReader(string));
		String line;
		String varName;
		int row = 0;
		int n;
		Bool b;
		
		try {
			while ((line = reader.readLine()) != null) {
				for (int i = 0; i != line.length(); i++) {
					if (line.charAt(i) == '.') {
						continue;
					}
					
					// partial solution numbers start at 1
					// SatFormula numbers start at 0
					n = Integer.parseInt(line.substring(i,i+1)) - 1;
					
					// this is not very efficent, just need to iterate
					// over each other cell in same col, and then in 
					// same row, but it doesn't really matter in the big pic 
					// and its simple
					for (int j = 0; j != line.length(); j++) {
						
						varName = "ZC" + i + row + j;

						if (j == n) {
							b = Bool.TRUE;
						} else {
							b = Bool.FALSE;
						}

						env = env.put(Var.makeVar(varName), b);							
						
						for (int k = 0; k != line.length(); k++) {
							
							if (j != i && k != row) {
								continue;
							}
							
							if (j == i ) {

								varName = "Zc" + j + k + n;
								
								if (k == row) {
									b = Bool.TRUE;
								} else {
									b = Bool.FALSE;
								}

								env = env.put(Var.makeVar(varName), b);							
								
							}
							
							if (k == row ) {

								varName = "Zr" + j + k + n;
								
								if (j == i) {
									b = Bool.TRUE;
								} else {
									b = Bool.FALSE;
								}

								env = env.put(Var.makeVar(varName), b);							
								
							}
							
							varName = "C" + j + k + n;
							
							if (j == i && k == row) {
								b = Bool.TRUE;
							} else {
								b = Bool.FALSE;
							}
							
							env = env.put(Var.makeVar(varName), b);
							
						}						
					}
										
				}
				
				row++;
				
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return env;
	}

	public Env solve(Env partial) {
		// call vars() and remove all variables that are in partial
		//   to accomplish this, make Set and Env iterable
		
		SatFormula sat = getFormula();
		
		Set<Var> remainingVars = sat.vars();
		for (Var v : partial) {
			remainingVars = remainingVars.remove(v);
		}
		return sat.solve(partial, remainingVars);
	}	
	
}
