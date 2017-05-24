package sudoku.sat;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;

public class SatFormulaTests {

	@Test
	public void testToString() {
		SatFormula sat = new SatFormula();
		
		Assert.assertEquals("", sat.toString());
		
		Clause clause = new Clause();
		clause = clause.add(new PosLiteral("a"));
		sat = sat.add(clause);
		Assert.assertEquals("{a}", sat.toString());
		
		clause = new Clause();
		clause = clause.add(new NegLiteral("b"));
		sat = sat.add(clause);
		Assert.assertEquals("{!b}{a}", sat.toString());
		
	}
	
	@Test
	public void testVars() {
		SatFormula sat = new SatFormula();
		Assert.assertEquals(0, sat.vars().size());
		
		
		Clause clause = new Clause();
		Literal a = new PosLiteral("a");
		clause = clause.add(a);
		sat = sat.add(clause);
		Assert.assertTrue(sat.vars().contains(a.getVar()));

		clause = new Clause();
		Literal b = new NegLiteral("b");
		clause = clause.add(b);
		sat = sat.add(clause);
		Assert.assertTrue(sat.vars().contains(b.getVar()));
		Assert.assertTrue(sat.vars().contains(a.getVar()));
		
	}
	
	@Test
	public void testReduce() {
		SatFormula sat = new SatFormula();
		Env env = null;
		Assert.assertNull(sat.reduce(env));
		
		Clause clause = new Clause();
		sat = sat.add(clause);
		Assert.assertEquals("", sat.reduce(env).toString());
		
		clause = new Clause();
		Literal a = new PosLiteral("a");
		clause = clause.add(a);
		sat = new SatFormula();
		sat = sat.add(clause);
		env = new Env();
		Assert.assertEquals("{a}", sat.reduce(env).toString());
		
		env = new Env();
		env = env.put(a.getVar(), Bool.TRUE);
		Assert.assertNull(sat.reduce(env));
		
		env = new Env();
		env = env.put(a.getVar(), Bool.FALSE);
		Assert.assertTrue(sat.reduce(env).isEmpty());
	}
	
	@Test
	public void testSolve() {
		// use facade, the code is much more readable
		
		SatFormula sat;

		sat = new SatFormula().clause().pos("a");
		Assert.assertEquals("TRUE[a] FALSE[]", sat.solve().toString());
		
		sat = new SatFormula().clause().neg("b");
		Assert.assertEquals("TRUE[] FALSE[b]", sat.solve().toString());

		sat = new SatFormula().clause().pos("a").clause().neg("b");
		Assert.assertEquals("TRUE[a] FALSE[b]", sat.solve().toString());

		sat = new SatFormula().clause().pos("a").clause().neg("a");
		Assert.assertNull(sat.solve());
	
	}
	
	@Test
	public void testAddClause() {
		SatFormula sat = new SatFormula();
		Clause clause = new Clause();
		SatFormula sat2 = sat.add(clause);
		// check that SatFormula is immutable, that add is a producer
		Assert.assertFalse(sat == sat2);
	}
	
	@Test
	public void testFacade() {
		// reduce coupling to that clients only depend on
		// SatFormula and not on Clause, PosLiteral and NegLiteral too
		
		SatFormula sat = new SatFormula();
		sat = sat.clause();
		Assert.assertEquals("{}", sat.toString());
		
		sat = new SatFormula();
		sat = sat.clause().pos("a");
		Assert.assertEquals("{a}", sat.toString());
		
		sat = new SatFormula();
		sat = sat.clause().neg("b");
		Assert.assertEquals("{!b}", sat.toString());
		
		sat = new SatFormula();
		sat = sat.clause().pos("a").neg("b");
		Assert.assertEquals("{!ba}", sat.toString());
		
		sat = new SatFormula();
		sat = sat.clause().pos("a").clause().neg("b");
		Assert.assertEquals("{!b}{a}", sat.toString());
		
	}
	
	@Test
	public void testSampleFormula() {
		// (P v Q) ^ ('P v R)
		SatFormula sat = new SatFormula().clause().pos("P").pos("Q")
										.clause().neg("P").pos("R");
		
		Env env = sat.solve();
		
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
		//
		// {!SH}{!HM}{S}{!M}	  
		//
		// !{!SM} in CNF is {S}{!M} 
		// 00 0     
		// 01 0
		// 10 1
		// 11 0
		
		SatFormula sat = new SatFormula().clause().neg("S").pos("H")
										.clause().neg("H").pos("M")
										.clause().pos("S")
										.clause().neg("M");
		
		// No solution if true
		Assert.assertNull(sat.solve());

	}
	
	@Test
	public void testMin() {
		SatFormula sat;
		
		sat = new SatFormula().clause().pos("a");
		Assert.assertEquals("{a}", sat.min().toString());

		sat = new SatFormula().clause().pos("a").neg("b");
		Assert.assertEquals("{!ba}", sat.min().toString());

		sat = new SatFormula().clause().pos("a").neg("b")
							 .clause().pos("a");
		Assert.assertEquals("{a}", sat.min().toString());	

		sat = new SatFormula().clause().pos("a").neg("b").pos("d")
							 .clause().pos("c")
							 .clause().pos("a").neg("b");
		Assert.assertEquals("{c}", sat.min().toString());
		
		// {!P}{QP}
		sat = new SatFormula().clause().pos("P").pos("Q")
							 .clause().neg("P");
//		System.out.println(sat);
		Assert.assertEquals("{!P}", sat.min().toString());
	}

	
}
