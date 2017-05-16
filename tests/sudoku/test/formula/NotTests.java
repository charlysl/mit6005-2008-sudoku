package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Env;
import sudoku.formula.Not;
import sudoku.formula.Var;

public class NotTests {

	@Test
	public void testConstructor() {
		Var var = new Var("a");
		Not f = new Not(var);
		Assert.assertEquals(var, f.getFormula());
	}
	
	@Test
	public void testVars() {
		Var var = new Var("a");
		Not f = new Not(var);
		Assert.assertTrue(f.vars().contains(var));
	}
	
	@Test
	public void testEval() {
		Var var = new Var("a");
		Not f = new Not(var);
		Env env = new Env().put(var,false);
		Assert.assertTrue(f.eval(env));
	}
	
	@Test
	public void testSolve() {
		Var var = new Var("a");
		Not f = new Not(var);
		Env env = f.solve();
		Assert.assertFalse(env.get(var));
	}
}
