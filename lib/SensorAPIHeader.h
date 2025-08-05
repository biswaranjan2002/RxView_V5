////////////////////////////////////////////////////////////////////////////////
/// @file		SensorLib.h
/// @author		Kevin
/// @brief		
///
/// Copyright(c): PIXXGEN Co.,Ltd. All rights are reserved.\n
/// Contact		: soft2@PIXXGEN.com				  
////////////////////////////////////////////////////////////////////////////////
#pragma once

#define MAP_PREFIX          _T("Local\\")
#define MAP_NAME            _T("SensorProbeMap")
#define FULL_MAP_NAME       MAP_PREFIX MAP_NAME
#define MMF_HEADER          128
#define MMF_MEMORY          536870912

#define MMF_BUF_SIZE MMF_HEADER+MMF_MEMORY

///////////////////    enum    ///////////////////////
typedef	enum _SENSOR_RAW_TYPE
{
	SRT_NONE = 0x0000,
	SRT_IMAGE = 0x2001,
	SRT_DARK,
	SRT_BRIGHT,
	SRT_BPM,
	SRT_DOWNLOAD_DARK,
	SRT_DOWNLOAD_BRIGHT,
	SRT_DOWNLOAD_BPM,
	SRT_NDT_DARK,
	SRT_MULTI_DARK,
	SRT_MULTI_BRIGHT,	//0x200A
	SRT_MULTI_IMAGE,
	SRT_MULTI_BPM,
	SRT_CUSTOM_DARK,
	SRT_PRE_DARK,
}SENSOR_RAW_TYPE;
typedef	enum _SENSOR_FILE_TYPE
{
	SFT_NONE = 0x0000,
	SFT_JPG = 0x2101,
	SFT_PNG,
	SFT_LOG,
}SENSOR_FILE_TYPE;
typedef	enum _CALLBACK_ID
{
	CBID_NONE = 0x0000,
	CBID_CONNECT = 0x0001,
	CBID_DISCONNECT,
	CBID_ALIVE,				//not used
	CBID_SYSTEMINFO,
	CBID_COMMAND,			//not used
	CBID_ACK,
	CBID_UPLOAD_START,
	CBID_UPLOAD,			//not used
	CBID_DOWNLOAD_START,
	CBID_DOWNLOAD,
	CBID_WIFI,
	CBID_WIRED,
	CBID_DR_MODEL,
	CBID_OTHER,
	CBID_BRIGHT_TIMER,
	CBID_ETC,
	CBID_ERROR,
	CBID_MULTI_DOWNLOAD,
	CBID_DEADMAN_PARAM,
	CBID_ETC_DATA,
	CBID_FORCED_DISCONNECTION,
	CBID_RNDIS_USB,
}CALLBACK_ID;

