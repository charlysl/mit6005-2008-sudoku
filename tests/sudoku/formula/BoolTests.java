package sudoku.formula;

import org.junit.Assert;
import org.junit.Test;

import sudoku.formula.Bool;

public class BoolTests {

	@Test
	public void testAnd() {
		Assert.assertEquals(
				Bool.FALSE, Bool.FALSE.and(Bool.FALSE));
		Assert.assertEquals(
				Bool.FALSE, Bool.FALSE.and(Bool.TRUE));
		Assert.assertEquals(
				Bool.FALSE, Bool.TRUE.and(Bool.FALSE));
		Assert.assertEquals(
				Bool.TRUE, Bool.TRUE.and(Bool.TRUE));
		Assert.assertEquals(
				Bool.UNDEFINED, Bool.UNDEFINED.and(Bool.UNDEFINED));
		Assert.assertEquals(
				Bool.FALSE, Bool.UNDEFINED.and(Bool.FALSE));
		Assert.assertEquals(
				Bool.UNDEFINED, Bool.UNDEFINED.and(Bool.TRUE));
		Assert.assertEquals(
				Bool.UNDEFINED, Bool.TRUE.and(Bool.UNDEFINED));
		Assert.assertEquals(
				Bool.FALSE, Bool.FALSE.and(Bool.UNDEFINED));
	}
	

	@Test
	public void testOr() {
		Assert.assertEquals(
				Bool.FALSE, Bool.FALSE.or(Bool.FALSE));
		Assert.assertEquals(
				Bool.TRUE, Bool.FALSE.or(Bool.TRUE));
		Assert.assertEquals(
				Bool.TRUE, Bool.TRUE.or(Bool.FALSE));
		Assert.assertEquals(
				Bool.TRUE, Bool.TRUE.or(Bool.TRUE));
		Assert.assertEquals(
				Bool.UNDEFINED, Bool.UNDEFINED.or(Bool.UNDEFINED));
		Assert.assertEquals(
				Bool.UNDEFINED, Bool.UNDEFINED.or(Bool.FALSE));
		Assert.assertEquals(
				Bool.UNDEFINED, Bool.FALSE.or(Bool.UNDEFINED));
		
		// This is the point, if one operand is true, the
		// or expression is true, no matter what the other
		// operand is, it could even be undefined:
		Assert.assertEquals(
				Bool.TRUE, Bool.UNDEFINED.or(Bool.TRUE));
		Assert.assertEquals(
				Bool.TRUE, Bool.TRUE.or(Bool.UNDEFINED));	
	}
	
	@Test
	public void testNot() {
		Assert.assertEquals(
				Bool.TRUE, Bool.FALSE.not());
		Assert.assertEquals(
				Bool.FALSE, Bool.TRUE.not());
		Assert.assertEquals(
				Bool.UNDEFINED, Bool.UNDEFINED.not());
	}	
}
