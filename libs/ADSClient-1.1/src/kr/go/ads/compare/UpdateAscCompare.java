package kr.go.ads.compare;

import java.util.Comparator;
import kr.go.ads.client.ReceiveData;

public class UpdateAscCompare implements Comparator<ReceiveData> {

	public int compare(ReceiveData arg1, ReceiveData arg2) {
		// 파일 생성일과 파일일자 및 재반영 여부를 확인하여 처리
		int compareMakeDay = arg1.getCreateDate().compareTo(arg2.getCreateDate());
		if(compareMakeDay == 0){
			compareMakeDay = arg1.getFileDate().compareTo(arg2.getFileDate());
			if(compareMakeDay == 0){
				compareMakeDay = arg2.getRetryYn().compareTo(arg1.getRetryYn());
			}
		}
		return compareMakeDay;
	}

}
