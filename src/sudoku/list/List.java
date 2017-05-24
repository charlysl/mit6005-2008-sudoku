package sudoku.list;

import java.util.Iterator;

public abstract class List<T> implements Iterable<T> {
	public abstract int size();

	public Iterator<T> iterator() {
		return new Iterator<T>() {

			List<T> list = List.this;
			T first;
			
			@Override
			public boolean hasNext() {
				return !(list instanceof EmptyList<?>);
			}

			@Override
			public T next() {
				first = ((Cons<T>) list).getFirst();
				list = ((Cons<T>) list).getRest();
				return first;
			}

			@Override
			public void remove() {
				// empty
			}
		};
	}

}
