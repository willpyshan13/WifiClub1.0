package cn.situne.itee.manager;


import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;

public class ApiManager {

    public enum HttpApi {
        CheckVersion,
        Login,
        ForgotPwd,
        AdministrationEdit,
        ChangePwd,
        TeeTimeCalendar,
        BookingBrowse,
        Appointment,
        AppointmentDetail,
        Csbooking,
        SigningGuest,
        CsbookingSearch,
        Checkinlist,
        DocsBooking,
        BookingDetail,
        MemberProfile,
        BalanceAccount,
        PurchaseHistory,
        Reservation,
        PastBooking,
        GeneralInfo,
        PurchaseDetail,
        Recharge,
        Refund,
        MemberShip,
        MemberShipEdit,
        AnnualFeeRenewal,
        ConfirmPayment,
        Balance,
        Pay,
        EditCourseData,
        EditDoCourseData,
        EditHoleData,
        EditDoHoleData,
        EditDoYards,
        EditdoIndex,
        GetPaceCalculate,
        DoDelCourseData,
        AddCourseData,
        CourseData,
        DoEditCustomersBk,
        BookingGoodsList,
        DoDelBookingGoods,
        TurnOnCard,
        ChangeTransferArea,
        DoDelBooking,
        DepositRecharge,
        DoAddSegmentTime,
        DelSegmentTime,
        DetailSegmentTime,
        Calendar,
        SegmentTime,
        EditSegmentTime,
        DoEditAdministration,
        AddTeeTime,
        EditTeeTime,
        EditTeeTimeSetting,
        EventsProductGet,
        EventsGet,
        EventsEditGet,
        EventsPricingGet,
        EventsDoPricing,
        EventsArea,
        EventsDelete,
        EventsPost,
        EventsPut,
        StaffDepartmentListGet,
        StaffDepartmentPostOrPut,
        StaffUserListGet,
        StaffAutorityGet,
        AgentsListGet,
        AgentsGet,
        SendMessageInfo,
        StaffAuthorityTeeTimePut,
        StaffShopAuthorityPut,
        StaffDiscountAuthorityPut,
        StaffAuthorityEventsPut,
        StaffAuthorityAgentsPut,
        StaffAuthorityStaffPut,
        StaffCustomersAuthorityPut,
        CustomerMemberGet,
        AgentsPricingListGet,
        AgentsPricingListDelete,
        AgentRechargePost,
        AgentsDelete,
        StaffUserDetailGet,
        StaffUserDetailPost,
        StaffUserDetailPut,
        StaffUserDetailDelete,
        StaffCaddieLevelGet,
        StaffCaddieLevelPut,
        AgentsPricingGet,
        AgentsAccountPut,
        AgentsAccountPost,
        AgentsPost,
        MemberPricingListGet,
        AgentsEditPost,
        AgentsPut,
        AgentsProductGet,
        MemberPricingListDelete,
        AgentsPricingPost,
        AgentsPricingPut,
        MemberProductGet,
        ReservationAgentGet,
        PastBookingAgentGet,
        BalanceAccountAgentGet,
        ProductAttribute,
        CustomersMemberPricingGet,
        PurchaseHistoryGet,
        BalanceAccountAgentPut,
        CustomersMemberPricingPost,
        CustomersMemberPricingPut,
        MemberEditListGet,
        MemberGET,
        MemberPOST,
        MemberPUT,
        MemberDelete,
        MemberGreenFeeListGet,
        MemberListGet,
        MemberListDelete,
        ShopsRentalProductGet,
        ShopsBookingFeePut,
        ShopsBookingFeeGet,
        ShopsProductTypeGet,
        ShopsProductTypePost,
        ShopsProductTypePut,
        ShopsProductTypeDelete,
        ShopsPromoteGet,
        ShopsGreenFeeGet,
        ShopsGreenFeePut,
        ShopCaddiePriceGet,
        ShopCaddiePricePut,
        TeeInfoListGet,
        TeeNameListGet,
        TransferTimeGet,
        ShopsPackageGet,
        TeeNameEditPost,
        ShopsPackagePost,
        ShopsPackagePut,
        ShopsChooseProductGet,
        ShopsSubclassGet,
        ShopsSubclassPut,
        ShopsPackageDel,
        ShopsQtySubclassGet,
        ShopsPriceSubclassGet,
        ShopProductGet,
        ShopProductPut,
        ShopProductPost,
        ShopProductDel,
        ShopsRentalProductPost,
        ShopsRentalProductPut,
        ShopsRentalProductDel,
        PromoteProductPost,
        PromoteProductPut,
        PromoteProductDel,
        ShopsProductDetailGet,
        ShopsProductPropertyGet,
        ShoppingPurchaseGet,
        ShopsProductGet,
        ShopsProductPut,
        ShopsProductPost,
        ShopsProductDelete,
        ShoppingPurchasePut,
        ShoppingPurchasePost,
        ShoppingPurchaseDelete,
        ShoppingPurchaseSplitPut,
        ScanQrCodeGet,
        PurchaseAaPost,
        PurchaseAaPut,
        CommonPhoto,
        CommonSignature,
        CityList,
        ShoppingChoosePackage,
        ShoppingProductByCode,
        PromoteProductSubclassGet,
        TeeTimeCheckOutGet,
        TeeTimeCheckOutPut,
        ShoppingCheckPayPut,
        ProductCount,
        SendTempPwd,
        ShoppingCustomerListGet,
        PricingPost,
        PricingGet,
        PricingListPageGet,
        AgentsSearch,
        StaffSearch,
        GetDateSun,
        AgentsPricingDelete,
        ProductSearch,
        ProductSaleAnalysis,
        CustomerConsumeAnalysis,
        CustomerAnalysis,
        CustomerConsumeChangeDetailGet,
        NfcCheckCardGet,
        NfcOneCardBindPost,
        NfcOneCardCheckAllGet,
        NfcBagCardBindGet,
        NfcBagCardUnbindPost,
        NfcBagCardCheckGet,
        NfcCaddiePhoneBindPost,
        NfcCaddieCheckGet,
        NfcCaddieBindPost,
        NfcCaddieUnbindPost,
        NfcCaddieNumberGet,
        NfcCaddiePhoneCheckOneCardGet,
        XDEVELOPX0236,
        XDEVELOPX0235,
        XDEVELOPX02361,
        NfcCaddiePhoneUnBindPost,
        NfcCaddieCardCheckAllGet,
        NFCADD,
        AgentsPricingPutX,
        PricingListPageGetX,
        X0425,
        xcheckin,
        xcheckpricing,
        AgentsBookingTimeList,
        AgentsBookingTimeAdd,
        AgentsBookingTimeDetail,
        AgentsBookingTimeDel,
        Xdeltimes,
        AgentsBookingTimeEdit,
        CsBookingGuestType,
        SignInGuestType,
        BookingSearch,
        BookingDetailList,
        BookingPurchaseDetail,

