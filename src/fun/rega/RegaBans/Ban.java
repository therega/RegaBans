package fun.rega.RegaBans;


import java.util.concurrent.TimeUnit;
import fun.rega.RegaBans.*;
import fun.rega.RegaBans.utils.BanType;


public class Ban {
  private String player;
  
  private BanType type;
  
  private String owner;
  
  private long time;
  
  private long expire;
  
  private String reason;
  
  public Ban(String player, BanType type, String owner, long time, long expire, String reason) {
    this.player = player;
    this.type = type;
    this.owner = owner;
    this.time = time;
    this.expire = expire;
    this.reason = reason;
  }
  
  public String getPlayer() {
    return this.player;
  }
  
  public BanType getType() {
    return this.type;
  }
  
  public String getOwner() {
    return this.owner;
  }
  
  public long getTime() {
    return this.time;
  }
  
  public String getTimeLeft() {
    long left = this.expire - System.currentTimeMillis();
    if (left < 0L)
      left = 0L; 
    String res = String.format("%d дн, %d ч, %d мин, %d сек", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toDays(left)), Long.valueOf(TimeUnit.MILLISECONDS.toHours(left) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(left))), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(left) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(left))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(left) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(left))) });
    return res;
  }
  
  public long getExpire() {
    return this.expire;
  }
  
  public String getReason() {
    return this.reason;
  }
}

