package sudoku.test.latinsquares;

import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Var;
import sudoku.latinsquares.LatinSquares;

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

	@Test
	public void testSizeTwoAtMost() {
		LatinSquares l = new LatinSquares(1);
		
		Formula f = l.getFormula();
		
//		System.out.println(f);
//		System.out.println(f.vars());
		Env env = f.solve();
		System.out.println(l.interpretResult(env));
	}
	
//	@Test
	public void testPartialSolution() {
		LatinSquares l = new LatinSquares(2);
		Formula f = l.getFormula();
		Env partial = new Env();
		
		// test full solution
		Var C000 = new Var("C000");
		Var C011 = new Var("C011");
		Var C101 = new Var("C101");
		Var C110 = new Var("C110");
		partial = partial.put(C000, Bool.TRUE);
		partial = partial.put(C011, Bool.TRUE);
		partial = partial.put(C101, Bool.TRUE);
		partial = partial.put(C110, Bool.TRUE);
		Env env = f.solve(partial);
		System.out.println(l.interpretResult(env));
		
	}
	
}
