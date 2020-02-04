package com.learnbind.ai.iot;

public class Constants {

    public static String accessToken = "";

    public static final String BASE_URL = "https://180.101.149.140:8743";

    public static final String APP_ID = "QVeez53jfLYq6Rk8cQ0XEbB3KQwa";
    public static final String SECRET = "GQzxIbNje7Bg7ICFABkjffu41VEa";

//    public static final String CALLBACK_BASE_URL = "http://118.190.141.247:8282";
    public static final String CALLBACK_BASE_URL = "http://39.107.230.168";

    public static final String DEVICE_DATA_CHANGED_CALLBACK_URL = CALLBACK_BASE_URL + "/meter/uploadDeviceData";

    public static final String SUBSCRIBE_NOTIFICATION = BASE_URL + "/iocm/app/sub/v1.1.0/subscribe";

    public static final String HEADER_APP_KEY = "app_key";
    public static final String HEADER_APP_AUTH = "Authorization";

    public static final String POST_ASYNC_CMD = BASE_URL + "/iocm/app/cmd/v1.4.0/deviceCommands";
    public static final String REPORT_CMD_EXEC_RESULT_CALLBACK_URL = CALLBACK_BASE_URL + "/cmd/callback";

    public static final String APP_AUTH = BASE_URL + "/iocm/app/sec/v1.1.0/login";
    public static final String REFRESH_TOKEN = BASE_URL + "/iocm/app/sec/v1.1.0/refreshToken";

    public static final String REGISTER_DEVICE = BASE_URL + "/iocm/app/reg/v1.1.0/devices";
    public static final String MODIFY_DEVICE_INFO = BASE_URL + "/iocm/app/dm/v1.1.0/devices";
    public static final String QUERY_DEVICE_ACTIVATION_STATUS = BASE_URL + "/iocm/app/reg/v1.1.0/devices";
    public static final String DELETE_DEVICE = BASE_URL + "/iocm/app/dm/v1.1.0/devices";
    public static final String DISCOVER_INDIRECT_DEVICE = BASE_URL + "/iocm/app/signaltrans/v1.1.0/devices/%s/services/%s/sendCommand";
    public static final String REMOVE_INDIRECT_DEVICE = BASE_URL + "/iocm/app/signaltrans/v1.1.0/devices/%s/services/%s/sendCommand";

    public static final String QUERY_DEVICES = BASE_URL + "/iocm/app/dm/v1.3.0/devices";
    public static final String QUERY_DEVICE_DATA = BASE_URL + "/iocm/app/dm/v1.3.0/devices";
    public static final String QUERY_DEVICE_HISTORY_DATA = BASE_URL + "/iocm/app/data/v1.1.0/deviceDataHistory";
    public static final String QUERY_DEVICE_CAPABILITIES = BASE_URL + "/iocm/app/data/v1.1.0/deviceCapabilities";

    public static final String DEVICE_DATA_CHANGED = "deviceDatasChanged";

    //Paths of certificates.
    public static String SELF_CERT_PATH = "/src/main/resources/cert/outgoing.CertwithKey.pkcs12";
    public static String TRUST_CA_PATH = "/src/main/resources/cert/ca.jks";

    //Password of certificates.
    public static String SELF_CERT_PWD = "IoM@1234";
    public static String TRUST_CA_PWD = "Huawei@123";
}
