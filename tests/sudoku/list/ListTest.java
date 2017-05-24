package sudoku.list;

import org.junit.Assert;
import org.junit.Test;

import sudoku.list.Cons;
import sudoku.list.EmptyList;
import sudoku.list.List;

public class ListTest {

	@Test
	public void testCons() {
		// Cons(o,empty)
		Object first = new Object();
		List<Object> rest = new EmptyList<Object>();
		List<Object> list = new Cons<Object>(first,rest);
		Cons<Object> cons = (Cons<Object>) list;
		Assert.assertEquals(first, cons.getFirst());
		Assert.assertEquals(rest, cons.getRest());
		
		// Cons(o,list)
		List<Object> list2 = new Cons<Object>(first,list);
		Assert.assertEquals(first, ((Cons<Object>) list2).getFirst());
		Assert.assertEquals(list,((Cons<Object>) list2).getRest());
	}
}
