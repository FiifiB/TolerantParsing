package correctiontool;

public class TestFailedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TestFailedException(){
		
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "The corrected document does not match the expected document";
	}

}
