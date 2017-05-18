package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.And;
import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Not;
import sudoku.formula.Or;
import sudoku.formula.Var;

public class FormulaTests {

	@Test
	public void testSampleFormula() {
		// (P v Q) ^ ('P v R)
		
		Var p = new Var("P");
		Var q = new Var("Q");
		Var r = new Var("R");
		
		Formula f = new And(new Or(p,q), new Or(new Not(p),r));
		
		Env env = f.solve();
		
		Assert.assertEquals(Bool.TRUE,
				(env.get(p).or(env.get(q))) 
				 .and(env.get(p).not()).or(env.get(r)));
	}
	
	@Test
	public void testSampleFormulaWithFacade() {
		Formula f = Formula.var("P").or(Formula.var("Q"))
						.and(Formula.var("P").not().or(Formula.var("R")));

		Env env = f.solve();
		
		Var p = new Var("P");
		Var q = new Var("Q");
		Var r = new Var("R");

		Assert.assertEquals(Bool.TRUE,
				(env.get(p).or(env.get(q))) 
					.and(env.get(p).not().or(env.get(r))));
	}
	
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
	
	@Test
	public void testSocratesFormulaWithFacade() {

		Formula f = Formula.var("S").not().or(Formula.var("H"))
					.and(Formula.var("H").not().or(Formula.var("M"))
							.and(Formula.var("S").not()
									.or(Formula.var("M")).not()
							)
					);
		
		Assert.assertNull(f.solve());
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals("a", Formula.var("a").toString());
		Assert.assertEquals("{ab}", Formula.var("a")
									.or(Formula.var("b")).toString());
		Assert.assertEquals("{ab}{a'bc}", 
				Formula.var("a").or(Formula.var("b"))
					.and(Formula.var("a").or(Formula.var("b").not())
							.or(Formula.var("c"))).toString());
	}
	
}