        GetTeeTimeFormatData,
        SaveTeeTime,
        ChangeReservationTime,
        Cdatacount


    }

    public static String getUrlWithNetApi(HttpApi api, Context mContext) {
        String res = AppUtils.getBaseUrl(mContext);
        String apiPart = StringUtils.EMPTY;
        switch (api) {
            case CheckVersion:
                apiPart = API_CHECK_VERSION;
                break;
            case Login:
                apiPart = API_LOGIN;
                break;
            case ForgotPwd:
                apiPart = API_FORGOT_PWD;
                break;
            case ChangePwd:
                apiPart = API_CHANGE_PWD;
                break;
            case AdministrationEdit:
                apiPart = API_ADMINISTRATION_EDIT;
                break;
            case BookingBrowse:
                apiPart = API_BOOKINGBROWSE;
                break;
            case TeeTimeCalendar:
                apiPart = API_TEE_TIME_CALENDAR;
                break;
            case Csbooking:
                apiPart = API_CSBOOKING;
                break;
            case SigningGuest:
                apiPart = API_SIGNING_GUEST;
                break;
            case CsbookingSearch:
                apiPart = API_CS_BOOKING_SEARCH;
                break;
            case Checkinlist:
                apiPart = API_CHECK_IN_LIST;
                break;
            case Appointment:
                apiPart = API_ADMINISTRATION;
                break;
            case AppointmentDetail:
                apiPart = API_ADMINISTRATION_DETAIL;
                break;
            case DocsBooking:
                apiPart = API_DO_CS_BOOKING;
                break;
            case BookingDetail:
                apiPart = API_BOOKING_DETAIL;
                break;
            case MemberProfile:
                apiPart = API_MEMBER_PROFILE;
                break;
            case BalanceAccount:
                apiPart = API_BALANCE_ACCOUNT;
                break;
            case PurchaseHistory:
                apiPart = API_PURCHASE_HISTORY;
                break;
            case Reservation:
                apiPart = API_RESERVATION;
                break;
            case PastBooking:
                apiPart = API_PAST_BOOKING;
                break;
            case GeneralInfo:
                apiPart = API_GENERAL_INFO;
                break;
            case PurchaseDetail:
                apiPart = API_PURCHASE_DETAIL;
                break;
            case Recharge:
                apiPart = API_RECHARGE;
                break;
            case Refund:
                apiPart = API_REFUND;
                break;

            case MemberShip:
                apiPart = API_MEMBERSHIP;
                break;
            case MemberShipEdit:
                apiPart = API_MEMBERSHIP_EDIT;
                break;
            case AnnualFeeRenewal:
                apiPart = API_ANNUAL_FEE_RENEWAL;
                break;

            case ConfirmPayment:
                apiPart = API_CONFIRM_PAYMENT;
                break;
            case Balance:
                apiPart = API_BALANCE;
                break;
            case Pay:
                apiPart = API_PAY;
                break;
            case EditCourseData:
                apiPart = API_EDITCOURSEDATA;
                break;
            case EditDoCourseData:
                apiPart = API_EDITDOCOURSEDATA;
                break;
            case EditHoleData:
                apiPart = API_EDITHOLEDATA;
                break;
            case EditDoHoleData:
                apiPart = API_EDITDOHOLEDATA;
                break;
            case GetPaceCalculate:
                apiPart = API_GETPACECALCULATE;
                break;
            case EditDoYards:
                apiPart = API_EDITDOYARDS;
                break;
            case EditdoIndex:
                apiPart = API_EDITDOINDEX;
                break;
            case DoDelCourseData:
                apiPart = API_DODELCOURSEDATA;
                break;
            case AddCourseData:
                apiPart = API_ADDCOURSEDATA;
                break;
            case CourseData:
                apiPart = API_COURSEDATA;
                break;
            case DoEditCustomersBk:
                apiPart = API_DOEDIT_CUSTOMERS_BK;
                break;
            case BookingGoodsList:
                apiPart = API_BOOKING_GOODS_LIST;
                break;
            case Calendar:
                apiPart = API_CALENDAR;
                break;
            case DoDelBookingGoods:
                apiPart = API_DO_DEL_BOOKING_GOODS;
                break;
            case TurnOnCard:
                apiPart = API_TURN_ON_CARD;
                break;
            case DoDelBooking:
                apiPart = API_DO_DEL_BOOKING;
                break;
            case DepositRecharge:
                apiPart = API_DEPOSIT_RECHARGE;
                break;
            case DoAddSegmentTime:
                apiPart = API_DOADDSEGMENTTIME;
                break;
            case SegmentTime:
                apiPart = API_SEGMENT_TIME;
                break;
            case EditSegmentTime:
                apiPart = API_EDIT_SEGMENT;
                break;
            case DetailSegmentTime:
                apiPart = API_DETAIL_SEGMENT_TIME;
                break;
            case DelSegmentTime:
                apiPart = API_DELSEGMENTTIME;
                break;
            case ChangeTransferArea:
                apiPart = API_CHANGE_TRANSFER_AREA;
                break;
            case DoEditAdministration:
                apiPart = API_DO_EDIT_ADMINISTRATION;
                break;
            case AddTeeTime:
                apiPart = API_ADD_TEE_TIME;
                break;
            case EditTeeTime:
                apiPart = API_EDIT_TEE_TIME;
                break;
            case EditTeeTimeSetting:
                apiPart = API_EDIT_TEE_TIME_SETTING;
                break;
            case EventsGet:
                apiPart = API_EVENTS;
                break;
            case EventsProductGet:
                apiPart = API_EVENTS_PRODUCT_GET;
                break;
            case EventsEditGet:
                apiPart = API_EVENTS_EDIT;
                break;
            case EventsPricingGet:
                apiPart = API_EVENTS_PRICING;
                break;
            case EventsDoPricing:
                apiPart = API_EVENTS_DO_PRICING;
                break;
            case EventsArea:
                apiPart = API_EVENTS_AREA;
                break;
            case EventsDelete:
                apiPart = API_EVENTS_DELETE;
                break;
            case EventsPut:
                apiPart = API_EVENTS_PUT;
                break;
            case EventsPost:
                apiPart = API_EVENTS_POST;
                break;
            case StaffDepartmentListGet:
                apiPart = API_STAFF_DEPARTMENT_LIST_GET;
                break;
            case StaffDepartmentPostOrPut:
                apiPart = API_STAFF_DEPARTMENT_POST_OR_POST;
                break;
            case StaffAutorityGet:
                apiPart = API_STAFF_AUTHORITY_GET;
                break;
            case StaffUserListGet:
                apiPart = API_STAFF_USER_LIST_GET;
                break;
            case AgentsListGet:
                apiPart = API_AGENT_LIST_GET;
                break;
            case AgentsGet:
                apiPart = API_AGENTS_GET;
                break;
            case SendMessageInfo:
                apiPart = API_SEND_MESSAGE_INFO_GET;
                break;
            case StaffAuthorityTeeTimePut:
                apiPart = API_STAFF_AUTHORITY_PUT;
                break;
            case StaffShopAuthorityPut:
                apiPart = API_STAFF_SHOP_AUTHORITY_PUT;
                break;
            case StaffDiscountAuthorityPut:
                apiPart = API_STAFF_DISCOUNT_AUTHORITY_PUT;
                break;
            case StaffAuthorityEventsPut:
                apiPart = API_STAFF_AUTHORITY_PUT;
                break;
            case StaffAuthorityAgentsPut:
                apiPart = API_STAFF_AUTHORITY_PUT;
                break;
            case StaffAuthorityStaffPut:
                apiPart = API_STAFF_AUTHORITY_PUT;
                break;
            case StaffCustomersAuthorityPut:
                apiPart = API_STAFF_CUSTOMER_AUTHORITY_PUT;
                break;
            case CustomerMemberGet:
                apiPart = API_CUSTOMER_MEMBER_GET;
                break;
            case AgentsPricingListGet:
                apiPart = API_AGENTS_PRICING_LIST_GET;
                break;
            case AgentsPricingListDelete:
                apiPart = API_AGENTS_PRICING_LIST_DELETE;
                break;
            case AgentsDelete:
                apiPart = API_AGENTS_DELETE;
                break;
            case StaffUserDetailGet:
                apiPart = API_STAFF_USER_DETAIL_GET;
                break;
            case StaffUserDetailPost:
                apiPart = API_STAFF_USER_DETAIL_POST;
                break;
            case StaffUserDetailPut:
                apiPart = API_STAFF_USER_DETAIL_PUT;
                break;
            case StaffUserDetailDelete:
                apiPart = API_STAFF_USER_DETAIL_DELETE;
                break;
            case AgentRechargePost:
                apiPart = API_AGENT_RECHARGE_POST;
                break;
            case AgentsPricingGet:
                apiPart = API_AGENT_PRICING_GET;
                break;
            case StaffCaddieLevelGet:
                apiPart = API_STAFF_CADDIE_GET;
                break;
            case StaffCaddieLevelPut:
                apiPart = API_STAFF_CADDIE_PUT;
                break;
            case AgentsAccountPut:
            case AgentsAccountPost:
                apiPart = API_AGENTS_ACCOUNT_PUT;
                break;
            case AgentsPost:
                apiPart = API_AGENTS_POST;
                break;
            case MemberPricingListGet:
                apiPart = API_MEMBER_PRICING_LIST_GET;
                break;
            case AgentsEditPost:
                apiPart = API_AGENTS_EDIT_POST;
                break;
            case AgentsPut:
                apiPart = API_AGENTS_PUT;
                break;
            case AgentsProductGet:
                apiPart = API_AGENTS_PRODUCT_GET;
                break;
            case MemberPricingListDelete:
                apiPart = API_MEMBER_PRICING_LIST_DELETE;
                break;
            case AgentsPricingPost:
            case AgentsPricingPut:
                apiPart = API_AGENTS_AGENTS_PRICING;
                break;
            case ReservationAgentGet:
                apiPart = API_RESERVATION_AGENT_GET;
                break;
            case PastBookingAgentGet:
                apiPart = API_PAST_BOOKING_AGENT_GET;
                break;
            case BalanceAccountAgentGet:
                apiPart = API_BALANCE_ACCOUNT_AGENT_GET;
                break;
            case MemberProductGet:
                apiPart = API_MEMBER_PRODUCT;
                break;
            case ProductAttribute:
                apiPart = API_PRODUCT_ATTRIBUTE;
                break;
            case PurchaseHistoryGet:
                apiPart = API_PURCHASE_HISTORY_GET;
                break;
            case CustomersMemberPricingGet:
                apiPart = API_CUSTOMERS_MEMBER_PRICING_GET;
                break;

            case BalanceAccountAgentPut:
                apiPart = API_BALANCE_ACCOUNT_AGENT_PUT;
                break;
            case CustomersMemberPricingPut:
            case CustomersMemberPricingPost:
                apiPart = API_MEMBER_PRICING_POST_AND_PUT;
                break;
            case MemberEditListGet:
                apiPart = API_MEMBER_EDIT_LIST_GET;
                break;
            case MemberGET:
            case MemberPOST:
            case MemberPUT:
            case MemberDelete:
                apiPart = API_MEMBER_POST_DELETE_PUT_GET;
                break;
            case MemberGreenFeeListGet:
                apiPart = API_MEMBER_GREEN_FEE_LIST;
                break;
            case MemberListGet:
            case MemberListDelete:
                apiPart = API_MEMBER_LIST;
                break;
            case ShopsRentalProductGet:
            case ShopsRentalProductPost:
            case ShopsRentalProductPut:
            case ShopsRentalProductDel:
                apiPart = API_SHOPS_RENTAL_GOODS_TYPE_GET;
                break;
            case ShopsBookingFeePut:
            case ShopsBookingFeeGet:
                apiPart = API_SHOPS_BOOKING_FRR;
                break;
            case ShopsProductTypeGet:
            case ShopsProductTypePost:
            case ShopsProductTypePut:
            case ShopsProductTypeDelete:
                apiPart = API_SHOPS_PRODUCT_TYPE;
                break;
            case ShopsGreenFeeGet:
                apiPart = API_SHOPS_GREEN_FEE_GET;
                break;
            case ShopsGreenFeePut:
                apiPart = API_SHOPS_GREEN_FEE_PUT;
                break;
            case ShopsPromoteGet:
                apiPart = API_SHOPS_PROMOTE;
                break;
            case ShopCaddiePriceGet:
            case ShopCaddiePricePut:
                apiPart = API_SHOPS_CADDIE_PRICE;
                break;
            case TeeInfoListGet:
                apiPart = API_TEE_INFO_LIST_GET;
                break;
            case TeeNameListGet:
                apiPart = API_TEE_NAME_LIST_GET;
                break;
            case TransferTimeGet:
                apiPart = API_TRANSFER_TIME_GET;
                break;
            case ShopsPackageGet:
            case ShopsPackagePost:
            case ShopsPackagePut:
            case ShopsPackageDel:
                apiPart = API_SHOPS_PACKAGE;
                break;
            case TeeNameEditPost:
                apiPart = API_TEE_NAME_EDIT_POST;
                break;
            case ShopsChooseProductGet:
                apiPart = API_SHOPS_CHOOSE_PRODUCT;
                break;
            case ShopsSubclassGet:
            case ShopsSubclassPut:
                apiPart = API_SHOPS_SUBCLASS_GET;
                break;
            case ShopProductGet:
            case ShopProductPost:
            case ShopProductPut:
            case ShopProductDel:
                apiPart = API_SHOP_PRODUCT;
                break;
            case ShopsQtySubclassGet:
                apiPart = API_SHOPS_QTY_SUBCLASS_GET;
                break;
            case ShopsPriceSubclassGet:
                apiPart = API_SHOPS_PRICE_SUBCLASS_GET;
                break;
            case PromoteProductSubclassGet:
                apiPart = API_SHOPS_PROMOTE_PRODUCT_SUBCLASS_GET;
                break;
            case PromoteProductPost:
            case PromoteProductPut:
            case PromoteProductDel:
                apiPart = API_PROMOTE_PRODUCT;
                break;
            case ShopsProductDetailGet:
                apiPart = API_SHOPS_PRODUCT_DETAIL;
                break;
            case ShopsProductPropertyGet:
                apiPart = API_SHOPS_PRODUCT_PROPERTY;
                break;
            case ShoppingPurchaseGet:
            case ShoppingPurchasePut:
            case ShoppingPurchasePost:
            case ShoppingPurchaseDelete:
                apiPart = API_SHOPPING_PURCHASE;
                break;
            case ShopsProductGet:
            case ShopsProductPut:
            case ShopsProductPost:
            case ShopsProductDelete:
                apiPart = API_SHOPS_PRODUCT;
                break;
            case ShoppingPurchaseSplitPut:
                apiPart = API_SHOPPING_PURCHASE_SPLIT;
                break;
            case ScanQrCodeGet:
                apiPart = API_SCAN_QR_CODE;
                break;
            case PurchaseAaPost:
            case PurchaseAaPut:
                apiPart = API_PURCHASE_AA;
                break;
            case CommonPhoto:
                apiPart = API_COMMON_PHOTO;
                break;
            case CommonSignature:
                apiPart = API_COMMON_SIGNATURE;
                break;
            case CityList:
                apiPart = API_CITY_LIST;
                break;
            case ShoppingChoosePackage:
                apiPart = API_CHOOSE_PACKAGE;
                break;
            case ShoppingProductByCode:
                apiPart = API_PRODUCT_BY_CODE;
                break;
            case TeeTimeCheckOutGet:
            case TeeTimeCheckOutPut:
                apiPart = API_TEE_TIME_CHECK_OUT;
                break;
            case ShoppingCheckPayPut:
                apiPart = API_SHOPPING_CHECK_PAY_PUT;
                break;

            case SendTempPwd:
                apiPart = API_SHOPPING_SEND_TEMP_PWD;
                break;
            case ProductCount:
                apiPart = API_SHOPPING_PRODUCT_COUNT;
                break;

            case ShoppingCustomerListGet:
                apiPart = API_SHOPPING_CUSTOMER_LIST;
                break;
            case PricingPost:
            case PricingGet:
                apiPart = API_SHOPPING_PRICING_POST;
                break;
            case PricingListPageGet:
                apiPart = API_SHOPPING_PRICING_LIST_PAGE_GET;
                break;
            case AgentsSearch:
                apiPart = API_AGENTS_SEARCH;
                break;
            case StaffSearch:
                apiPart = API_STAFF_SEARCH;
                break;
            case GetDateSun:
                apiPart = API_GET_DATE_SUN;
                break;
            case AgentsPricingDelete:
                apiPart = API_PRICING_DELETE;
                break;
            case ProductSearch:
                apiPart = API_PRODUCT_SEARCH;
                break;
            case ProductSaleAnalysis:
                apiPart = API_PRODUCT_SALE_ANALYSIS;
                break;
            case CustomerConsumeAnalysis:
                apiPart = API_CUSTOMER_CONSUME_ANALYSIS;
                break;
            case CustomerAnalysis:
                apiPart = API_CUSTOMER_ANALYSIS;
                break;
            case CustomerConsumeChangeDetailGet:
                apiPart = API_CUSTOMER_CONSUMECHANGE_DETAIL;

                break;
            case NfcCheckCardGet:
                apiPart = API_NFC_CHECK_CARD;
                break;

            case NfcOneCardBindPost:
                apiPart = API_NFC_ONE_CARD_BIND;
                break;
            case NfcOneCardCheckAllGet:
                apiPart = API_NFC_ONE_CARD_CHECK_ALL;
                break;
            case NfcBagCardBindGet:
                apiPart = API_NFC_BAG_CARD_BIND;
                break;

            case NfcBagCardUnbindPost:
                apiPart = API_NFC_BAG_CARD_UNBIND;
                break;
            case NfcBagCardCheckGet:
                apiPart = API_NFC_BAG_CARD_CHECK;
                break;

            case NfcCaddiePhoneBindPost:
                apiPart = API_NFC_CADDIE_PHONE_BIND;
                break;
            case NfcCaddieCheckGet:
                apiPart = NFC_CADDIE_CHECK;
                break;

            case NfcCaddieBindPost:
                apiPart = NFC_CADDIE_BIND;
                break;

            case NfcCaddieUnbindPost:
                apiPart = NFC_CADDIE_UNBIND_POST;
                break;

            case NfcCaddieNumberGet:
                apiPart = NFC_CADDIE_NUMBER;
                break;

            case NfcCaddiePhoneCheckOneCardGet:
                apiPart = NFC_CADDIE_PHONE_CHECK_ONECARD;
                break;

            case NfcCaddiePhoneUnBindPost:
                apiPart = NFC_CADDIE_PHONE_UN_BIND;
                break;

            case NfcCaddieCardCheckAllGet:
                apiPart = NFC_CADDIE_CARD_CHECK_ALL;
                break;

            case NFCADD:
                apiPart = NFC_ADD;
                break;

            case AgentsPricingPutX:
                apiPart = "x114";
                break;

            case PricingListPageGetX:
                apiPart = "x113";
                break;

            case XDEVELOPX0235:
                apiPart = "x0235";
                break;

            case XDEVELOPX02361:
                apiPart = "x02361";
                break;
            case XDEVELOPX0236:
                apiPart = "x0236";
                break;

            case X0425:
                apiPart = "x0425";
                break;

            case xcheckin:
                apiPart = "xcheckin";
                break;

            case xcheckpricing:
                apiPart = "xcheckpricing";
                break;

            case AgentsBookingTimeList:
                apiPart = "agentsBookingTimeList";
                break;


            case AgentsBookingTimeAdd:
                apiPart = "agentsBookingTimeAdd";
                break;

            case AgentsBookingTimeDetail:
                apiPart = "agentsBookingTimeDetail";
                break;


            case AgentsBookingTimeDel:
                apiPart = "agentsBookingTimeDel";
                break;

            case Xdeltimes:
                apiPart = "xdeltimes";
                break;


            case AgentsBookingTimeEdit:
                apiPart = "agentsBookingTimeEdit";
                break;


            case CsBookingGuestType:
                apiPart = "csbookingusertype";
                break;

            case SignInGuestType:
                apiPart = "signingusertype";
                break;

            case BookingSearch:
                apiPart = "bookingSearch";
                break;

            case BookingDetailList:
                apiPart = "bookingDetailList";
                break;


            case BookingPurchaseDetail:
                apiPart = "bookingPurchaseDetail";
                break;

            case GetTeeTimeFormatData:
                apiPart = "getTeeTimeFormatData";
                break;
            case SaveTeeTime:
                apiPart = "saveTeeTime";
                break;


            case ChangeReservationTime:
                apiPart = "changeReservationTime";
                break;



            case Cdatacount:
                apiPart = "cdatacount";
                break;


        }
        return res + apiPart;
    }