typedef	enum _SENSOR_MODULE_FUNCTION
{
	SMF_NONE = 0x00,
	SMF_INIT_LIB = 0x1001,
	SMF_CLOSE_LIB,
	SMF_CONNECT,
	SMF_DISCONNECT,
	SMF_FORCED_CONNECT,

	// config
	SMF_READ_SYSTEM_INFO = 0x1100,	//0x1100
	SMF_WRITE_SYSTEM_INFO,
	SMF_READ_WIRED,
	SMF_WRITE_WIRED,
	SMF_READ_WIFI,
	SMF_WRITE_WIFI,
	SMF_READ_OTHER_CONFIG,
	SMF_WRITE_OTHER_CONFIG,
	SMF_READ_CAL_MODE,
	SMF_WRITE_CAL_MODE,
	SMF_READ_BATTERY_LEVEL,		//0x110A
	SMF_READ_TEMPERATURE,
	SMF_READ_WIFI_LEVEL,
	SMF_READ_RDY_TIME,
	SMF_WRITE_RDY_TIME,
	SMF_READ_EXP_TIME,
	SMF_WRITE_EXP_TIME,			//0x1110
	SMF_READ_MULTI_BINNING_PARAM,
	SMF_WRITE_MULTI_BINNING_PARAM,
	SMF_READ_WEB_VIEWER_FLAG,
	SMF_WRITE_WEB_VIEWER_FLAG,
	SMF_READ_SLEEP_MODE_FLAG,
	SMF_WRITE_SLEEP_MODE_FLAG,
	SMF_READ_CHANGE_WIFI_MODE_BY_POWER_BTN_FLAG,
	SMF_WRITE_CHANGE_WIFI_MODE_BY_POWER_BTN_FLAG,
	SMF_READ_GAIN_PARAM,
	SMF_WRITE_GAIN_PARAM,		//0x111A
	SMF_READ_CAL_STATUS,
	SMF_READ_RAW_RESOLUTION,
	SMF_READ_RS232_BAUD_RATE,
	SMF_WRITE_RS232_BAUD_RATE,
	SMF_READ_DEADMAN_SWITCH_CON,
	SMF_WRITE_DEADMAN_SWITCH_CON,
	SMF_READ_WIFI_ENABLE,
	SMF_WRITE_WIFI_ENABLE,
	SMF_READ_RS232_PARAM,
	SMF_WRITE_RS232_PARAM,
	SMF_READ_CAL_PROCESS,
	SMF_WRITE_CAL_PROCESS,
	SMF_READ_POGO_CONNECTOR,
	SMF_WRITE_NET_CHECK,
	SMF_READ_NOTIFY_BATTERY_CHARGE,
	SMF_WRITE_NOTIFY_BATTERY_CHARGE,
	SMF_READ_ENABLE_AED_INTERRUPT,
	SMF_WRITE_ENABLE_AED_INTERRUPT,
	SMF_READ_USE_FAED,
	SMF_WRITE_USE_FAED,
	SMF_READ_SKIP_THRESHOLD,
	SMF_WRITE_SKIP_THRESHOLD,
	SMF_READ_EX_EXP,
	SMF_WRITE_EX_EXP,
	SMF_READ_ENABLE_INNER_STORAGE,
	SMF_WRITE_ENABLE_INNER_STORAGE,
	SMF_READ_RNDIS,
	SMF_WRITE_RNDIS,

	//commend
	SMF_RESET = 0x1200,
	SMF_REBOOT,
	SMF_CALIBRATION,
	SMF_STOP_ACQUISITION,
	SMF_STOP_TRANSMISSION,
	SMF_FIRMWARE_LOG,

	//raw data
	SMF_GET_IMAGE = 0x1300,
	SMF_GET_DARK = 0x1301,
	SMF_GET_BRIGHT,
	SMF_MAKE_BPM,
	SMF_GET_NDT_DARK,

	SMF_DOWNLOAD_INNER_DARK = 0x1400,
	SMF_DOWNLOAD_INNER_BRIGHT,
	SMF_DOWNLOAD_INNER_BPM,
	SMF_DOWNLOAD_INNER_UNSENT,

	SMF_UPLOAD_FIRMWARE = 0x1500,
	SMF_UPLOAD_DARK,
	SMF_UPLOAD_BRIGHT,
	SMF_UPLOAD_BPM,

	//multi
	SMF_MULTI_ONE_EXP = 0x1600,
	SMF_MULTI_READ_ONE_EXP_PARAM,
	SMF_MULTI_WRITE_ONE_EXP_PARAM,
	SMF_MULTI_READ_MMF_FLAG,
	SMF_MULTI_WRITE_MMF_FLAG,
	SMF_MULTI_START,
	SMF_MULTI_STOP,

	//inner storage
	SMF_DELETE_ALL_SENT = 0x1700,
	SMF_DELETE_ALL_UNSENT,
	SMF_READ_STORAGE_RATE,
	SMF_WRITE_STORAGE_RATE,

	//etc
	SMF_GET_API_VERSION = 0x1800,
	SMF_WRITE_STATIC_CAL_PATH,
	SMF_READ_CAL_PATH,
	SMF_READ_CUT_OPTION,
	SMF_WRITE_CUT_OPTION,
	SMF_CUT_RAW_DATA,
	SMF_VIRTUAL_EXPOSURE,
	SMF_READ_DOWNLOAD_STATUS_OPTION,
	SMF_WRITE_DOWNLOAD_STATUS_OPTION,
	SMF_READ_IMAGE_ID,
	SMF_WRITE_IMAGE_ID,
	SMF_READ_TRANSFER_MODE,
	SMF_WRITE_TRANSFER_MODE,
	SMF_READ_REFRESH_INFO,
	SMF_WRITE_REFRESH_INFO,

	SMF_READ_NO_IMG_ON_OLED,
	SMF_WRITE_NO_IMG_ON_OLED,
	SMF_READ_MOVEMENT_DETECTION,
	SMF_WRITE_MOVEMENT_DETECTION,
	SMF_READ_CHECK_ALIVE,
	SMF_WRITE_CHECK_ALIVE,
	SMF_READ_BATTERY_LOW_LIMIT,
	SMF_WRITE_BATTERY_LOW_LIMIT,
	SMF_READ_PIXEL_SPACING,

	//1826N support
	SMF_READ_TFT_CLEAR = 0x1900,
	SMF_WRITE_TFT_CLEAR,
	SMF_READ_DR_RELAY,
	SMF_WRITE_DR_RELAY,
	SMF_READ_RS232_FILTER,
	SMF_WRITE_RS232_FILTER,
	SMF_READ_RS232_MAX_LENGTH,
	SMF_WRITE_RS232_MAX_LENGTH,
	SMF_READ_RS232_TERMINATOR_COUNT,
	SMF_WRITE_RS232_TERMINATOR_COUNT,
	SMF_WRITE_RS232_CMD,

}SENSOR_MODULE_FUNCTION;
typedef	enum _SENSOR_ACK_TYPE
{
	SAT_NONE = 0x0000,//not used
	SAT_SET = 0x3001,
	SAT_GET,//not used
	SAT_INFO,
	SAT_CMD,
	SAT_UPLOAD,
	SAT_AED_INTERRUPT,
	SAT_UN_SENT_IMAGE_CNT,
	SAT_DR_SKIP,
	SAT_G_SKIP,
	SAT_MULTI_END,
	SAT_BAT_ENABLE,
	SAT_WRITE_SYSTEM_INFO = 0x3101,
	SAT_RESET,
	SAT_REBOOT,
	SAT_GET_IMAGE,
	SAT_GET_DARK,
	SAT_GET_BRIGHT,
	SAT_MAKE_BPM,
	SAT_GET_NDT_DARK,
	SAT_MULTI_ONE_EXP,
	SAT_WRITE_WIFI,//0x3110A
	SAT_UPLOAD_DARK,
	SAT_UPLOAD_BRIGHT,
	SAT_UPLOAD_BPM,
	SAT_UPLOAD_FIRMWARE,
	SAT_WRITE_WIRED,
	SAT_WRITE_OTHER_CONFIG,
	SAT_WRITE_EXP_TIME,
	SAT_WRITE_RDY_TIME,
	SAT_WRITE_WEB_VIEWER_FLAG,
	SAT_WRITE_SLEEP_MODE_FLAG,
	SAT_STOP_ACQUISITION,
	SAT_WRITE_MULTI_BINNING_PARAM,
	SAT_TFT_CLEAR_STOP,
	SAT_TFT_CLEAR_START,
	SAT_RELAY_ON,
	SAT_RELAY_OFF,
	SAT_RS232_SETTING,
	SAT_DEADMAN_SWITCH_READY,
	SAT_DETECTOR_IS_READY,
	SAT_WRITE_POWER_BTN_DOBULE_CLICK,
	SAT_WRITE_GAIN,
	SAT_WRITE_DEADMAN_SWITCH_CONTROL,
	SAT_WRITE_WIFI_MODE,
	SAT_WRITE_NOFITY_BATTERY_CHARGING,
	SAT_WRITE_ENABLE_AED_INTERRUPT,
	SAT_WRITE_USE_FAED,
	SAT_WRITE_EX_EXP_TIME,
	SAT_WRITE_SKIP_THRESHOLD,
	SAT_VIRTUAL_EXPOSURE,
	SAT_DELETE_ALL_UNSENT,
	SAT_DELETE_ALL_SENT,
	SAT_WRITE_IMAGE_ID,
	SAT_WRITE_RS232_FILTER,
	SAT_WRITE_TERMINATOR_COUNT,
	SAT_WRITE_MAX_LENGTH,
	SAT_ENABLE_INNER_STORAGE,
	SAT_DOWNLOAD_INNER_UNSENT,
	SAT_WRITE_TRANSFER_MODE,

}SENSOR_ACK_TYPE;
typedef	enum _SENSOR_ERROR_FIELD
{
	SEF_SUCCESS = 0x00,
	SEF_UNKNOWN_MODULE = 0x4001,
	SEF_MODULE_OPEN_FAIL,
	SEF_ALREADY_OPEN_MODULE,
	SEF_MODULE_CLOSE_FAIL,
	SEF_MODULE_IS_NOT_CONSISTENT,
	SEF_MODULE_IS_DISABLE,
	SEF_INI_IS_NOT_EXISTED,
	SEF_TRIGGER_IS_NOT_MULTI_FRAME,
	SEF_DISCONNECTED_STATUS,
	SEF_ALREADY_CONNECTED,	//0x400A
	SEF_DR_IS_NOT_NDT_MODEL,
	SEF_MAKIMG_DARK,
	SEF_MAKIMG_NDT_DARK,
	SEF_MAKIMG_MULTI_DARK,
	SEF_MAKIMG_BRIGHT,
	SEF_NG_BRIGHT,			//0x4010
	SEF_MAKIMG_BPM,
	SEF_PARAM_OUT_OF_RANGE,
	SEF_LIBRARY_IS_IN_USE,
	SEF_CALLBACK_POINTER_IS_NOT_NULL,
	SEF_NDT_TRIGGER_NO_SUPPORT_MODULE,
	SEF_STOP_ACQUISITION,
	SEF_DR_IS_NOT_MULTIFRAME_MODEL,
	SEF_DISABLE_MMF,
	SEF_CURRENT_STATUS_IS_TRANSMISSION,
	SEF_CURRENT_STATUS_IS_NOT_TRANSMISSION,
	SEF_API_IS_NOT_INITIALIZE,
	SEF_MICOM_ERROR,
}SENSOR_ERROR_FIELD;
typedef	enum _SENSOR_ETC_INFO
{
	SEI_NONE = 0x00,
	SEI_API_VERSION = 0x5001,
	SEI_CALIBRATION_MODE,
	SEI_DETECTOR_MOVEMENT,
	SEI_BATTERY_LEVEL,
	SEI_TEMPERATURE,
	SEI_WIFI_LEVEL,
	SEI_RDY_TIME,
	SEI_EXP_TIME,
	SEI_BINNING_INFO,
	SEI_WEB_VIEWER,//0x500A
	SEI_SLEEP_MODE,
	SEI_CHANGE_WIFI_MODE_BY_POWER_BTN,
	SEI_GAIN,
	SEI_CAL_STATUS,
	SEI_SW_TR_READY,
	SEI_SW_TR_EXPOSURE,//0x5010
	SEI_DOWNLOAD_STATUS,
	SEI_MMF_INFO,
	SEI_MMF_IMAGE,
	SEI_RAW_RESOLUTION,
	SEI_20501_NOT_USED,		//not used
	SEI_20502_NOT_USED,		//not used
	SEI_WIFI_MODE,
	SEI_RS232_BAUD_RATE,
	SEI_RS232_PARAM,
	SEI_CAL_PROCESSING,//0x501A
	SEI_TFT_CLEAR,
	SEI_RELAY,
	SEI_DEADMAN_SWITCH_STATUS,
	SEI_POGO_STATUS,
	SEI_NOTIFY_BATTERY_CHARGING,
	SEI_ENABLE_AED_INTERRUPT,//0x5020
	SEI_FAED_MODE,
	SEI_SKIP_THRESHOLD,
	SEI_EX_EXP,
	SEI_RS232_MAX_LENGTH,
	SEI_RS232_TERMINATOR_COUNT,
	SEI_ENABLE_INNER_STORAGE,
	SEI_DETECTOR_IS_CONNECTED_TO_ANOTHER_CLIENT,
	SEI_PW_BTN_IS_PRESSED,
	SEI_TRANSFER_MODE,
	SEI_REFRESH_INFO,
	SEI_REFRESH_START,
	SEI_REFRESH_END,
	SEI_LOWER_LIMIT_BATTERY,
	SEI_DISPLAY_IMG_ON_OLED,
	SEI_INNER_STORAGE_SIZE,
	SEI_ENABLE_MOVEMENT_DETECTION,
	SEI_ENABLE_CHECK_ALIVE,
	SEI_HOT_SWAP_STATE,
	SEI_NDT_DARK_INFO,
	SEI_PIXEL_SPACING,

}SENSOR_ETC_TYPE;
//#define SEI_NOFITY_BATTERY_CHARGING 20511 //0x501F

