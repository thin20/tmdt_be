package com.example.tmdt_be.common;

public class Const {
    public final static String CHECK_ISDN_ADD_PREFIX = "^0+(?!$)|^84(?!$)";

    public final static String REGEX_BASE64 = "^(?:[A-Za-z0-9+\\/]{4})*(?:[A-Za-z0-9+\\/]{2}==|[A-Za-z0-9+\\/]{3}=)?$";

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    public final static class SORT_TYPE {
        public final static String NEWEST = "createdAt";
        public final static String POPULAR = "star";
        public final static String BEST_SALE = "selled";
        public final static String PRICE = "price";
    }

    public final static class ORDER_TYPE {
        public final static String DESC = "desc";
        public final static String ASC = "asc";
    }

    public final static class PURCHASE_TYPE {
        public final static String ALL = "0";
        public final static String ORDER = "1";
        public final static String WAIT_CONFIRM = "2";
        public final static String WAIT_TAKE = "3";
        public final static String DELIVERING = "4";
        public final static String DELIVERED = "5";
        public final static String CANCELED = "6";
    }
}