    private static final String API_CHECK_VERSION = StringUtils.EMPTY;

    private static final String API_LOGIN = "login";
    private static final String API_FORGOT_PWD = "forgetpwd";
    private static final String API_CHANGE_PWD = "changepwd";
    private static final String API_BOOKINGBROWSE = "main";
    private static final String API_ADMINISTRATION_EDIT = "edit";
    private static final String API_DO_EDIT_ADMINISTRATION = "doedit";
    private static final String API_TEE_TIME_CALENDAR = "teetimes";

    private static final String API_SEGMENT_TIME = "segmenttime";
    private static final String API_EDIT_SEGMENT = "doeditsegmenttime";
    private static final String API_DETAIL_SEGMENT_TIME = "detailsegmenttime";


    private static final String API_ADD_TEE_TIME = "adddoteetimeset";
    private static final String API_EDIT_TEE_TIME = "editdoteetimeset";
    private static final String API_EDIT_TEE_TIME_SETTING = "editteetimeset";

    //04-1 START
    private static final String API_CSBOOKING = "csbooking";
    private static final String API_SIGNING_GUEST = "signingguest";
    private static final String API_CS_BOOKING_SEARCH = "csbookingsearch";
    private static final String API_DO_CS_BOOKING = "docsbooking";
    private static final String API_DOEDIT_CUSTOMERS_BK = "doeditcustomersbk";
    private static final String API_BOOKING_DETAIL = "bookingdetail";
    private static final String API_BOOKING_GOODS_LIST = "bookinglist";
    private static final String API_DO_DEL_BOOKING_GOODS = "dodelbookinggoods";
    private static final String API_TURN_ON_CARD = "turnon";
    private static final String API_DO_DEL_BOOKING = "dodelbooking";
    private static final String API_DEPOSIT_RECHARGE = "depositrecharge";
    private static final String API_CHANGE_TRANSFER_AREA = "changetransferarea";

