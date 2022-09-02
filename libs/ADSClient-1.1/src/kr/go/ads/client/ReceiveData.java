package kr.go.ads.client;

import java.io.Serializable;


/**
 *	ReceiveData
 *	주소정보유통시스템을 통해 제공받은 데이터 정보 
 **/

public class ReceiveData implements Serializable {

	private static final long serialVersionUID = 8743092208345922471L;
	//--- receive information ------//
	private String filePath;	/* 파일 경로 */
	private String fileName;	/* 파일명 */
	private int fileSize;		/* 데이터 사이즈 */
	private String fileDate;	/* 파일 기준일 */
	private String createDate;	/* 데이터 생성일 */
	private String retryYn;	/* 재반영 데이터 여부*/
	private String cntcCode;	/* 자료 구분코드 */
	private String resCode;		/* 파일별 응답코드 */
	private String resMsg;		/* 파일별 응답 메시지 */
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getRetryYn() {
		if(retryYn == null){
			retryYn = "N";
		}
		return retryYn;
	}
	public void setRetryYn(String retryYn) {
		this.retryYn = retryYn;
	}
	public String getCntcCode() {
		return cntcCode;
	}
	public void setCntcCode(String cntcCode) {
		this.cntcCode = cntcCode;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
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
	
	
}
