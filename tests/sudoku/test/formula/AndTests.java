package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.And;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Not;
import sudoku.formula.Var;
import sudoku.set.Set;

public class AndTests {

	@Test
	public void testConstructor() {
		Var a = new Var("a");
		Var b = new Var("b");
		And f = new And(a,b);
		Assert.assertEquals(a, f.getFirst());
		Assert.assertEquals(b, f.getSecond());
	}
	
	@Test
	public void testVars() {
		Var a = new Var("a");
		Var b = new Var("b");
		And f = new And(a,b);
		Set<Var> vars = f.vars();
		Assert.assertTrue(vars.contains(a));
		Assert.assertTrue(vars.contains(b));
	}
	
	@Test
	public void testEval() {
		Var a = new Var("a");
		Var b = new Var("b");
		And f = new And(a,b);
		Assert.assertTrue(f.eval(new Env().put(a, true).put(b,true)));
		Assert.assertFalse(f.eval(new Env().put(a, true).put(b,false)));
		Assert.assertFalse(f.eval(new Env().put(a, false).put(b,true)));
		Assert.assertFalse(f.eval(new Env().put(a, false).put(b,false)));
	}
	
	@Test
	public void testSolve() {
		Var a = new Var("a");
		Var b = new Var("b");
		
		Formula f = new And(a,b);
		Env env = f.solve();
		Assert.assertTrue(env.get(a));
		Assert.assertTrue(env.get(b));
		
		f = new And(a,new Not(b));
		env = f.solve();
		System.out.println(env);
		Assert.assertTrue(env.get(a));
		Assert.assertFalse(env.get(b));
		
		f = new And(new Not(a),b);
		env = f.solve();
		System.out.println(env);
		Assert.assertFalse(env.get(a));
		Assert.assertTrue(env.get(b));
		
		f = new And(new Not(a), new Not(b));
		env = f.solve();
		System.out.println(env);
		Assert.assertFalse(env.get(a));
		Assert.assertFalse(env.get(b));
	}
}
