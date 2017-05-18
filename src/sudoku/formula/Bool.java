package sudoku.formula;

public enum Bool {
	TRUE, 
	FALSE, 
	UNDEFINED;

	public Bool and(Bool b) {
		if (this.equals(UNDEFINED) || b.equals(UNDEFINED)) {
			return UNDEFINED;
		} else if (this.equals(TRUE) && b.equals(TRUE)) {
			return TRUE;
		} else {
			return FALSE;
		}
	}
	
	public Bool or(Bool b) {
		 if (this.equals(TRUE) || b.equals(TRUE)) {
			return TRUE;
		 } else if (this.equals(FALSE) && b.equals(FALSE)) {
			return FALSE;
		} else {
			return UNDEFINED;
		}
	}

	public Bool not() {
		if (this.equals(UNDEFINED)) {
			return UNDEFINED;
		} else if (this.equals(FALSE)) {
			return TRUE;
		} else {
			return FALSE;
		}
	}	
	
}
