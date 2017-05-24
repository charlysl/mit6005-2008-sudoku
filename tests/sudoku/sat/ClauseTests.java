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
		Clause reduced;
		Env env;
		env = new Env();

		clause = clause.reduce(env);
		Assert.assertTrue(clause.isEmpty());
		
		Literal a = new PosLiteral("a");
		clause = clause.add(a);
		
		// {a} with "a" UNDEFINED must reduce to same {a}
		reduced = clause.reduce(env);
		Assert.assertTrue(reduced == clause);
		Assert.assertEquals("{a}", reduced.toString());
		
		// {a} with "a" TRUE must reduce to null
		env = new Env();
		env = env.put(a.getVar(), Bool.TRUE);
		Assert.assertNull(clause.reduce(env));

		// {a} with "a" FALSE must reduce to {}
		env = new Env();
		env = env.put(a.getVar(), Bool.FALSE);
		Assert.assertTrue(clause.reduce(env).isEmpty());

		Literal b = new NegLiteral("b");
		clause = clause.add(b);

		// {!ba} with "a" and "b" UNDEFINED must reduce same {!ba}
		env = new Env();
		reduced = clause.reduce(env);
		Assert.assertTrue(reduced == clause);
		Assert.assertEquals("{!ba}", reduced.toString());		
		
		// {!ba} with "a" UNDEFINED and "b" FALSE must reduce to null
		env = new Env();
		env = env.put(b.getVar(), Bool.FALSE);
		Assert.assertNull(clause.reduce(env));
		
		// {!ba} with "a" TRUE and "b" UNDEFINED must reduce to null
		env = new Env();
		env = env.put(a.getVar(), Bool.TRUE);
		Assert.assertNull(clause.reduce(env));
		
		// {!ba} with "a" UNDEFINED and "b" TRUE must reduce to {a}
		env = new Env();
		env = env.put(b.getVar(), Bool.TRUE);
		Assert.assertEquals("{a}", clause.reduce(env).toString());
		
		// {!ba} with "a" FALSE and "b" UNDEFINED must reduce to {!b}
		env = new Env();
		env = env.put(a.getVar(), Bool.FALSE);
		Assert.assertEquals("{!b}", clause.reduce(env).toString());
		
		// {!ba} with "a" TRUE and "b" FALSE must reduce to null
		env = new Env();
		env = env.put(a.getVar(), Bool.TRUE);
		env = env.put(b.getVar(), Bool.FALSE);
		Assert.assertNull(clause.reduce(env));
		
		// {!ba} with "a" FALSE and "b" TRUE must reduce to {}
		env = new Env();
		env = env.put(a.getVar(), Bool.FALSE);
		env = env.put(b.getVar(), Bool.TRUE);
		Assert.assertTrue(clause.reduce(env).isEmpty());
		
		// {!ba} with "a" TRUE and "b" TRUE must reduce to null
		env = new Env();
		env = env.put(a.getVar(), Bool.TRUE);
		env = env.put(b.getVar(), Bool.TRUE);
		Assert.assertNull(clause.reduce(env));
		
		// {!ba} with "a" FALSE and "b" FALSE must reduce to null
		env = new Env();
		env = env.put(a.getVar(), Bool.FALSE);
		env = env.put(b.getVar(), Bool.FALSE);
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
