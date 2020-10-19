package fun.rega.RegaBans.utils;

public enum BanType {
	  BAN, BANIP, MUTE;
	  
	  public static BanType getByName(String name) {
	    try {
	      return Enum.<BanType>valueOf(BanType.class, name.toUpperCase());
	    } catch (IllegalArgumentException e) {
	      return null;
	    } 
	  }
	}
