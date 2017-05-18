package sudoku.list;

public class EmptyList<T> extends List<T> {
	
	@Override
	public String toString() {
		return "[]";
	}

	@Override
	public int size() {
		return 0;
	}
	
}