    //player
    private static final String API_MEMBER_PROFILE = "profile";

    private static final String API_RECHARGE = "recharge";
    private static final String API_MEMBERSHIP_EDIT = "memberShip";

    private static final String API_MEMBERSHIP = "x0483";
    private static final String API_CONFIRM_PAYMENT = "confirmPayment";
    private static final String API_BALANCE_ACCOUNT = "balanceAccount";
    private static final String API_PURCHASE_HISTORY = "purchaseHistory";

    private static final String API_PURCHASE_DETAIL = "purchaseDetail";

    private static final String API_ANNUAL_FEE_RENEWAL = "annualFeeRenewal";

    private static final String API_RESERVATION = "reservation";
    private static final String API_PAST_BOOKING = "pastBooking";
    private static final String API_GENERAL_INFO = "generalInfo";


    //04-1 END
    private static final String API_ADMINISTRATION = "appointment";

    private static final String API_ADMINISTRATION_DETAIL = "appointmentdetail";
    private static final String API_EDITCOURSEDATA = "editcdata";
    private static final String API_EDITDOCOURSEDATA = "editdocdata";
    private static final String API_EDITHOLEDATA = "edithdata";
    private static final String API_EDITDOHOLEDATA = "editdohdata";
    private static final String API_GETPACECALCULATE = "getPaceCalculate";
    private static final String API_EDITDOYARDS = "editdoYards";
    private static final String API_EDITDOINDEX = "editdoIndex";
    private static final String API_DODELCOURSEDATA = "deldocdata";
    private static final String API_ADDCOURSEDATA = "addcdata";
    private static final String API_COURSEDATA = "cdata";
    private static final String API_DOADDSEGMENTTIME = "doaddsegmenttime";
    private static final String API_DELSEGMENTTIME = "delsegmenttime";

