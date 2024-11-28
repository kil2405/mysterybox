package com.weplaylabs.mysterybox.common;

public class ConstantVal {
    // Login result Type
	public static final int LOGIN_TYPE_SUCCESS = 100;						// 성공
	public static final int LOGIN_TYPE_NOT_FOUND_USER = 101;				// 유저를 찾을 수 없음
	public static final int LOGIN_TYPE_BLOCK = 102;							// 차단된 계정
	
	// Market Type
	public static final byte MARKET_TYPE_AOS = 0;							// Google - PlayStore
	public static final byte MARKET_TYPE_IOS = 1;							// Apple - GameCenter

    // Default
	public static final byte DEFAULT_ZERO = 0;
	public static final byte DEFAULT_VALUE = -1;
	public static final byte DEFAULT_ONE = 1;
	public static final byte DEFAULT_SUCCESS = 100;
	public static final byte DEFAULT_FAIL = 101;

	//Version Check Type
	public static final byte VERSION_CHECK_OK = 0;
	public static final byte VERSION_CHECK_NO_SESSION = 1;
	public static final byte VERSION_CHECK_SERVER_INSPECTION = 2;

    // Day
	public static final int ALWAYS = 0;
	public static final int SUNDAY = 1;
	public static final int MONDAY = 2;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
	public static final int THURSDAY = 5;
	public static final int FRIDAY = 6;
	public static final int SATURDAY = 7;
	public static final int DAY_WEEK_COUNT = 7;

    // Time
	public static final int CALIBRATION_TIME = 5;										// 시간보정 : 5초
	public static final int MINUTE_OF_SECOND = 60;										// 1분
	public static final byte HOUR_OF_DAY = 24;											// 하루 24시간
	public static final int HOUR_OF_SECOND = 3600;										// 1시간(초)
	public static final int DAY_OF_SECOND = 86400;										// 하루(초)
	public static final int DAY_OF_MINUTE = 1440;										// 하루(분)
	public static final int WEEK_OF_SECOND = DAY_OF_SECOND * 7;							// 1주일(초)
	public static final int MINUTE_OF_HOUR = 60;										// 1시간(분)
	public static final int DAY_OF_MONTH = 30;											// 1달(일)
	public static final long MONTH_OF_MINUTE = (MINUTE_OF_HOUR * HOUR_OF_DAY * 31); 	// 1달(분)
	public static final long MAIL_EXPIRE_TIME = WEEK_OF_SECOND * 4;						// 2주일(초)
	public static final String MAIL_CASH_EXPIRE_TIME = "2099-12-31 00:00:00";			// 영구 캐쉬메일 만료시간

    // User Type
	public static final byte USER_GRADE_OPERATOR = 0;
	public static final byte USER_GRADE_NORMAL = 1;
	public static final byte USER_GRADE_BLOCK = 2;

    // Valid Type
	public static final byte DISABLE = 0;
	public static final byte ENABLE = 1;
	public static final byte DISABLE_BAN = 2;
	public static final byte IS_FALSE = 0;
	public static final byte IS_TRUE = 1;

    // date division
	public static final byte DATE_SECTION_YEAR = 1;
	public static final byte DATE_SECTION_MONTH = 2;
	public static final byte DATE_SECTION_DAY = 3;
	public static final byte DATE_SECTION_HOUR = 4;
	public static final byte DATE_SECTION_HOUR_FULL = 5;
	public static final byte DATE_SECTION_MINUTE = 6;

	// Tutorial Reward Item
	public static final int TUTORIAL_REWARD_ITEM = 20000;

	// Click Max Count
	public static final int CLICK_MAX_COUNT = 650;

	// Error Code
	public static final int ERROR_CDOE_FILTER_1000 = 1000;  // Application Update
	public static final int ERROR_CDOE_FILTER_1001 = 1001;  // 서버 점검
	public static final int ERROR_CDOE_FILTER_1002 = 1002;  // 중복 접속
	public static final int ERROR_CDOE_FILTER_1003 = 1003;  // 세션 만료
	public static final int ERROR_CDOE_FILTER_1004 = 1004;  // 시스템 에러가 발생했습니다. 잠시 후 다시 시도해 주세요.
	public static final int ERROR_CDOE_USER_1005 = 1005;  	// 같은 닉네임이 있습니다.
	public static final int ERROR_CDOE_USER_1006 = 1006; 	// 1글자 이상 12자 이하로 입력해주세요.
}
