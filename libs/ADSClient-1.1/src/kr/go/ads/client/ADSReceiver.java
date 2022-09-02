package kr.go.ads.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 *	ADSReceiver
 *	주소정보유통시스템에 접속하여, 필요한 데이터를 받기 위한 API 
 *	@author hyungho Kim
 *	@since 2015.01.23
 *	@version 1.0.0
 * 
 **/
public class ADSReceiver {
	private final String url="http://update.juso.go.kr/";
	private String file_path;
	private InputStream is;
	private HttpURLConnection hurlc;
	private boolean createDateDir = false;
	private String DirDatePattern = "YYYYMMDD";
	private int usingDate = ADSUtils.FILE_DATE;
	
	/**
	 *	주소정보를 요청하여, 해당 요청하는 변동자료를 송수신 받을 수 있다.
	 *	@param app_key 발급받은 승인키
	 *	@param date_gb 날짜구분 코드(D:일변동, M:월변동)
	 *	@param cntc_cd 자료요청 구분 코드 
	 *	@param retry_in 재반영 데이터 포함여부(Y:재반영 데이터 포함 제공, N:재반영 데이터 미제공)
	 *	@param req_date 요청일자 From(요청일자 설정시, 해당 일에 대한 정보만 제공 받음) : 일반적으로 NULL 이여야한다.
	 *	@param req_date_to 요청일자 To(요청기간동안의 데이터를 요청시 설정, 단 From~To 사이의 기간이 10일을 초과할 수 없음)
	 *	@return ReceiveData 
	 **/
	public ReceiveDatas receiveAddr(String app_key, String date_gb, String cntc_cd, String retry_in, String req_date, String req_date_to) throws Exception{
		int result = 0;
		String url_info = url + "updateInfo.do";
		ReceiveDatas receiveData = new ReceiveDatas();
		int paramCh = paramCheck(receiveData, app_key, date_gb, cntc_cd, retry_in, req_date, req_date_to);
		
		if(paramCh != 0){
			return receiveData;
		}
		
		if(req_date == null) req_date = "";
		if(req_date_to == null) req_date_to = "";
		
		String param= "cntc_cd="+cntc_cd+"&app_key="+app_key+"&date_gb="+date_gb+"&retry_in="+retry_in+"&req_dt="+req_date+"&req_dt2="+req_date_to;
		url_info += "?" + param;
		 
		
		try{
			hurlc = sendRequest(url_info);	
		}catch(Exception ex){
			receiveData.setResult(-1);
			return receiveData;
		}
		
		try{
			if(hurlc.getResponseCode() != 200){
				receiveData.setResult(hurlc.getResponseCode());
				return receiveData; 
			}	
		}catch(Exception ex){
			receiveData.setResult(-1);
			return receiveData;
		}
		
		
		int total_cnt = hurlc.getHeaderFieldInt("total_count", 0);  // 일반+재반영 수신파일 총 갯수
		int normal_cnt = hurlc.getHeaderFieldInt("normal_count", 0);  // 일반 수신파일 총 갯수
		int retry_cnt = hurlc.getHeaderFieldInt("retry_count", 0);  // 재반영 수신파일 총 갯수
		String err_code = hurlc.getHeaderField("err_code");	   		// 인증 응답 코드
		String app_yn = hurlc.getHeaderField("approval_yn");	    // 인증 승인 여부
		
		receiveData.setResTotalCnt(total_cnt);
		receiveData.setResNormalCnt(normal_cnt);
		receiveData.setResRetryCnt(retry_cnt);
		receiveData.setResCode(err_code);
		

		try{
			setInputStream(hurlc.getInputStream());	
		}catch(Exception ex){
			// 서버 응답 없는 경우 재시도 필요함
			receiveData.setResult(2000);
			return receiveData;
		}

		makeReceiveData(receiveData, total_cnt, true);
		
		return receiveData;
	}
	
	private void setInputStream(InputStream is){
		this.is = is;
	}
	
