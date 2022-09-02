package kr.go.ads.compare;

import java.util.Comparator;
import kr.go.ads.client.ReceiveData;

public class MakeAscCompare implements Comparator<ReceiveData> {

	public int compare(ReceiveData arg1, ReceiveData arg2) {
		// TODO Auto-generated method stub
		return arg1.getCreateDate().compareTo(arg2.getCreateDate());
	}

}
