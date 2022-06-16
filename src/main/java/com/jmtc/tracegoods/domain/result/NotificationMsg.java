package com.jmtc.tracegoods.domain.result;

/**
 * @author Chris
 * @date 2021/6/1 21:02
 * @Email:gang.wu@nexgaming.com
 */
public enum NotificationMsg {
    SUCCESS("000000", "Operation Success"),
    FAILED("999999","Operation Fail"),
    LOG_ADD_FAIL("000001", "add log info failed"),
    LOG_FIND_FAIL("000002", "find log info failed"),
    LOG_ADD_FAIL_DESC("000003", "Description is empty"),
    GOODS_FIND_FAIL("000004", "get good info failed"),
    USER_DEL_ERROR("000005", "Error occured when trying to delete the user"),
    USER_NAME_EMPTY("000006", "User Name can not empty"),
    USER_PASSWORD_EMPTY("000007", "User Password can not empty"),
    ATT_ADD_FAIL_NAME("000008", "Att Name can not empty"),
    ATT_ADD_FAIL_DESC("000009", "Att Description is empty"),
    ATT_FIND_FAIL("000010", "find attribute info failed"),
    GUEM_ADD_FAIL_NAME("000011", "GUME Name can not empty"),
    GUEM_ADD_FAIL_MESSAGE("000012", "GUME MESSAGE can not empty"),
    GUEM_FIND_FAIL("000010", "find Message info failed"),
            ;

    private NotificationMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
