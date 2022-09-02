package kr.go.ads.client;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ADSUtils {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public static final String YYYYMMDD = "YYYYMMDD";
	public static final String YYMMDD = "YYMMDD";
	public static final String YYYYMM = "YYYYMM";
	public static final String YYMM = "YYMM";
	public static final String YYYY = "YYYY";
	
	public static final int MAKE_ASC = 0;
	public static final int MAKE_DESC = 1;
	public static final int FILE_ASC = 2;
	public static final int UPDATE_ASC = 3;
	
	
	public static final int MAKE_DATE = 0;
	public static final int FILE_DATE = 1;
	
	
    public static String getYesterdayWithFormat(String format){
    	Calendar cal;
    	SimpleDateFormat dateFormat;
    	cal = Calendar.getInstance();
    	
    	dateFormat = new SimpleDateFormat(format);
    	cal.add(cal.DATE, -1);
    	
    	return dateFormat.format(cal.getTime());
    }
    
    
    /**
     *	기준일자 이후 +1 에 대한 날짜 정보를 반환한다. 
     **/
    public static String getTomorrowDate(String date, int term){
    	Calendar cal;
    	SimpleDateFormat dateFormat;
    	cal = Calendar.getInstance();
    	dateFormat = new SimpleDateFormat("yyyyMMdd");
    	if(date.length() == 6 && date.startsWith("20")){
    		// YYYYMM 데이트 형식이다.
    		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        	cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)));
        	cal.set(Calendar.DATE, 1);
    	}else if(date.length() == 8){
    		// YYYYMMDD 데이트 형식이다.
    		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        	cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6))-1);
        	cal.set(Calendar.DATE, Integer.parseInt(date.substring(6, 8)));
        	cal.add(cal.DATE, term);
    	}else if(date.length() == 6){
    		// YYMMDD 데이트 형식이다.
    		cal.set(Calendar.YEAR, 2000+Integer.parseInt(date.substring(0, 2)));
        	cal.set(Calendar.MONTH, Integer.parseInt(date.substring(2, 4)));
        	cal.set(Calendar.DATE, Integer.parseInt(date.substring(4, 6)));
        	cal.add(cal.DATE, term);
    	}
    	
    	return dateFormat.format(cal.getTime());
    }

    
	public static String getYYYYMM()
	{
		String result = "";
		Calendar c  = Calendar.getInstance(); 
	    SimpleDateFormat sf = new SimpleDateFormat("yyyyMM"); 
	    
	    result = sf.format(c.getTime());
		
        return result;
	}
	
	
	public static String getYYYYMMDD(){
		String result = "";
		Calendar c  = Calendar.getInstance(); 
	    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd"); 
	    
	    result = sf.format(c.getTime());
		
        return result;		
	}
	
	public static String repalceDateFormat(String date_format){

		String result = date_format;
		Calendar c  = Calendar.getInstance();
	    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd"); 
	    
	    String today = sf.format(c.getTime());
	    
	    result = result.replaceAll("YYYY", today.substring(0,4));
	    result = result.replaceAll("yyyy", today.substring(0,4));
	    result = result.replaceAll("YY", today.substring(2,4));
	    result = result.replaceAll("yy", today.substring(2,4));
	    result = result.replaceAll("MM", today.substring(4,6));
	    result = result.replaceAll("DD", today.substring(6));
	    result = result.replaceAll("dd", today.substring(6));
		
        return result;		
	}
	
	public static String repalceDateFormatPath(String date_path, String work_date){

		String result = date_path;
		Calendar c  = Calendar.getInstance();
	    String path_date = getTomorrowDate(work_date, 1);
	    
	    result = result.replaceAll("YYYY", path_date.substring(0,4));
	    result = result.replaceAll("yyyy", path_date.substring(0,4));
	    result = result.replaceAll("YY", path_date.substring(2,4));
	    result = result.replaceAll("yy", path_date.substring(2,4));
	    result = result.replaceAll("MM", path_date.substring(4,6));
	    result = result.replaceAll("DD", path_date.substring(6));
	    result = result.replaceAll("dd", path_date.substring(6));
		
        return result;		
	}
	
	
	public static String repalceDateFormat(String date, String date_format){
		String result = date_format;
		
		String today = date;
	    
		if(today.length() == 6 && date.startsWith("20")){
			result = result.replaceAll("YYYY", today.substring(0,4));
			result = result.replaceAll("yyyy", today.substring(0,4));
		    result = result.replaceAll("YY", today.substring(2,4));
		    result = result.replaceAll("yy", today.substring(2,4));
		    result = result.replaceAll("MM", today.substring(4,6));
		    result = result.replaceAll("DD", "01");
		    result = result.replaceAll("dd", "01");
		}else if(today.length() == 8){
			result = result.replaceAll("YYYY", today.substring(0,4));
			result = result.replaceAll("yyyy", today.substring(0,4));
		    result = result.replaceAll("YY", today.substring(2,4));
		    result = result.replaceAll("yy", today.substring(2,4));
		    result = result.replaceAll("MM", today.substring(4,6));
		    result = result.replaceAll("DD", today.substring(6));
		    result = result.replaceAll("dd", today.substring(6));
		}else if (today.length() == 6){
			result = result.replaceAll("YYYY", "20"+today.substring(0,2));
			result = result.replaceAll("yyyy", "20"+today.substring(0,2));
		    result = result.replaceAll("YY", today.substring(0,2));
		    result = result.replaceAll("yy", today.substring(0,2));
		    result = result.replaceAll("MM", today.substring(2,4));
		    result = result.replaceAll("DD", today.substring(4));
		    result = result.replaceAll("dd", today.substring(4));
		}else{
			result = result.replaceAll("YYYYMMDD", today);
			result = result.replaceAll("yyyyMMdd", today);
			result = result.replaceAll("YYMMDD", today);
			result = result.replaceAll("yyMMdd", today);
		}
	    
        return result;		
	}

	
	public static String getHHMMSS()
	{
		String result = "";
		Calendar c  = Calendar.getInstance(); 
	    SimpleDateFormat sf = new SimpleDateFormat("HHmmss");
	    
	    result = sf.format(c.getTime());
	    
	    return result;
	}
	
    public static String getLastYYYYMM()
	{
		int lastYear = 0;
		String lastYYYYMM;
		String result = "";
		Calendar c  = Calendar.getInstance(); 
	    SimpleDateFormat sf = new SimpleDateFormat("yyyyMM"); 
	    
	    result = sf.format(c.getTime());
	    
		String YYYY = result.substring(0, 4);
		lastYear = (int)Integer.parseInt(YYYY);
		
		lastYear = lastYear - 1;
		
		lastYYYYMM = lastYear + result.substring(4, 6);
        
        return lastYYYYMM;
	}
 
}
