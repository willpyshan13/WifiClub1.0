/**
 * Project Name: itee
 * File Name:  Constants.java
 * Package Name: cn.situne.itee.constant
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.common.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName:Constants <br/>
 * Function: Constants of this app. <br/>
 * Date: 2015-01-15 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings("UnusedDeclaration")
public class Constants {



    public static final String REQUEST_TAG = "iTee";

    //     beta
//    public static final String BASE_API_URL_BETA ="http://www.situne.cn/repos/branches/develop_11/api/index.php/";

    public static final String BASE_API_URL_BETA ="http://apiold.wificlub.com/index.php/";

           // "http://www.situne.cn/repos/branches/beta/api/index.php/";

    public static final int SECOND_60 = 60;
    public static final String CHANNEL_ID_BETA = "Situne";

    //     release
    public static final String BASE_API_URL_RELEASE = "http://apiold.wificlub.com/index.php/";
    public static final String CHANNEL_ID_RELEASE = "Release";

    //     develop
    public static final String BASE_API_URL_DEVELOP = "http://apiold.wificlub.com/index.php/";
    public static final String CHANNEL_ID_DEVELOP = "Develop";

    public static final String CHECK_VERSION_URL = "http://apiold.wificlub.com/version.php";
    public static final String APP_TAG = "iTee";

    public static final String PHOTO_DEFAULT_URL = "http://apiold.wificlub.com/Public/default/default.png";
    public static final String PHOTO_DEFAULT_PROMOTE_URL = "http://apiold.wificlub.com/Public/default/default.png";
    public static final String PHOTO_DEFAULT_LOADING_URL = "http://apiold.wificlub.com/Public/default/5-121204193R0-50.gif";
//    public static final String PHOTO_DEFAULT_LOADING_URL = "http://www.lanrentuku.com/img/allimg/1212/5-121204193R0-50.gif";

    public static final int DESIGN_UI_WIDTH = 720;
    public static final int DESIGN_UI_HEIGHT = 1280;

    public static final int SHOPS_DATA_STATUS_1 = 1;
    public static final int SHOPS_DATA_STATUS_0 = 0;

    public static final int SHOPS_PACKAGE_TYPE_1 = 1;
    public static final int SHOPS_PACKAGE_TYPE_2 = 2;
    public static final String SHOPS_BOOKING_PAY_STATUS_1 = "1";
    public static final String SHOPS_BOOKING_PAY_STATUS_0 = "0";


    public static final int SHOPS_BOOKING_FLAG_1 = 1;
    public static final int SHOPS_BOOKING_FLAG_2 = 2;

    public static final String SHOPPING_MANAGER_DISCOUNT_TYPE_MONEY = "0";
    public static final String SHOPPING_MANAGER_DISCOUNT_TYPE_PERCENT = "1";

    public static final String CHECK_YES = "1";
    public static final String CHECK_NO = "0";


    public static final String PRICING_TYPE_1 = "1";
    public static final String PRICING_TYPE_2 = "2";
    public static final String PRICING_TYPE_3 = "3";
    public static final String PRICING_TYPE_4 = "4";



    public static final String UPLOAD_TYPE_MEMBER = "1";
    public static final String UPLOAD_TYPE_STAFF = "2";
    public static final String UPLOAD_TYPE_PROMOTE = "3";
    public static final String UPLOAD_TYPE_SIGN = "4";

    public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYYMMDD_OBLIQUE = "yyyy/MM/dd";
    public static final String DATE_FORMAT_YYYYMM = "yyyy-MM";

    public static final String TIME_STAMP_FORMAT_YYYYMMDD_HHMMSSSSS = "yyyyMMddHHmmssSSS";

    public static final String TIME_FORMAT_HHMMSS = "HH:mm:ss";
    public static final String TIME_FORMAT_HHMM = "HH:mm";
    public static final String TIME_DEFAULT = "00:00";
    public static final String TIME_ADD_SS = ":00";

    public static final String STRING_FORMAT_02D_02D = "%02d:%02d";
    public static final String TIME_FORMAT_DHD = "%dh%d";
    public static final String CADDIES_NO_FORMAT = "%03d";

    public static final String STR_COMMA = ",";
    public static final String STR_SEPARATOR = "-";
    public static final String STR_SEPARATOR_SPACE = " - ";
    public static final String STR_EMPTY = StringUtils.EMPTY;
    public static final String STR_POUND_SIGN = "#";
    public static final String STR_SPACE = " ";
    public static final String STR_DOUBLE_SPACE = "  ";
    public static final String STR_SYMBOL_PERCENT = "%";
    public static final String STR_SECOND = "s";
    public static final String STR_QUESTION = "?";
    public static final String STR_DOT = ".";
    public static final String STR_SLASH = "/";
    public static final String STR_COLON = ":";
    public static final String STR_COLON_WITH_SPACE = " : ";
    public static final String STR_H = "h";
    public static final String STR_OFF = "OFF";
    public static final String STR_PER_CENT = "%";
    public static final String STR_MINUS_MONEY = " -";
    public static final String STR_ITEM_SELECTED = "√";
    public static final String STR_WAVE = "~";
    public static final String STR_AND = "&";
    public static final String STR_MULTIPLY = "×";
    public static final String STR_PLUS = "+";

    public static final String STR_GREEN_ID_SEPARATOR = "%_-1@#&";

    public static final String STR_0 = "0";
    public static final String STR_1 = "1";
    public static final String STR_2 = "2";

    public static final String STR_FLAG_YES = "1";
    public static final String STR_FLAG_NO = "0";

    public static final String STR_MINUS_1 = "-1";

    public static final String STR_AGENT_LOGIN = "G";

    public static final String SEGMENT_TIME_NORMAL_ID = "0";
    public static final String SEGMENT_TIME_TWO_TEE_START_ID = "1";
    public static final String SEGMENT_TIME_NINE_HOLES_ONLY_ID = "2";
    public static final String SEGMENT_TIME_BLOCK_TIMES_ID = "3";
    public static final String SEGMENT_TIME_MEMBER_ONLY_ID = "4";
    public static final String SEGMENT_TIME_THREE_TEE_START_ID = "5";
    public static final String SEGMENT_TIME_PRIME_TIME_ID = "6";
    public static final String SEGMENT_TIME_EVENT_TIME_ID = "7";
    public static final String SEGMENT_TIME_PRIME_DISCOUNT_ID = "8";
    public static final String SEGMENT_TIME_TRANSFER_TIME_BLOCK_ID = "13";

    public static final String SEGMENT_TIME_TWO_TEE_START = "Two tee start";
    public static final String SEGMENT_TIME_NINE_HOLES_ONLY = "9 holes only";
    public static final String SEGMENT_TIME_BLOCK_TIMES = "Block times";
    public static final String SEGMENT_TIME_MEMBER_ONLY = "Member only";
    public static final String SEGMENT_TIME_THREE_TEE_START = "Three tee start";
    public static final String SEGMENT_TIME_PRIME_TIME = "Prime time";

    public static final String SEGMENT_TIME_LIFE_MEMBER = "life member";
    public static final String SEGMENT_TIME_FAMILY_MEMBER = "family member";

    public static final String DEFAULT_DISCOUNT_PERCENT = "0";
    public static final String DEFAULT_SALES_TAX = "20";
    public static final String UNIT_METERS = "meters";
    public static final String UNIT_YARDS = "yards";
    public static final String FIRST_DAY_SUN = "SUN";
    public static final String FIRST_DAY_MON = "MON";
    public static final String TIME_HOUR_LOWERCASE = "h";
    public static final String TIME_HOUR_UPPERCASE = "H";
    public static final String TIME_MIN_UNIT = "Min";


    public static final String DEFAULT_LOGOUT_TIME_HOUR = "0";
    public static final String DEFAULT_LOGOUT_TIME_MIN = "30";
    public static final String DEFAULT_GAP_TIME = "8";

    public static final int MAX_PERCENT_VALUE = 100;
    public static final int MAX_CADDIE_NUMBER_VALUE = 999;

    public static final int SWITCH_LEFT = 0;
    public static final int SWITCH_RIGHT = 1;

    public static final String UNLIMITED_FLAG_OFF = "0";
    public static final String UNLIMITED_FLAG_ON = "1";

    public static final String MONEY_DISCOUNT_MONEY = "2";
    public static final String MONEY_DISCOUNT_PERCENT = "1";

    public static final int WIDTH_RIGHT_SHOW_VIEW = 300;
    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String EMAIL_TEXT_TYPE = "plain/text";
    public static final String EMAIL_HTML_TYPE = "text/html";
    public static final int SOCKET_TIMEOUT = 30000;

    public static final String SHOP_TYPE_RENTAL = "10001";
    public static final String SHOP_TYPE_PROMOTE = "10002";
    public static final String SHOP_TYPE_PACKAGE = "10003";

    public static final String COUNTRY_CHINA = "China";

    public static final String CUSTOMER_NON_MEMBER = "2";
    public static final String CUSTOMER_MEMBER = "1";
    public static final Integer CHECK_IN = 0;
    public static final String FEMALE = "F";
    public static final String MALE = "M";
    public static final String HOLE_9_VALUE = "9";

    public static final String STR_SQUARE_BRACKETS_START = "[";
    public static final String STR_SQUARE_BRACKETS_END = "]";

    //PaymentType
    public static final String PAYMENT_PATTERN_CASH = "1";   //现金
    public static final String PAYMENT_PATTERN_VOUCHERS = "2";  //抵用券
    public static final String PAYMENT_PATTERN_CREDIT_CARD = "3";   //信用卡
    public static final String PAYMENT_PATTERN_THIRD_PARTY = "4";   //第三方
    public static final String PAYMENT_PATTERN_BALANCE_ACCOUNT = "5"; //冲预收
    public static final String PAYMENT_PATTERN_BANK_TRANSFER = "6";     //银行转账？

//    getString(R.string.paytype_cash),
//    getString(R.string.paytype_card),
//    getString(R.string.paytype_balance),
//    getString(R.string.paytype_zhifubao),
//    getString(R.string.paytype_wechat),
//    getString(R.string.paytype_chongyushou),
//    getString(R.string.paytype_sign)
    public static final String PAYTYPE_CASH = "1";   //现金
//    public static final String PAYTYPE_VOUCHERS = "2";  //抵用券、代金券
    public static final String PAYTYPE_CARD = "3";   //银行卡  目前银行卡、信用卡未单独区分，这里预留。暂用同一编号
    public static final String PAYTYPE_CREDIT_CARD = "3";   //信用卡
    public static final String PAYTYPE_WECHAT = "4";   //微信
    public static final String PAYTYPE_BALANCE = "5"; //冲预收
    public static final String PAYTYPE_TRANSFER = "6";     //银行转账
//    public static final String PAYTYPE_SING = "7";     //
    public static final String PAYTYPE_CHEQUE = "8";     //支票
    public static final String PAYTYPE_SING = "9";     //
    public static final String PAYTYPE_SING_COMPANY = "21";     //
    public static final String PAYTYPE_SING_GUDONG = "22";     //
    public static final String PAYTYPE_ZHIFUBAO = "10";     //支票

    public static final String STR_LINE_BREAK = "\r\n";
    public static final String STR_HTML_LINE_BREAK = "<br>";
    public static final String STR_MINUS = "-";
    public static final String CORRECT_FORMAT_MOBILE = "^\\+?\\d{5,15}$";
    public static final String CORRECT_FORMAT_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    public static final String CORRECT_FORMAT_CADDIE_LEVEL = "[(A-Za-z0-9)]+";
    public static final int MINUTES_PER_HOUR = 60;
    public static final int MAX_AGE = 200;
    public static final double MAX_TAX = 100.00;
    public static final int MAX_MINUTE = 59;
    public static final int MAX_PERCENT = 100;
    public static final int MENU_ID_USER_NAME = 100;
    public static final int MENU_ID_TEE_TIMES = 101;
    public static final int MENU_ID_EVENTS = 102;
    public static final int MENU_ID_SHOPS = 103;
    public static final int MENU_ID_CUSTOMERS = 104;
    public static final int MENU_ID_AGENTS = 105;
    public static final int MENU_ID_STAFF = 106;
    public static final int MENU_ID_NEWS = 107;
    public static final int MENU_ID_ADMINISTRATION = 108;
    public static final int MENU_ID_SCAN_QR_CODE = 109;
    public static final int MENU_ID_CHARTS = 110;

    public static final String KEY_SP_IDENTIFIER = "itee";
    public static final String KEY_SP_LOGIN_INFO = "LoginInfo";
    public static final String KEY_SP_LOGIN_USER_NAME = "LoginUserName";
    public static final String KEY_SP_SHOPPING_CART = "ShoppingCart";

    // the constant to judge green_fee_more
    public static final String GREEN_FEE_PLAY_MORE = "Play more";
    /* 登录 */
    // 登录成功
    public static final int RETURN_CODE_LOGIN_SUCCESS = 20101;
    // 超时
    public static final int RETURN_CODE_LOGIN_FAILED_TIME_OUT = 20124;
    // 缺少参数
    public static final int RETURN_CODE_LOGIN_FAILED_NEED_MORE_PARAM = 20111;
    // 用户不存在
    public static final int RETURN_CODE_LOGIN_FAILED_USER_NOT_EXIST = 20100;
    // 密码错误
    public static final int RETURN_CODE_LOGIN_FAILED_INCORRECT_PWD = 20102;
    // 临时密码失效
    public static final int RETURN_CODE_LOGIN_FAILED_INVALID_TEMP_PWD = 20110;
    //忘记密码
    //用户存在
    public static final int RETURN_CODE_FORGOT_PASSWORD_USER_EXIST = 20106;
    //用户不存在
    public static final int RETURN_CODE_FORGOT_PASSWORD_USER_NOT_EXIST = 20100;
    /*  修改密码  */
    // 密码修改成功
    public static final int RETURN_CODE_CHANGE_PASSWORD_SUCCESS = 20103;
    // 与旧密码一致
    public static final int RETURN_CODE_CHANGE_PWD_FAILED_SAME_WITH_OLD_PWD = 20104;
    // 密码不符合要求
    public static final int RETURN_CODE_CHANGE_PWD_FAILED_NOT_CONFIRM_TO_RULE = 20105;
    // 旧密码不对
    public static final int RETURN_CODE_CHANGE_PWD_FAILED_INCORRECT_OLD_PWD = 20107;

    public static final int RETURN_CODE_20201_MODIFY_SUCCESSFULLY = 20201;
    /*    Return successfully*/
    public static final int RETURN_CODE_20301 = 20301;
    /*    Return successfully no data*/
    public static final int RETURN_CODE_20302 = 20302;
    public static final int RETURN_CODE_20401_ADD_SUCCESSFULLY = 20401;
    public static final int RETURN_CODE_20112_RECHARGE_SUCCESSFULLY = 20112;
    public static final int RETURN_CODE_20403_DELETE_SUCCESSFULLY = 20403;
    public static final int RETURN_CODE_20118_PAY_SUCCESSFULLY = 20118;
    public static final int RETURN_CODE_20125_CHECK_OUT_SUCCEEDED = 20125;
    public static final int RETURN_CODE_20127_LOW_STOCK = 20127;
    public static final int RETURN_CODE_20137_CURRENT_DATE_HAS_BOOKED = 20137;
    public static final int RETURN_CODE_20148_COURSE_HOLE = 20148;
    public static final int RETURN_CODE_20147_COURSE_HOLE = 20147;
    public static final int RETURN_CODE_20149_COURSE_HOLE = 20149;
    /* course area lock status  */
    public static final int COURSE_AREA_LOCK_STATUS_LOCKED = 1;
    public static final int COURSE_AREA_LOCK_STATUS_UNLOCKED = 0;
    public static final int AUTHORITY_STATUS_YES = 1;
    public static final int AUTHORITY_STATUS_NO = 0;
    public static final String FILE_NAME_LOGO = "logo.png";
    public static final int CADDIE_DEPARTMENT_ID = 1;
    public static final String STAFF_MANAGER = "M";
    public static final String AGENTS_DEFAULT_START_TIME = "00:00";
    public static final String AGENTS_DEFAULT_END_TIME = "23:59";
    public static final String CURRENCY_ID_CN = "2";
    public static final String CURRENCY_ID_US = "3";
    public static final String CURRENCY_ID_EU = "1";
    public static final String CURRENCY_ID_FR = "4";
    public static final String CURRENCY_CN = "¥";
    public static final String CURRENCY_US = "$";
    public static final String CURRENCY_EU = "€";
    public static final String CURRENCY_FR = "£";
    public static final String CURRENCY_M = "m";
    public static final String CURRENCY_DAY = "day";
    public static final int DEFAULT_SHOP_ID_PROMOTE = 1;
    public static final int DEFAULT_SHOP_ID_RENTAL_SHOP = 2;
    public static final int DEFAULT_SHOP_ID_PACKAGE = 3;

    public static final String KEY_FIRST_DAY_OF_WEEK = "FirstDayOfWeek";
    public static final String KEY_CURRENCY = "Currency";
    public static final String KEY_CURRENCY_SYMBOL = "Currency_symbol";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_REFRESHTOKEN = "refreshToken";


    public static final String KEY_DATE_FORMAT = "DateFormat";
    public static final String KEY_IS_CADDIE = "is_caddie";


    public static final int SHOPS_ENABLE_STATUS_YES = 1;
    public static final int PASSWORD_MIN_SIZE = 6;
    public static final int PASSWORD_MAX_SIZE = 16;
    public static final String RENTAL_PRODUCT_TYPE_CADDIE = "1";
    public static final String RENTAL_PRODUCT_TYPE_CART = "2";
    public static final String RENTAL_PRODUCT_TYPE_CLUBS = "3";
    public static final String RENTAL_PRODUCT_TYPE_SHOES = "4";
    public static final String RENTAL_PRODUCT_TYPE_TROLLEY = "5";
    public static final String RENTAL_PRODUCT_TYPE_UMBRELLA = "6";
    public static final String RENTAL_PRODUCT_TYPE_TOWEL = "7";
    public static final String RENTAL_PRODUCT_NAME_CADDIE = "Caddie - ";
    public static final String PAYMENT_CHECK_OUT = "1";
    public static final String PAYMENT_NOT_CHECK_OUT = "0";
    public static final String STR_BRACKETS_START = "(";
    public static final String STR_BRACKETS_END = ")";
    public static final String DATE_RETURN = "dateReturn";
    public static final String STR_DOUBLE_SEPARATOR = " -- ";

    public static final String DB_NAME_ADDRESS = "address.db";
    public static final int REQUEST_CODE_SELECT_ADDRESS = 10010;

    public static final String ROLE_SUPER_ADMIN = "1";

    public static final int ROW_HEIGHT = 100;

    public static final String SEARCH_PRODUCT_TYPE_PACKAGE = "package";
    public static final String SEARCH_PRODUCT_TYPE_PROMOTE = "promote";

    public static final String CHART_SELECT_METHOD_DEFAULT = Constants.CHART_SELECT_METHOD_DAY;
    public static final String CHART_SELECT_METHOD_DAY = "Day";
    public static final String CHART_SELECT_METHOD_WEEK = "Week";
    public static final String CHART_SELECT_METHOD_MONTH = "Month";
    public static final String CHART_SELECT_METHOD_YEAR = "Year";


    public static float FONT_SIZE_SMALLEST = 8.f;
    public static float FONT_SIZE_MORE_SMALLER = 10.f;
    public static float FONT_SIZE_SMALLER = 12.f;
    public static float FONT_SIZE_NORMAL = 16.f;
    public static float FONT_SIZE_LARGER = 20.f;
    public static float FONT_SIZE_MORE_LARGER = 24.f;









    public static float FONT_SIZE_15 = 15.f;
    public static int FONT_SIZE_LOGIN = 28;

    public static final int NFC_RETURN_CODE_20602 = 20602;//no bind
    public static final int NFC_RETURN_CODE_20603 = 20603;// bind
    public static final int NFC_RETURN_CODE_20604 = 20604;//not use

    public static final String NFC_CARD_TYPE_ONE = "1";
    public static final String NFC_CARD_TYPE_BAG = "2";
    public static final String NFC_CARD_TYPE_CADDIE = "3";
    public static final String NFC_CARD_TYPE_BAG_2 = "4";


    public static final String NFC_NO_CHECK_IN = "R";
    public static final String NFC_CHECK_IN = "C";
    public static final String NFC_BIND_STATUS_YES = "1";
    public static final String NFC_BIND_STATUS_NO = "0";


    public static final String ONE_TEE_START_FLAG = "1";
    public static final String TWO_TEE_START_FLAG = "2";
    public static final String THREE_TEE_START_FLAG = "3";
    public static final String MIX_TEE_START_FLAG = "4";
    public static final String CUSTOM_FLAG = "5";



    public static final String CUSTOM_FLAG_START = "1";
    public static final String CUSTOM_FLAG_TRANSFER= "2";
    public static final String CUSTOM_FLAG_9_HLLES = "3";

}
