package sudoku;

import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.latinsquares.LatinSquaresSat;
import sudoku.sat.Clause;
import sudoku.sat.PosLiteral;
import sudoku.sat.SatFormula;

public class Sudoku extends LatinSquaresSat {

	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku(3);
		Env env = sudoku.envFromSolution(
				"79....3..\n" +
				".....69..\n" +
				"8...3..76\n" +
				".....5..2\n" +
				"..54187..\n" +
				"4..7.....\n" +
				"61..9...8\n" +
				"..23.....\n" +
				"..9....54\n"
		);
		env = sudoku.solve(env);
		System.out.println(sudoku.interpretResult(env));		
	}
	
	private int size;
	
	public Sudoku(int size) {
		super(size*size);
		this.size = size;
	}

	private int getSize() {
		return size;
	}
	
	@Override
	public SatFormula getFormula() {
		SatFormula sat = super.getFormula();
		
		for (int n = 0; n != getDim(); n++) {
		
			sat = getSquaresAtMostFormula(sat,n);
			
		}			
			
		return sat;
	}

	/**
	 * A sudoku is divided in size*size squares.
	 * 
	 * Within each of these squares each number must be unique.
	 * 
	 * Let's use the following variables naming convention, based
	 * on a size 3 (9x9) sudoku:
	 * 
	 * CS885 the bottom right corner cell (CSscrn):
	 *  left square, and increases left to right and top to bottom.
	 *  The first 2 (c) is the column. 
	 *  The second 2 (r) is the row.
	 *  5 means that this is the variable for n=5 for this cell 
	 * 
	 * ZS441 then represents the auxiliary variable for the middle cell
	 *  for n=4.
	 *  
	 *  The constraint that a given n can appear only once per square is,
	 *  for the top left square with n=0 in a 4x4 sudoku:
	 *  
	 *  (C000^!C010^!C100^!C110) || (!C000^C010^!C100^!C110) ...
	 *  
	 *  which, introducing auxiliary variables, would be in CNF:
	 *  
	 *  (ZS000^ZS010^ZS100^ZS110) || (!ZS000^ZS000) || (!ZS000^!ZS010) ...
	 *  
	 *  The above constraint already includes the constraint that a given
	 *  n must appear at least once per square. 
	 * 
	 * @param sat
	 * @return new sat
	 */
	 SatFormula getSquaresAtMostFormula(SatFormula sat, int n) {
		
		Clause zClause;
		Var zVar;
		Var cVar;
		int col; // absolute cell column (from 0 to dim)
		int row; // absolute cell row (from 0 to dim)
		int col2;
		int row2;
		
		for (int sc = 0; sc != getSize(); sc++) {
			// square column
			
			for (int sr = 0; sr != getSize(); sr++) {
				// square row
			
				// once per square and n
				zClause = new Clause();
				
				for (int i = 0; i != getSize(); i++) {
					// cell column inside square
					
					for (int j = 0; j != getSize(); j++) {
						// cell row inside square
						
						col = sc*getSize() + i;
						row = sr*getSize() + j;
						
						// once per cell and n
						zVar = (Var) makeVar("ZS",col,row,n,true);
						
						// i.e. {ZS000...}
						zClause = zClause.add(new PosLiteral(zVar.getName()));
						
						for (int i2 = 0; i2 != getSize(); i2++) {
							// cell column inside square
							
							for (int j2 = 0; j2 != getSize(); j2++) {
								// cell row inside square

								col2 = sc*getSize() + i2;
								row2 = sr*getSize() + j2;

								// i.e. {!ZS000...}
								sat = sat.clause().neg(zVar.getName());
								
								cVar = (Var) makeCellVar(col2,row2,n,true);
										
								if (i2 == i && j2 == j) {
									// i.e. {!ZS000C000}
									sat = sat.pos(cVar.getName());
								} else {
									// i.e. {!ZS000!C100}
									sat = sat.neg(cVar.getName());
								}
								
							}
						}

						// once per cell and n
						//i.e. {^ZS000C000}{!ZS000!C010}{!ZS000!C100}{!ZS000!C110}				
					}
				}
				
				//i.e. {ZS000ZS010ZS100ZS110}
				sat = sat.add(zClause);
				
			}
			
		}
		
		return sat;
	}
	
	

}
