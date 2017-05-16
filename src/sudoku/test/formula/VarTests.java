package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

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
		
		Env env = new Env().put(f,true);
		Assert.assertTrue(f.eval(env));
		
		env = new Env().put(f,false);
		Assert.assertFalse(f.eval(env));
	}
	
	@Test
	public void testSolveVarFormula() {
		Var f = new Var("a");
		Env env = f.solve();
		Assert.assertTrue(env.get(f));
		
		
	}
	
}