    private static final String API_CHECK_IN_LIST = "checkinlist";


    private static final String API_CALENDAR = "calendar";
    private static final String API_EVENTS = "events";
    private static final String API_EVENTS_PRODUCT_GET = "eventsProduct";
    private static final String API_EVENTS_EDIT = "eventsEdit";

    private static final String API_EVENTS_PRICING = "eventsPricing";
    private static final String API_EVENTS_DO_PRICING = "eventsDoPricing";
    private static final String API_EVENTS_AREA = "eventsArea";
    private static final String API_EVENTS_DELETE = "events";
    private static final String API_EVENTS_POST = "events";
    private static final String API_EVENTS_PUT = "events";

    private static final String API_STAFF_DEPARTMENT_LIST_GET = "departmentList";
    private static final String API_STAFF_DEPARTMENT_POST_OR_POST = "department";
    private static final String API_STAFF_AUTHORITY_GET = "authority";
    private static final String API_STAFF_USER_LIST_GET = "userList";
    private static final String API_STAFF_USER_DETAIL_GET = "userDetail";
    private static final String API_STAFF_USER_DETAIL_POST = "userDetail";
    private static final String API_STAFF_USER_DETAIL_PUT = "userDetail";
    private static final String API_STAFF_USER_DETAIL_DELETE = "userDetail";

