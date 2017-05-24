package sudoku.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Not;
import sudoku.formula.Or;
import sudoku.formula.Var;
import sudoku.set.Set;

public class OrTests {

	@Test
	public void testConstructor() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");
		Or f = new Or(a,b);
		Assert.assertEquals(a, f.getFirst());
		Assert.assertEquals(b, f.getSecond());
	}
	
	@Test
	public void testVars() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");
		Or f = new Or(a,b);
		Set<Var> vars = f.vars();
		Assert.assertTrue(vars.contains(a));
		Assert.assertTrue(vars.contains(b));
	}
	
	@Test
	public void testEval() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");
		Or f = new Or(a,b);
		Assert.assertEquals(Bool.TRUE,
			f.eval(new Env().put(a, Bool.TRUE).put(b,Bool.TRUE)));
		Assert.assertEquals(Bool.TRUE,
			f.eval(new Env().put(a, Bool.TRUE).put(b,Bool.FALSE)));
		Assert.assertEquals(Bool.TRUE,
			f.eval(new Env().put(a, Bool.FALSE).put(b,Bool.TRUE)));
		Assert.assertEquals(Bool.FALSE,
			f.eval(new Env().put(a, Bool.FALSE).put(b,Bool.FALSE)));
	}
	
	@Test
	public void testSolve() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");

		Formula f;
		Env env;
		
		f = new Or(a,b);
		env = f.solve();
		Assert.assertEquals(Bool.TRUE, env.get(a).or(env.get(b)));
		
		f = new Or(a,new Not(b));
		env = f.solve();
//		System.out.println(env);
		Assert.assertEquals(Bool.TRUE, 
				env.get(a).or(env.get(b).not()));
		
		f = new Or(new Not(a),b);
		env = f.solve();
//		System.out.println(env);
		Assert.assertEquals(Bool.FALSE, env.get(a));
		Assert.assertEquals(Bool.TRUE, 
				env.get(a).not().or(env.get(b)));
		
		f = new Or(new Not(a), new Not(b));
		env = f.solve();
//		System.out.println(env);
		Assert.assertEquals(Bool.TRUE,
				env.get(a).not().or(env.get(b).not()));
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals("{ab}", new Or(Var.makeVar("a"),Var.makeVar("b")).toString());
		Assert.assertEquals("{abc}", Formula.var("a")
										.or(Formula.var("b"))
										.or(Formula.var("c")).toString());
	}
}
