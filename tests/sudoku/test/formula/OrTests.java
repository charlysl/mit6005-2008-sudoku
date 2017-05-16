package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Not;
import sudoku.formula.Or;
import sudoku.formula.Var;
import sudoku.set.Set;

public class OrTests {

	@Test
	public void testConstructor() {
		Var a = new Var("a");
		Var b = new Var("b");
		Or f = new Or(a,b);
		Assert.assertEquals(a, f.getFirst());
		Assert.assertEquals(b, f.getSecond());
	}
	
	@Test
	public void testVars() {
		Var a = new Var("a");
		Var b = new Var("b");
		Or f = new Or(a,b);
		Set<Var> vars = f.vars();
		Assert.assertTrue(vars.contains(a));
		Assert.assertTrue(vars.contains(b));
	}
	
	@Test
	public void testEval() {
		Var a = new Var("a");
		Var b = new Var("b");
		Or f = new Or(a,b);
		Assert.assertTrue(f.eval(new Env().put(a, true).put(b,true)));
		Assert.assertTrue(f.eval(new Env().put(a, true).put(b,false)));
		Assert.assertTrue(f.eval(new Env().put(a, false).put(b,true)));
		Assert.assertFalse(f.eval(new Env().put(a, false).put(b,false)));
	}
	
	@Test
	public void testSolve() {
		Var a = new Var("a");
		Var b = new Var("b");
		
		Formula f = new Or(a,b);
		Env env = f.solve();
		Assert.assertTrue(env.get(a) || env.get(b));
		
		f = new Or(a,new Not(b));
		env = f.solve();
		System.out.println(env);
		Assert.assertTrue(env.get(a) || !env.get(b));
		
		f = new Or(new Not(a),b);
		env = f.solve();
		System.out.println(env);
		Assert.assertFalse(env.get(a));
		Assert.assertTrue(!env.get(a) || env.get(b));
		
		f = new Or(new Not(a), new Not(b));
		env = f.solve();
		System.out.println(env);
		Assert.assertTrue(!env.get(a) || !env.get(b));
	}
}