typedef	enum _SENSOR_ETC_DATA
{
	SED_NONE = 0x00,
	SED_IMAGE_ID = 0x6001,
	SED_RS232_FILTER,
	SED_UPLOAD_STATE,
	SED_RS232_CMD,

}SENSOR_ETC_DATA;


#if defined(WIN32)
#include <pshpack1.h>
#else
#pragma pack(push,1)
#endif

////////////////////////  Data struct   ///////////////////////////
typedef	struct _SENSOR_MULTI_ONE_EXP
{
	unsigned int m_unFramePerSecond;	// Frame Per Second
	unsigned int m_unFrameNum;			// 1 ~ 8
}SENSOR_MULTI_ONE_EXP;
typedef	struct _SENSOR_CUT_OPTION
{
	bool bEnable;	// 0 disable, 1 enable
	int nTop;
	int nBottom;
	int nLeft;
	int nRight;
}_SENSOR_CUT_OPTION_t;
typedef	struct _MMF_HEADER
{
	unsigned char m_uchHeaderSize;		// Header size of mmf  [max:128]
	unsigned int m_unMemorySize;		// Memory size of mmf  [536870912]
	unsigned short m_ushWidth;			// Width of image
	unsigned short m_ushHeight;			// Height of image
	unsigned int m_unSizeOfImage;		// size of image
	unsigned char m_uchCurrentImage;	// Currnt number of image [start = 0 ]
	unsigned short m_ushCompleteWriteImage;// When software finish writing, change parameter to currnt number of image
										//   and increse m_cCurrntImage
	unsigned char m_uchReadLastImage;		// if (m_cCurrntImage==0), read last memory.
	unsigned short m_ushMaxImage;			// m_unMemorySize / m_unSizeOfImage
	unsigned char m_uchReserved[109];
}_MMF_HEADER_t;
////////////////////////  CALLBACK_ID`s struct   ///////////////////////////
typedef	struct _CBID_SYSTEMINFO
{
	char m_chTemperature;					// -127 ~ 127 Celsius
	unsigned int m_unBatteryGage;			// 1~100
	unsigned int m_unWifiLevel;				// Wifi level (0~100, 110: No WLAN, 111:AP mode)
	unsigned int m_unRDYTime;				// 0~0xFFFF (ms, 0 ~ 65535)
	unsigned int m_unEXPTime;				// 0~180000 (ms, 0 ~ 180 seconds)

	unsigned int m_unTrigger;				// Trigger  1: Auto 6:Manual 7:Soft
	unsigned int m_unLProcess;				// 0: disable 1: enable
	unsigned int m_unBulidVersion;
	unsigned int m_unService;				//
	unsigned int m_unProductType;			// 

	unsigned char m_puchMacAdress[6];			// Mac Address
	unsigned char m_puchLinuxDate[6];			// Linux Date : year,month,day, hour,minutem,sec
	unsigned char m_puchBootVersion[3];			// Boot version
	unsigned char m_puchKernelVersion[3];		// Kernel version
	unsigned char m_puchAppVersion[3];			// App version

	unsigned char m_puchFileSystemVersion[3];	// File System version
	unsigned char m_puchLogicVersion[3];		// Logic version 
	unsigned char m_puchMainMcuVersion[3];		// Main MCU version
	unsigned char m_puchBattMcuVersion[3];		// Batt version
	unsigned char m_puchFactoryDate[3];			// Factory Date : year,month,day, hour,minutem,sec

	unsigned char m_puchSerialNumber[24];		// Detector serial number
	unsigned char m_puchReserved[5];			// 
}CBID_SYSTEMINFO_t;
typedef	struct _CBID_DR_MODEL
{
	int m_nModelID;				// 
	int m_nTriggerType;			// 0:Normal , 1: NDT , 2: MultiFrame
}CBID_DR_MODEL_t;
typedef	struct _CBID_WIRED
{
	unsigned char m_puchIPAddress[4];			// 00.00.00.00
	unsigned char m_puchGateway[4];				// 00.00.00.00
	unsigned char m_puchNetMask[4];				// 00.00.00.00
	unsigned short m_usClinetPort;				// 7000
	unsigned char m_puchReserved[4];
}CBID_WIRED_t;
typedef	struct _CBID_WIFI
{
	unsigned char m_uchNetworkMode;				// 0: Wireless 1: AP MODE 
	unsigned char m_uchDhcpEn;					// 0: DHCP OFF 1: DHCP ON
	unsigned char m_uchAPBand;					// 0: 5G 1: 2.4G
	unsigned char m_uchChannel;					// 1~165
	unsigned char m_puchIPAddress[4];			// 00.00.00.00
	unsigned char m_puchGateway[4];				// 00.00.00.00
	unsigned char m_puchNetMask[4];				// 00.00.00.00
	unsigned short m_usClinetPort;				// 7000
	unsigned char m_puchSSID[32];				// SSID
	unsigned char m_uchEncryption;				// 0:OPEN , 1:WEP , 2:WPA_PSK , 3:WPA2_PSK
	unsigned char m_puchEncryptionPassword[64];	// Encryption Password
	unsigned char m_puchReserved[4];
}CBID_WIFI_t;
typedef	struct _CBID_OTHER
{
	unsigned char m_uchBinning;
	unsigned char m_uchWebView;
	unsigned char m_uchSleepMode;
	unsigned char m_uchChangeWirelessMode;
	unsigned char m_puchReserved[4];
}CBID_OTHER_t;
typedef	struct _CBID_DOWNLOAD_START
{
	unsigned int m_unFileType;
	unsigned int m_unFileSize;
}CBID_DOWNLOAD_START_t;
typedef	struct _CBID_BRIGHT_TIMER
{
	unsigned int m_unRemainingSeconds;
}CBID_BRIGHT_TIMER_t;
typedef	struct _CBID_ERROR
{
	unsigned int m_unLastModule;
	unsigned int m_unErrorCode;
	unsigned char m_puchReserved[4];
}CBID_ERROR_t;
typedef	struct _CBID_ACK
{
	unsigned int m_unAckCode;
	unsigned int m_unParam1;
	unsigned int m_unParam2;
}CBID_ACK_t;
typedef	struct _CBID_ETC
{
	unsigned int m_unType;
	int m_nParam1;
	int m_nParam2;
}CBID_ETC_t;
typedef	struct _CBID_DEADMAN_PARAM
{
	int m_nParam1;
	int m_nParam2;
	int m_nParam3;
}_CBID_DEADMAN_PARAM_t;
typedef	struct _CBID_UPLOAD_START
{
	unsigned int m_unFileType;
	unsigned int m_unFileSize;
}CBID_UPLOAD_START_t;
typedef	struct _CBID_UPLOAD
{
	unsigned int m_unPercent;
}CBID_UPLOAD_t;
typedef	struct _CBID_MMF
{
	PVOID m_MMF;
}_CBID_MMF_t;
typedef	struct _CBID_ETC_DATA
{
	unsigned int m_unType;
	unsigned int m_nSize;
	unsigned char m_pData[120];
}_CBID_ETC_DATA_t;

#if defined(WIN32)
#include <poppack.h>
#else
#pragma pack(pop)
#endif