	/**
	 *	재반영 정보를 조회하거나, 재반영 데이터만을 제공받을 수 있다.
	 *
	 *	@param app_key 발급받은 승인키
	 *	@param date_gb 날짜구분 코드(D:일변동, M:월변동)
	 *	@param cntc_cd 자료요청 구분 코드
	 *	@return ReceiveData 
	 **/
	public ReceiveDatas receiveRetry(String app_key, String date_gb, String cntc_cd)  throws Exception{
		int result = 0;
		String url_info = url + "retryInfo.do";
		ReceiveDatas receiveData = new ReceiveDatas();
		int paramCh = paramCheck(receiveData, app_key, date_gb, cntc_cd, "Y", null,null);
		if(paramCh != 0){
			receiveData.setResult(paramCh);
			return receiveData;
		}
		String param= "cntc_cd="+cntc_cd+"&app_key="+app_key+"&date_gb="+date_gb+"&retry_in=Y";
		url_info += "?" + param;
		try{
			hurlc = sendRequest(url_info);	
		}catch(Exception ex){
			receiveData.setResult(2000);
			return receiveData;
		}
		
		if(hurlc == null){
			// 응답이 NULL 인경우 삭제
			return null;
		}

		int total_cnt = hurlc.getHeaderFieldInt("total_count", 0);  // 일반+재반영 수신파일 총 갯수
		int normal_cnt = hurlc.getHeaderFieldInt("normal_count", 0);  // 일반 수신파일 총 갯수
		int retry_cnt = hurlc.getHeaderFieldInt("retry_count", 0);  // 재반영 수신파일 총 갯수
		String err_code = hurlc.getHeaderField("err_code");	   		// 인증 응답 코드
		String app_yn = hurlc.getHeaderField("approval_yn");	    // 인증 승인 여부

		receiveData.setResTotalCnt(total_cnt);
		receiveData.setResNormalCnt(normal_cnt);
		receiveData.setResRetryCnt(retry_cnt);
		receiveData.setResCode(err_code);
		
		try{
			setInputStream(hurlc.getInputStream());	
		}catch(Exception ex){
			// 서버 응답 없는 경우 재시도 필요함 - 1
			receiveData.setResult(-1);
			return receiveData;
		}
		
		makeReceiveData(receiveData, total_cnt, true);	
		
		return receiveData;
	}
	
	
	private int paramCheck(ReceiveDatas receiveData, String app_key, String date_gb, String cntc_cd, String retry_in, String req_date, String req_date_to){
		int result = 0;
		receiveData.setReqAppKey(app_key);
		receiveData.setReqCntc(cntc_cd);
		receiveData.setReqDateGb(date_gb);
		receiveData.setReqDt(req_date);
		receiveData.setReqDtTo(req_date_to);
		receiveData.setResult(0);

		if(app_key == null){
			receiveData.setResult(1);
			receiveData.setResCode("E0001");
			result = -1;			// 승인키가 존재 하지 않는 경우, 에러코드 1
		}
		if(date_gb == null || "".equals(date_gb) || !("D".equals(date_gb) || "M".equals(date_gb) || "T".equals(date_gb) || "W".equals(date_gb))){
			receiveData.setResult(1006);
			receiveData.setResCode("E1006");
			result = -1;		// 날짜 구분값이 존재하지 않거나 다름
		}
		if(cntc_cd == null || "".equals(cntc_cd) || cntc_cd.length() != 6){
			receiveData.setResult(1002);
			receiveData.setResCode("E1002");
			result = -1;		// 요청자료 구분이 존재하지 않음
		}
		return result;
	}
	
	
	/**
	 *	요청후 응답을 정보를 수신한다. 
	 **/
	private HttpURLConnection sendRequest(String req_info) throws MalformedURLException, IOException{
		URL url = null;
		URLConnection urlCon = null;
		HttpURLConnection hurlc = null;
		
		url = new URL(req_info);
		urlCon = url.openConnection();
		hurlc = (HttpURLConnection)urlCon;
		hurlc.setRequestMethod("POST");
			
		return hurlc;
	}
	
