package sudoku.list;

public class Cons<T> extends List<T> {

	private T first;
	private List<T> rest;
	
	private String string;
	private int size = -1;
	
	public Cons(T first, List<T> rest) {
		this.first = first;
		this.rest = rest;
	}
	
	public T getFirst() {
		return first;
	}
	
	public List<T> getRest() {
		return rest;
	}
	
	@Override
	public String toString() {
		if (string != null) {
			return string;
		}
		
		string = "[" + first + rest + "]";
		return string;
	}

	@Override
	public int size() {
		if (size == -1) {
			size = 1 + rest.size();
		}
		
		return size;
	}
}
