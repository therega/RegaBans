package fun.rega.RegaBans.utils;

public class CheckKeyResult {
	  private boolean result;
	  
	  private String[] args;
	  
	  public CheckKeyResult() {
	    this.result = false;
	  }
	  
	  public CheckKeyResult(String[] args) {
	    this.result = true;
	    this.args = args;
	  }
	  
	  public boolean getResult() {
	    return this.result;
	  }
	  
	  public String[] getArguments() {
	    return this.args;
	  }
	}
