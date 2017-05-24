package sudoku.sat;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;
import sudoku.formula.Env;


public class ClauseTests {

	@Test
	public void testToString() {
		Clause clause = new Clause();

		Assert.assertEquals("{}", clause.toString());
		
		clause = clause.add(new PosLiteral("a"));
		Assert.assertEquals("{a}", clause.toString());
		
		clause = clause.add(new NegLiteral("b"));
		Assert.assertEquals("{!ba}", clause.toString());
	}
	
	@Test
	public void testEmpty() {
		Clause clause = new Clause();
		Assert.assertTrue(clause.isEmpty());
		clause = clause.add(new PosLiteral("a"));
		Assert.assertTrue(!clause.isEmpty());
	}
	
	@Test
	public void testReduce() {
		Clause clause = new Clause();
		Env env;
		env = new Env();

		clause = clause.reduce(env);
		Assert.assertTrue(clause.isEmpty());
		
		Literal plit = new PosLiteral("a");
		clause = clause.add(plit);
		
		Assert.assertEquals("{a}", clause.reduce(env).toString());
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.TRUE);
		Assert.assertNull(clause.reduce(env));

		env = new Env();
		env = env.put(plit.getVar(), Bool.FALSE);
		Assert.assertTrue(clause.reduce(env).isEmpty());

		Literal nlit = new NegLiteral("b");
		clause = clause.add(nlit);

		env = new Env();
		Assert.assertEquals("{a!b}", clause.reduce(env).toString());		
		
		env = new Env();
		env = env.put(nlit.getVar(), Bool.FALSE);
		Assert.assertNull(clause.reduce(env));
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.TRUE);
		Assert.assertNull(clause.reduce(env));
		
		env = new Env();
		env = env.put(nlit.getVar(), Bool.TRUE);
		Assert.assertEquals("{a}", clause.reduce(env).toString());
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.FALSE);
		Assert.assertEquals("{!b}", clause.reduce(env).toString());
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.TRUE);
		env = env.put(nlit.getVar(), Bool.FALSE);
		Assert.assertNull(clause.reduce(env));
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.FALSE);
		env = env.put(nlit.getVar(), Bool.TRUE);
		Assert.assertTrue(clause.reduce(env).isEmpty());
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.TRUE);
		env = env.put(nlit.getVar(), Bool.TRUE);
		Assert.assertNull(clause.reduce(env));
		
		env = new Env();
		env = env.put(plit.getVar(), Bool.FALSE);
		env = env.put(nlit.getVar(), Bool.FALSE);
		Assert.assertNull(clause.reduce(env));
		
	}
	
	@Test
	public void testVars() {
		Clause clause = new Clause();
		Assert.assertEquals(0, clause.vars().size());
		
		Literal plit = new PosLiteral("a");
		clause = clause.add(plit);
		Assert.assertTrue(clause.vars().contains(plit.getVar()));
		
	}
	
	@Test
	public void testSize() {
		Clause clause;
		
		clause = new Clause();
		Assert.assertEquals(0, clause.size());
		
		Assert.assertEquals(1, clause.add(new PosLiteral("a")).size());
	}
	
	@Test
	public void testPickLiteral() {
		Clause clause;
		
		clause = new Clause().add(new PosLiteral("a"));
		Assert.assertEquals("a", clause.pickLiteral().toString());
		
		clause = new Clause().add(new PosLiteral("a"))
							.add(new PosLiteral("b"));
		Assert.assertEquals("b", clause.pickLiteral().toString());
	}
}
