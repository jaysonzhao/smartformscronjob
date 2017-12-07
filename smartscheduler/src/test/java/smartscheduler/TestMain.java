package smartscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.gzsolartech.smartforms.entity.bpm.BpmActivityMeta;

public class TestMain {

	public static void main(String[] args) {
		List<BpmActivityMeta> nextActies=new ArrayList<>();
		BpmActivityMeta meta1=new BpmActivityMeta();
		meta1.setActivityId("ccc");
		meta1.setSortNum(1);
		nextActies.add(meta1);
		
		meta1=new BpmActivityMeta();
		meta1.setActivityId("bbb");
		meta1.setSortNum(0);
		nextActies.add(meta1);
		
		meta1=new BpmActivityMeta();
		meta1.setActivityId("ddd");
		meta1.setSortNum(3);
		nextActies.add(meta1);
		
		meta1=new BpmActivityMeta();
		meta1.setActivityId("aaa");
		meta1.setSortNum(3);
		nextActies.add(meta1);
		
		//先按照序号，再按照名称，从小到大排序
		nextActies=nextActies.stream().sorted((e1, e2)->{
			Integer it1=e1.getSortNum();
			it1 = (it1==null) ? 0 : it1.intValue();
			Integer it2=e2.getSortNum();
			it2 = (it2==null) ? 0 : it2.intValue();
			if (it1.equals(it2)) {
				return e1.getActivityId().compareTo(e2.getActivityId());
			} else {
				return it1.compareTo(it2);
			}
		}).collect(Collectors.toList());
		
		for (BpmActivityMeta tmpmeta : nextActies) {
			System.out.println("itemid="+tmpmeta.getActivityId()+", sortnum="+tmpmeta.getSortNum());
		}
	}

}