    private static final String API_STAFF_CADDIE_GET = "caddieLevel";
    private static final String API_STAFF_CADDIE_PUT = "caddieLevel";

    private static final String API_AGENT_LIST_GET = "agentsList";
    private static final String API_AGENTS_GET = "agents";
    private static final String API_SEND_MESSAGE_INFO_GET = "sendMessageInfo";


    private static final String API_STAFF_AUTHORITY_PUT = "authority";
    private static final String API_STAFF_SHOP_AUTHORITY_PUT = "shopsAuthority";
    private static final String API_STAFF_DISCOUNT_AUTHORITY_PUT = "discountAuthority";
    private static final String API_STAFF_CUSTOMER_AUTHORITY_PUT = "customersAuthority";

    private static final String API_CUSTOMER_MEMBER_GET = "member";
    private static final String API_AGENTS_PRICING_LIST_GET = "agentsPricingList";
    private static final String API_AGENTS_PRICING_LIST_DELETE = "agentsPricingList";
    private static final String API_AGENTS_DELETE = "agents";
    private static final String API_AGENT_RECHARGE_POST = "agentRecharge";
    private static final String API_AGENT_PRICING_GET = "agentsPricing";
    private static final String API_AGENTS_ACCOUNT_PUT = "agentsAccount";
    private static final String API_AGENTS_POST = "agents";
    private static final String API_MEMBER_PRICING_LIST_GET = "memberPricingList";
    private static final String API_AGENTS_EDIT_POST = "agentsEdit";
    private static final String API_AGENTS_PUT = "agents";
    private static final String API_AGENTS_PRODUCT_GET = "agentsProduct";

