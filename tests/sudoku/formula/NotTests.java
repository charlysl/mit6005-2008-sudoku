package sudoku.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;
import sudoku.formula.Formula;
import sudoku.formula.Not;
import sudoku.formula.Var;

public class NotTests {

	@Test
	public void testConstructor() {
		Var var = Var.makeVar("a");
		Not f = new Not(var);
		Assert.assertEquals(var, f.getFormula());
	}
	
	@Test
	public void testVars() {
		Var var = Var.makeVar("a");
		Not f = new Not(var);
		Assert.assertTrue(f.vars().contains(var));
	}
	
	@Test
	public void testEval() {
		Var var = Var.makeVar("a");
		Not f = new Not(var);
		
		Env env = new Env().put(var, Bool.FALSE);
		Assert.assertEquals(Bool.TRUE, f.eval(env));
		
		env = new Env().put(var, Bool.TRUE);
		Assert.assertEquals(Bool.FALSE, f.eval(env));
		
		env = new Env();
		Assert.assertEquals(Bool.UNDEFINED, f.eval(env));
	}
	
	@Test
	public void testSolve() {
		Var var = Var.makeVar("a");
		Not f = new Not(var);
		Env env = f.solve();
		Assert.assertEquals(Bool.FALSE, env.get(var));
	}
	
	@Test
	public void testToString() {
		Assert.assertEquals("'a", Formula.var("a").not().toString());
	}
}
