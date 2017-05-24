package sudoku.latinsquares;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Var;
import sudoku.latinsquares.LatinSquaresFormula;

public class LatinSquaresTests {

//	@Test
//	public void testSizeTwoAtLeast() {
//		LatinSquares l = new LatinSquares(2);
//		
//		Formula f = l.getAtLeastFormula(null,0);
//		f = l.getAtLeastFormula(f,1);
//		
//		System.out.println(f);
//		Env env = f.solve();
//		System.out.println(l.interpretResult(env));
//	}

//	@Test 
	public void testSizeTwoAtMost() {
		LatinSquaresFormula l = new LatinSquaresFormula(1);
		
		Formula f = l.getFormula();
		
//		System.out.println(f);
//		System.out.println(f.vars());
		Env env = f.solve();
		System.out.println(l.interpretResult(env));
	}
	
//	@Test
	public void testPartialSolution() {
		LatinSquaresFormula l = new LatinSquaresFormula(2);
		Formula f = l.getFormula();
		Env partial = new Env();
		
		// test full solution
		Var C000 = Var.makeVar("C000");
		Var C011 = Var.makeVar("C011");
		Var C101 = Var.makeVar("C101");
		Var C110 = Var.makeVar("C110");
		partial = partial.put(C000, Bool.TRUE);
		partial = partial.put(C011, Bool.TRUE);
		partial = partial.put(C101, Bool.TRUE);
		partial = partial.put(C110, Bool.TRUE);
		Env env = f.solve(partial);
//		System.out.println(l.interpretResult(env));
		
	}

}
