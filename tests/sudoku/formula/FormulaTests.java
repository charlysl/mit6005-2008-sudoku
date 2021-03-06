package sudoku.formula;

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
		
		Var p = Var.makeVar("P");
		Var q = Var.makeVar("Q");
		Var r = Var.makeVar("R");
		
		Formula f = new And(new Or(p,q), new Or(new Not(p),r));
		
		Env env = f.solve();
		
		Assert.assertEquals(Bool.TRUE,
				(env.get(p).or(env.get(q))) 
				 .and(env.get(p).not()).or(env.get(r)));
	}
	
	@Test
	public void testSampleFormulaWithFacade() {
		Formula f = getSampleFormula();

		Env env = f.solve();
		
		Var p = Var.makeVar("P");
		Var q = Var.makeVar("Q");
		Var r = Var.makeVar("R");

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
		
		Var s = Var.makeVar("Socrates");
		Var h = Var.makeVar("Human");
		Var m = Var.makeVar("Mortal");
		
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

	@Test
	public void testPartialSolution() {
		Var a = Var.makeVar("a");
		Env partial = new Env();
		partial = partial.put(a, Bool.TRUE);
		Env solution = a.solve(partial);
		Assert.assertEquals(1, solution.size());
		Assert.assertEquals(Bool.TRUE, solution.get(a));
		
		Formula f = getSampleFormula();
		partial = new Env();
		Var p = Var.makeVar("P");
//		Var r = Var.makeVar("R");
		partial = partial.put(p, Bool.TRUE);
//		partial = partial.put(r, Bool.TRUE);
		solution = f.solve(partial);
		Assert.assertEquals(Bool.TRUE, solution.get(p));
//		Assert.assertEquals(Bool.TRUE, solution.get(r));
	}
	
	@Test
	public void testWrongPartialSolution() {
		Var a = Var.makeVar("a");
		Env partial = new Env();
		partial = partial.put(a, Bool.FALSE);
		Assert.assertNull(a.solve(partial));
	}

	private Formula getSampleFormula() {
		return Formula.var("P").or(Formula.var("Q"))
				.and(Formula.var("P").not().or(Formula.var("R")));
	}
	
}