    private static final String API_RESERVATION_AGENT_GET = "reservationAgent";
    private static final String API_PAST_BOOKING_AGENT_GET = "pastBookingAgent";
    private static final String API_BALANCE_ACCOUNT_AGENT_GET = "balanceAccountAgent";

    private static final String API_AGENTS_AGENTS_PRICING = "agentsPricing";

    private static final String API_MEMBER_PRICING_LIST_DELETE = "memberPricingList";
    private static final String API_MEMBER_PRODUCT = "memberProduct";
    private static final String API_PURCHASE_HISTORY_GET = "purchaseHistory";
    private static final String API_CUSTOMERS_MEMBER_PRICING_GET = "memberPricing";
    private static final String API_BALANCE_ACCOUNT_AGENT_PUT = "balanceAccountAgent";

    private static final String API_PRODUCT_ATTRIBUTE = "productAttribute";
    private static final String API_MEMBER_PRICING_POST_AND_PUT = "memberPricing";
    private static final String API_MEMBER_EDIT_LIST_GET = "memberEditList";
    private static final String API_MEMBER_POST_DELETE_PUT_GET = "member";
    private static final String API_MEMBER_GREEN_FEE_LIST = "greenFeeList";
    private static final String API_MEMBER_LIST = "memberList";

    private static final String API_SHOPS_RENTAL_GOODS_TYPE_GET = "rentalProduct";
    private static final String API_SHOPS_BOOKING_FRR = "bookingFee";
    private static final String API_SHOPS_PRODUCT_TYPE = "productType";
    private static final String API_SHOPS_GREEN_FEE_GET = "greenFee";

