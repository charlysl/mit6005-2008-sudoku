package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Var;
import sudoku.set.Set;

public class VarTests {

	@Test
	public void testConstructor() {
		Var f = new Var("a");
		Assert.assertEquals("a", f.getName());
	}
	
	@Test
	public void testVars() {
		Var f = new Var("a");
		Set<Var> vars = f.vars();
		
		// check that f is in vars
		Assert.assertTrue(vars.contains(f));
	}
	
	@Test 
	public void testEval() {
		Var f = new Var("a");
		
		Env env = new Env().put(f, Bool.TRUE);
		Assert.assertEquals(Bool.TRUE, f.eval(env));
		
		env = new Env().put(f, Bool.FALSE);
		Assert.assertEquals(Bool.FALSE, f.eval(env));

		env = new Env();
		Assert.assertEquals(Bool.UNDEFINED, f.eval(env));
	}
	
	@Test
	public void testSolveVarFormula() {
		Var f = new Var("a");
		Env env = f.solve();
		Assert.assertEquals(Bool.TRUE, env.get(f));
		
		
	}
	
	@Test
	public void testEquals() {
		Var a = new Var("A");
		Assert.assertEquals(a, a);
		
		Var b = new Var("A");
		Assert.assertEquals(a, b);
		Assert.assertEquals(b, a);
		
		Var c = new Var("C");
		Assert.assertNotEquals(a, c);
		Assert.assertNotEquals(c, a);
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals("a", new Var("a").toString());
	}
}
