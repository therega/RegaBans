package fun.rega.RegaBans.utils;

public class BuildTimeResult {
	  private boolean result;
	  
	  private long time;
	  
	  private long raw;
	  
	  private String unit;
	  
	  public BuildTimeResult(boolean result, long time, long raw, String unit) {
	    this.result = result;
	    this.time = time;
	    this.raw = raw;
	    this.unit = unit;
	  }
	  
	  public BuildTimeResult(boolean result) {
	    this.result = result;
	  }
	  
	  public boolean getResult() {
	    return this.result;
	  }
	  
	  public long getTime() {
	    return this.time;
	  }
	  
	  public long getRaw() {
	    return this.raw;
	  }
	  
	  public String getUnit() {
	    return this.unit;
	  }
	}