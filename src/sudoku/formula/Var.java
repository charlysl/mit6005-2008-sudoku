package sudoku.formula;

import java.util.HashMap;
import java.util.Map;

import sudoku.set.Set;

public class Var extends Formula {

	private String name;
	
	private static Map<String,Var> instances = 
							new HashMap<String,Var>();
	
	private Var(String name) {
		this.name = name;
	}
	
	public static Var makeVar(String name) {
		// Factory method that performs interning.
		//
		// Thanks to interning, Vars are equal if they
		// are the same object, so == can be used to
		// implement equals, which should be faster
		// than calling equals on the name strings.
		Var instance = instances.get(name);
		if (instance == null) {
			instance = new Var(name);
			instances.put(name, instance);
		}
		return instance;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public Set<Var> vars() {
		return new Set<Var>(this);
	}

	@Override
	public Bool eval(Env env) {
		return env.get(this);
	}

	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * {@link sudoku.Set#contains(T) Set.contains(T t)} relies on {@link #equals(Object) equals},
	 * and, indirectly, so do {@link sudoku.formula.Env Env} and several
	 * test methods.
	 * 
	 * Given that Var is an immutable object, and its value is then just like a 
	 * math value, it has no identity other that it's value; that is,
	 * the {@link #name name} field.
	 * 
	 * Due to interning, if 2 vars are ==, they are equal too.
	 * 
	 */
	@Override
	public boolean equals(Object o) {
		return this == o;		
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	String buildString(String str) {
		return str + toString();
	}
}