    private static final String API_SHOPS_GREEN_FEE_PUT = "greenFee";
    private static final String API_REFUND = "refund";

    private static final String API_SHOPS_PROMOTE = "promote";

    private static final String API_SHOPS_CADDIE_PRICE = "caddiePrice";

    private static final String API_TEE_INFO_LIST_GET = "teeInfoList";
    private static final String API_TEE_NAME_LIST_GET = "teeNameList";
    private static final String API_TRANSFER_TIME_GET = "transferTime";

    private static final String API_SHOPS_PACKAGE = "package";

    private static final String API_TEE_NAME_EDIT_POST = "teeNameEdit";

    private static final String API_SHOPS_CHOOSE_PRODUCT = "chooseProduct";

    private static final String API_SHOPS_SUBCLASS_GET = "subclass";

    private static final String API_SHOPS_QTY_SUBCLASS_GET = "qtySubclass";

    private static final String API_SHOPS_PRICE_SUBCLASS_GET = "priceSubclass";
    private static final String API_SHOPS_PROMOTE_PRODUCT_SUBCLASS_GET = "promoteProductSubclass";


    private static final String API_SHOP_PRODUCT = "product";
    private static final String API_PROMOTE_PRODUCT = "promote";

    private static final String API_SHOPS_PRODUCT_DETAIL = "productDetail";
    private static final String API_BALANCE = "balance";
    private static final String API_PAY = "payment";

    private static final String API_SHOPS_PRODUCT = "product";

    private static final String API_SHOPS_PRODUCT_PROPERTY = "productProperty";
    private static final String API_SHOPPING_PURCHASE = "purchase";

    private static final String API_SHOPPING_PURCHASE_SPLIT = "purchaseSplit";
    private static final String API_SCAN_QR_CODE = "entry";

    private static final String API_PURCHASE_AA = "purchaseAA";
    private static final String API_COMMON_PHOTO = "photo";
    private static final String API_COMMON_SIGNATURE = "signature";
    private static final String API_CITY_LIST = "cityList";

    private static final String API_CHOOSE_PACKAGE = "choosePackage";
    private static final String API_PRODUCT_BY_CODE = "productByCode";

    private static final String API_TEE_TIME_CHECK_OUT = "checkOut";

    private static final String API_SHOPPING_CHECK_PAY_PUT = "checkPayment";

    private static final String API_SHOPPING_SEND_TEMP_PWD = "sendTempPwd";

    private static final String API_SHOPPING_PRODUCT_COUNT = "productCount";

    private static final String API_SHOPPING_CUSTOMER_LIST = "customerList";
    private static final String API_SHOPPING_PRICING_POST = "Pricing";
    private static final String API_SHOPPING_PRICING_LIST_PAGE_GET = "PricingListPage";

    private static final String API_AGENTS_SEARCH = "agentsSearch";
    private static final String API_STAFF_SEARCH = "userSearch";

    private static final String API_GET_DATE_SUN = "getDateSun";

    private static final String API_PRICING_DELETE = "Pricing";

    private static final String API_PRODUCT_SEARCH = "productSearch";

    private static final String API_PRODUCT_SALE_ANALYSIS = "ProductSaleAnalysis";
    private static final String API_CUSTOMER_CONSUME_ANALYSIS = "CustomerConsumeAnalysis";
    private static final String API_CUSTOMER_ANALYSIS = "CustomerAnalysis";

    private static final String API_CUSTOMER_CONSUMECHANGE_DETAIL = "CustomerConsumeChangeDetail";

    private static final String API_NFC_CHECK_CARD = "nfcCheckCard";
    private static final String API_NFC_ONE_CARD_BIND = "nfcOneCardBind";

    private static final String API_NFC_ONE_CARD_CHECK_ALL = "nfcOneCardCheckAll";
    private static final String API_NFC_BAG_CARD_BIND = "nfcBagCardBind";

    private static final String API_NFC_BAG_CARD_UNBIND = "nfcBagCardUnbind";

    private static final String API_NFC_BAG_CARD_CHECK = "nfcBagCardCheck";

    private static final String API_NFC_CADDIE_PHONE_BIND = "nfcCaddiePhoneBind";
    private static final String NFC_CADDIE_CHECK = "nfcCaddieCheck";
    private static final String NFC_CADDIE_BIND = "nfcCaddieBind";
    private static final String NFC_CADDIE_UNBIND_POST = "nfcCaddieUnbind";
    private static final String NFC_CADDIE_NUMBER = "nfcCaddieNumber";
    private static final String NFC_CADDIE_PHONE_CHECK_ONECARD = "nfcCaddiePhoneCheckOneCard";
    private static final String NFC_CADDIE_PHONE_UN_BIND = "nfcCaddiePhoneUnBind";
    private static final String NFC_CADDIE_CARD_CHECK_ALL = "nfcCaddieCardCheckAll";
    private static final String NFC_ADD = "nfcCardAdd";


}


