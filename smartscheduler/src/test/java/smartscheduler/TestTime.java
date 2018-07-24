package smartscheduler;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestTime {

	public static void main(String[] args) throws InterruptedException {
		Timestamp tsnow=new Timestamp(System.currentTimeMillis());
		Thread.sleep(1000);
		LocalDateTime ldtnow=LocalDateTime.now();
		LocalDateTime newLdt=ldtnow.plusMinutes(5);
		LocalDateTime ldtts=LocalDateTime.ofInstant(Instant.ofEpochMilli(tsnow.getTime()), ZoneId.of("Asia/Shanghai"));
		System.out.println("ldtnow="+ldtnow);
		System.out.println("ldtts="+ldtts);
		System.out.println("newLdt="+newLdt);
		System.out.println(ldtts.isAfter(ldtnow));
		System.out.println(ldtnow.isAfter(newLdt));
	}

}
