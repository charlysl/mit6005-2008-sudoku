package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.And;
import sudoku.formula.Formula;
import sudoku.formula.Not;
import sudoku.formula.Or;
import sudoku.formula.Var;

public class FormulaTests {
	
	@Test
	public void testSocrates() {
		// Automatically check if true:
		//
		// If Socrates => Human and Human => Mortal then Socrates => Mortal
		//
		// axioms:
		// Socrates => Human
		// Human => Mortal
		//
		// theorem:
		// Socrates => Mortal
		
		// build formula
		
		Var s = new Var("Socrates");
		Var h = new Var("Human");
		Var m = new Var("Mortal");
		
		Formula f = new And(
						new Or( new Not(s), h),
						new And(
								new Or(new Not(h), m),
								new Not(
										new Or(new Not(s), m)
								)
						)
					);
		
		// if the theorem is true, then the formula has no solution		
		Assert.assertNull(f.solve());
	}
}