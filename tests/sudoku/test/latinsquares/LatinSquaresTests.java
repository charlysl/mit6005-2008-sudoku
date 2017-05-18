package sudoku.test.latinsquares;

import org.junit.Test;

import sudoku.formula.Env;
import sudoku.formula.Formula;
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
		LatinSquares l = new LatinSquares(2);
		
		Formula f = l.getFormula();
		
		System.out.println(f);
		System.out.println(f.vars());
		Env env = f.solve();
		System.out.println(l.interpretResult(env));
	}
	
	
}
