package sudoku.test.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Env;
import sudoku.formula.Var;

public class EnvTests {
	
	@Test
	public void testPutAndGet() {
		Env env = new Env();
		Var var = new Var("a");

		env = env.put(var,true);
		Assert.assertTrue(env.get(var));

		env = env.put(var,true);
		Assert.assertTrue(env.get(var));

		env = env.put(var,false);
		Assert.assertFalse(env.get(var));

		Var var2 = new Var("b");

		env = env.put(var2,false);
		Assert.assertFalse(env.get(var2));

		env = env.put(var2,true);
		Assert.assertTrue(env.get(var2));

		env = env.put(var,false);
		Assert.assertFalse(env.get(var));
	}
}