	private String getFilePath(){
		if(this.file_path == null){
			this.file_path = "";
		}
		return this.file_path;
	}
	
	
	private void makeReceiveData(ReceiveDatas receiveData, int total_cnt, boolean sizeZeroMake) throws IOException{
		FileOutputStream fos = null;
		for(int i = 0; i<total_cnt; i++){
			byte[] meta_data = new byte[100];				    // 각 파일별 수신 정보 데이터 
			read(meta_data);
			String tmp_header = new String(meta_data);
			ReceiveData receive = new ReceiveData();
	  //-------------------- response 데이터 수신 정보(100바이트)------------------------
			int file_seq = Integer.parseInt(tmp_header.substring(0, 2)); // 수신받은 파일 순번
			String file_date = tmp_header.substring(2, 10); // 수신받은 파일의 기준일
			String file_name= tmp_header.substring(10, 60).trim();	// 수신받은 파일명
			String tmp_data_size = tmp_header.substring(60, 70);	// 파일 사이즈(byte)
			int data_size = Integer.parseInt(tmp_data_size);
			String err_cd = tmp_header.substring(70, 75);		// 각 파일당 응답코드
			String cntc_code = tmp_header.substring(75, 81);	// 제공받는 CNTC_CD 정보
			String retry_yn = tmp_header.substring(81, 82);		// 재반영데이터 여부
			String make_date = tmp_header.substring(82, 90);	// 파일 생성일자
			receive.setFileDate(file_date);
			receive.setFileName(file_name);
			receive.setFileSize(data_size);
			receive.setCntcCode(cntc_code);
			receive.setCreateDate(make_date);
			receive.setResCode(err_cd);
			receive.setRetryYn(retry_yn);
			receive.setFilePath(this.getFilePath());
			
			receiveData.add(receive);
			
	  //----------------------------------------------------------------------------
			byte[] body;
			if(data_size > 1024){
				body = new byte[1024];	 	  // 수신시 받을 데이터 단위 설정(기본 1024byte)
			}else{
				body = new byte[data_size];	   // 수신시 받을 데이터 단위 설정(이하 데이타 길이만큼)
			}
			
			if(file_name.equals("")) continue; // 파일명이 없는 경우, Skip
			
			File file = null;
			if(sizeZeroMake){
				file = new File(simpleMakeDir(make_date, file_date)+file_name);
				fos = new FileOutputStream(file);	
				receive.setFilePath(file.getAbsolutePath());
				if(data_size == 0) continue;	   // 데이터 사이즈가 0일 경우, 빈 파일만 생성 후 Skip	
			}else if(!sizeZeroMake && data_size == 0){
				continue;	   // 데이터 사이즈가 0일 경우, 빈 파일만 생성 후 Skip
			}else{
				// 해당 파일의 디렉토리를 생성한다.
				file = new File(simpleMakeDir(make_date, file_date)+file_name);
				fos = new FileOutputStream(file);
				receive.setFilePath(file.getAbsolutePath());
			}
			
			int tmp_size = 0;

			while((tmp_size = read(body)) > 0){
				data_size -= tmp_size;
				fos.write(body);				// File 저장
				fos.flush();
				if(data_size - tmp_size < 0){
					body = new byte[data_size];
				}
			}
		}
	}
	
	
	private String simpleMakeDir(String make_date, String file_date){
		String result = "";
		if(getCreateDateDirectory()){
			if(getUsingDate() == ADSUtils.MAKE_DATE){
				// 파일생성일 기준에 따라 디렉토리를 생성
				result = makeDir(ADSUtils.repalceDateFormat(make_date, getDatePattern()));
			}else{
				// 변동자료 기준일에 따라 디렉토리를 생성
				result = makeDir(ADSUtils.repalceDateFormat(file_date, getDatePattern()));
			}
		}
		return result;
	}
	
	private String makeDir(String dir_path){
		String total_path = "";
		try{
			new File(getFilePath()+"/"+dir_path).mkdir();
			total_path = getFilePath()+"/"+dir_path;
		}catch(Exception ex){
			ex.printStackTrace();
			total_path = getFilePath();
		}
		if(!total_path.endsWith("/")){
			total_path += "/";
		}
		return total_path;
	}
	
	private int read(byte[] data) throws IOException {
		return read(data, 0, data.length);
	}

	private int read(byte[] data, int offset, int length) throws IOException {
		int received = 0;
		int maxcount = 0;
		int recv = 0;
		int _timeout = 1000;
		while((maxcount += 10) <= _timeout){
			recv = is.read(data, offset+received, length-received);
			if(recv > 0){
				received += recv;
			}
			if(received >= length) break;
			try{ Thread.sleep(10); } catch (Exception ignored){}
		}
		if(received < length) return -1;
		return received;
	}
		
	/**
	* 	수신받은 정보를 저장할 경로를 지정한다. 경로는 '/'로 끝나는 문자열입니다.
	* 	만약 입력한 filePath값이 /kais/update 일 경우 /kais/update/로 적용합니다.
	*	@param filePath 저장할 로컬 파일 경로
	**/
	public void setFilePath(String filePath){
		if(filePath.endsWith("/")){
			this.file_path = filePath;			
		}else{
			this.file_path = filePath+"/";	
		}
	}
	
	private String getDatePattern(){
		return this.DirDatePattern;
	}
	
	/**
	* 	날짜형식의 디렉토리 생성하는지 옵션 선택
	*	@param datePattern 날짜형식(YYYYMMDD, YYMMDD, YYYYY, MMDD)
	**/	
	public void setCreateDateDirectory(String datePattern){
		this.createDateDir = true;
		this.DirDatePattern = datePattern;
	}
	
	private boolean getCreateDateDirectory(){
		return this.createDateDir;
	}
	
	/**
	* 	신규 디렉토리 생성하는지 옵션 선택
	*	@param usingDatePattern ADSUtils.MAKE_DATE : 파일생성일 기준, ADSUtils.FILE_DATE : 데이터 변동일자 기준  
	*	
	**/	
	public void setUsingDate(int usingDatePattern){
		this.usingDate = usingDatePattern;
	}
	
	private int getUsingDate(){
		return this.usingDate;
	}
	
	
		
}
