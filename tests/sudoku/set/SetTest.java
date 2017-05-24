package sudoku.set;

import org.junit.Assert;
import org.junit.Test;

import sudoku.set.Set;

public class SetTest {
	@Test
	public void testPick() {
		Object o = new Object();
		Set<Object> set = new Set<Object>(o);
		
		Assert.assertEquals(o, set.pick());
	}
	
	@Test
	public void testRemove() {
		Object o = new Object();
		Set<Object> set = new Set<Object>(o);
		
		Assert.assertEquals(o, set.pick());
		
		// remove an element that is not in the set
		set = set.remove(new Object());
		Assert.assertFalse(set.isEmpty());
		
		// remove an element that is in the set
		set = set.remove(o);
		Assert.assertTrue(set.isEmpty());		
	}
	
	@Test
	public void testRemoveMiddleElement() {
		// create set with 3 elements
		Object o = new Object();
		Set<Object> set = new Set<Object>(o);
		Object o2 = new Object();
		set = set.add(o2);
		Assert.assertTrue(set.contains(o2));
		Object o3 = new Object();
		set = set.add(o3);
		Assert.assertTrue(set.contains(o3));
		
		// remove middle element
		set = set.remove(o2);
		Assert.assertFalse(set.contains(o2));
		Assert.assertTrue(set.contains(o));
		Assert.assertTrue(set.contains(o3));
	}
	
	@Test
	public void testAdd() {
		// create a set with one object
		Object o = new Object();
		Set<Object> set = new Set<Object>(o);
		
		// add same object again and test that set didn't change
		Set<Object> set2 = set.add(o);
		Assert.assertEquals(set, set2);
		
		// add one new object
		Object o2 = new Object();
		set = set.add(o2);
		Assert.assertFalse(set.isEmpty());
		
		// must be empty after removing both objects
		set = set.remove(o).remove(o2);
		Assert.assertTrue(set.isEmpty());
	}
	
	@Test
	public void testContains() {
		Object o = new Object();
		Set<Object> set = new Set<Object>(o);
		Assert.assertTrue(set.contains(o));
	}
	
	@Test
	public void testEmptySet() {
		Set<Object> set = new Set<Object>();
		Object o = new Object();
		Assert.assertTrue(set.isEmpty());
		set = set.add(o);
		Assert.assertTrue(set.contains(o));
		set = set.remove(o);
		Assert.assertTrue(set.isEmpty());
	}
	
	@Test
	public void testAddAll() {
		Object o = new Object();
		Set<Object> set = new Set<Object>(o);
		Object o2 = new Object();
		Set<Object> set2 = new Set<Object>(o2);
		set = set.addAll(set2);
		Assert.assertTrue(set.contains(o));
		Assert.assertTrue(set.contains(o2));
	}
	
	@Test
	public void testIterable() {
		Set<Object> set = new Set<>();
		for (@SuppressWarnings("unused") Object o : set) {
		}
		
		Object o = new Object();
		set = new Set<>(o);
		int c = 0;
		for (@SuppressWarnings("unused") Object e : set) {
			c++;
		}
		Assert.assertEquals(1, c);
	}
}
