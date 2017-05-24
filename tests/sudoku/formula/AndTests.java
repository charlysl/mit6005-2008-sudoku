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
import sudoku.set.Set;

public class AndTests {

	@Test
	public void testConstructor() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");
		And f = new And(a,b);
		Assert.assertEquals(a, f.getFirst());
		Assert.assertEquals(b, f.getSecond());
	}
	
	@Test
	public void testVars() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");
		And f = new And(a,b);
		Set<Var> vars = f.vars();
		Assert.assertTrue(vars.contains(a));
		Assert.assertTrue(vars.contains(b));
	}
	
	@Test
	public void testEval() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");
		And f = new And(a,b);
		Assert.assertEquals(Bool.TRUE,
			f.eval(new Env().put(a, Bool.TRUE).put(b,Bool.TRUE)));
		Assert.assertEquals(Bool.FALSE,
			f.eval(new Env().put(a, Bool.TRUE).put(b,Bool.FALSE)));
		Assert.assertEquals(Bool.FALSE,
			f.eval(new Env().put(a, Bool.FALSE).put(b,Bool.TRUE)));
		Assert.assertEquals(Bool.FALSE,
			f.eval(new Env().put(a, Bool.FALSE).put(b,Bool.FALSE)));
		Assert.assertEquals(Bool.UNDEFINED,
				f.eval(new Env()));
	}
	
	@Test
	public void testSolve() {
		Var a = Var.makeVar("a");
		Var b = Var.makeVar("b");
		
		Formula f = new And(a,b);
		Env env = f.solve();
		Assert.assertEquals(Bool.TRUE, env.get(a));
		Assert.assertEquals(Bool.TRUE, env.get(b));
		
		f = new And(a,new Not(b));
		env = f.solve();
//		System.out.println(env);
		Assert.assertEquals(Bool.TRUE, env.get(a));
		Assert.assertEquals(Bool.FALSE, env.get(b));
		
		f = new And(new Not(a),b);
		env = f.solve();
//		System.out.println(env);
		Assert.assertEquals(Bool.FALSE, env.get(a));
		Assert.assertEquals(Bool.TRUE, env.get(b));
		
		f = new And(new Not(a), new Not(b));
		env = f.solve();
//		System.out.println(env);
		Assert.assertEquals(Bool.FALSE, env.get(a));
		Assert.assertEquals(Bool.FALSE, env.get(b));
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals("{a}{b}", Formula.var("a")
											.and(Formula.var("b")).toString());
		Assert.assertEquals("{a}{b}{c}", Formula.var("a")
				.and(Formula.var("b")).and(Formula.var("c")).toString());
		Assert.assertEquals("{ab}{cd}", new And(
											new Or(Var.makeVar("a"),Var.makeVar("b")),
											new Or(Var.makeVar("c"),Var.makeVar("d"))
							).toString());
	}
}
