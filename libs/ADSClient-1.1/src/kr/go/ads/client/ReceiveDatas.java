package kr.go.ads.client;
import java.util.ArrayList;
import java.util.Collections;

import kr.go.ads.compare.FileAscCompare;
import kr.go.ads.compare.MakeAscCompare;
import kr.go.ads.compare.UpdateAscCompare;


/**
 *	ReceiveDatas
 *	주소정보유통시스템을 통해 제공받은 데이터 정보 리스트 
 **/

public class ReceiveDatas {

	//--- request information ------//
	private String reqCntc;
	private String reqAppKey;
	private String reqDateGb;
	private String reqRetryIn;
	private String reqDt;
	private String reqDtTo;
	private int result;
	
	//--- receive information ------//
	private int resTotalCnt;	/* 응답받은 전체 데이터 건수 */
	private int resNormalCnt;	/* 응답받은 변동자료 데이터 건수 */
	private int resRetryCnt;	/* 응답받은 재반영 데이터 건수 */
	private String resCode="E1999";		/* 응답코드 */
	
	private ArrayList<ReceiveData> list;
	
	public ReceiveDatas(){
		if(list == null){
			list = new ArrayList();
		}
	}
	
	public String getReqCntc() {
		return reqCntc;
	}
	public void setReqCntc(String reqCntc) {
		this.reqCntc = reqCntc;
	}
	public String getReqAppKey() {
		return reqAppKey;
	}
	public void setReqAppKey(String reqAppKey) {
		this.reqAppKey = reqAppKey;
	}
	public String getReqDateGb() {
		return reqDateGb;
	}
	public void setReqDateGb(String reqDateGb) {
		this.reqDateGb = reqDateGb;
	}
	public String getReqRetryIn() {
		return reqRetryIn;
	}
	public void setReqRetryIn(String reqRetryIn) {
		this.reqRetryIn = reqRetryIn;
	}
	public String getReqDt() {
		return reqDt;
	}
	public void setReqDt(String reqDt) {
		this.reqDt = reqDt;
	}
	public String getReqDtTo() {
		return reqDtTo;
	}
	public void setReqDtTo(String reqDtTo) {
		this.reqDtTo = reqDtTo;
	}
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	public int getResTotalCnt() {
		return resTotalCnt;
	}

	public void setResTotalCnt(int resTotalCnt) {
		this.resTotalCnt = resTotalCnt;
	}

	public int getResNormalCnt() {
		return resNormalCnt;
	}

	public void setResNormalCnt(int resNormalCnt) {
		this.resNormalCnt = resNormalCnt;
	}

	public int getResRetryCnt() {
		return resRetryCnt;
	}

	public void setResRetryCnt(int resRetryCnt) {
		this.resRetryCnt = resRetryCnt;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public void add(ReceiveData receiveData){
		list.add(receiveData);
	}

	public ArrayList<ReceiveData> getReceiveDatas(){
		return (ArrayList<ReceiveData>)list;
	}
	
	public String getResMsg(){
		String msg="";
		if("P0000".equals(this.resCode)){
			msg = "정상 반영";
		}else if("P1000".equals(this.resCode)){
			msg = "최신 상태입니다. 대상 파일이 없습니다.";
		}else if("E0001".equals(this.resCode)){
			msg = "승인키를 확인하세요.(승인키 정보 오류)";
		}else if("E0002".equals(this.resCode)){
			msg = "승인되지 않은 사이트입니다.";
		}else if("E0003".equals(this.resCode)){
			msg = "정상적인 경로로 접속하시기 바랍니다.";
		}else if("E0004".equals(this.resCode)){
			msg = "업데이트서버 신청이 필요합니다.";
		}else if("E0005".equals(this.resCode)){
			msg = "서버의 응답이 없습니다. 잠시후 시도해 주세요.";
		}else if("E1001".equals(this.resCode)){
			msg = "해당 파일이 없습니다.";
		}else if("E1002".equals(this.resCode)){
			msg = "요청자료 구분코드가 필요합니다.";
		}else if("E1003".equals(this.resCode)){
			msg = "승인키가 필요합니다.(승인키 입력누락)";
		}else if("E1004".equals(this.resCode)){
			msg = "요청기간이 최대 기일을 초과 하였습니다.(MAX 10일)";
		}else if("E1005".equals(this.resCode)){
			msg = "시스템 에러 : 응답메시지 생성 오류";
		}else if("E1006".equals(this.resCode)){
			msg = "월, 일 변동구분 값을 확인하세요.";
		}else if("E1007".equals(this.resCode)){
			msg = "요청일자는 YYYYMMDD 형식입니다.";
		}else if("E1008".equals(this.resCode)){
			msg = "제공할 데이터가 없습니다. 제공할 파라미터가 등록되어있지 않습니다.";
		}else if("P1999".equals(this.resCode)){
			msg = "관리자에게 문의하세요.(1588-0061)";
		}else{
			msg = "관리자에게 문의하세요.(1588-0061)";
		}
		
		return msg;
	}
	
	/**
	 *	@param order 다운로드 받은 파일을 정렬 방법
	 *		   ADSUtils.MAKE_ASC : 파일 생성일 기준 오름차순 정렬
	 *		   ADSUtils.FILE_ASC : 파일데이터의 기준일자 오름차순 정렬
	 *		   ADSUtils.UPDATE_ASC : 반영시 순서 기준 오름차순 정렬
	 *
	 *	@return ArrayList order순서에 의해서 정렬된 다운로드 데이터 정보 list
	 **/
	public ArrayList<ReceiveData> getReceiveDatas(int order){
		if(order == ADSUtils.MAKE_ASC){
			Collections.sort(list, new MakeAscCompare());
		}else if(order == ADSUtils.FILE_ASC){
			Collections.sort(list, new FileAscCompare());
		}else if(order == ADSUtils.UPDATE_ASC){
			Collections.sort(list, new UpdateAscCompare());
		}
		
		return (ArrayList<ReceiveData>)list;
	}
}
