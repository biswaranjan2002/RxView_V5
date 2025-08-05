/*
 *  structures.h
 *
 *	Authors:
 *	Liu Jieqing		<jqing.liu@careray.com>	
 *
 *  2009-1-15 File created.
 *
 *  This file is the structures commonly used throughout the 
 *  whole system. Need to increase and improve gradually.
 *
 *
 *  Modification history:
 *  2013-01-21 modified, add some 1500P used structures
 *  and others
 * 2013-02-28 modified, add Correction struct
 */

#ifndef __STRUCTURES_H__
#define __STRUCTURES_H__

#define STRLEN 256
#define DOSELENGTH 8192

/*
 *	Structures used to save the detector information
 *
 *	rawImageWidth ！ The original image width of the detector
 *	rawImageHeight ！ The original image height of the detector
 *	maxPixelValue ！ The max value of each pixel
 *	bitsPerPixel ！ Number of bits per pixel
 *	hardWareVersion[STRLEN] ！ The version of hardware
 *	softWareVersion[STRLEN] ！ The version of software
 *	serialNumber[STRLEN] ！ The serial number of the detector
 *	DetectorDescription[STRLEN] ！ A string that describes the detector, STRLEN is a macro definition, 256
 */
struct DetectorInfo
{
	int rawImageWidth;
	int rawImageHeight;
	int maxPixelValue;
	int bitsPerPixel;
	char hardWareVersion[STRLEN];
	char softWareVersion[STRLEN];
	char serialNumber[STRLEN];
	char detectorDescription[STRLEN];
};

/*
 *	Structure used to save current detector temperature information
 *	maxTemperature ！ Max detector temperature of temperature sensors currently
 *	aveTemperature ！ Average detector temperature of temperature sensors currently
  *	overhot_flag ！ Detector over hot flag
 */
struct Temperature
{
	float reserved[5];
	float maxTemperature;
	float aveTemperature;
	int overhot_flag;		//Status infor, see enum TempStatus 
};

//窮学佚連(Used for 1500P)
struct BatteryInfo
{
	int manu_access;
	int alarm_capa;   //unit: mAh
	int alarm_time;   //unit: min
	int mode;                //range: 0x0000~0xffff
	int atrate;     //-32768mA~32768mA
	int atrate_tofull;   //unit: min
	int atrate_toempty;      //unit: min
	int atrate_ok;        //unit: min
	float temperature;  //unit: C
	float voltage;   //unit: V
	float current;   //unit: A
	float ave_current;   //unit: A
	float max_error;
	float relative_state_of_charge;
	float absolute_state_of_charge; 
	int rest_capacity;         //unit mAh
	int full_capacity;  //unit mAh
	int run_time_to_empty;
	int ave_time_to_empty;
	float charging_current;   //unit: A
	float charging_voltage;   //unit: V
	int battery_status;
	int cycle_count;
	int design_capacity;        //mAh
	float design_voltage;       //V
	float cell4_voltage; //V
	float cell3_voltage; //V
	float cell2_voltage; //V
	float cell1_voltage; //V                      
};

struct WirelessInfo
{
	char essid[32];
	char mode[16];
	char freq[16];
	char channel[16];
	char bit_rate[16];
	char encypt_key[64];
	char security_mode[16];
	char link_quality[16];
	char signal_level[16];		//signal level
	char noise_level[16];
	char sensitivity[16];
	char reserved1[16];         //the first 256 bytes
	unsigned long long tx_packets;
	unsigned long long rx_packets;
	unsigned long long tx_bytes;
	unsigned long long rx_bytes;
	int reserved[64];               //the second 256 bytes  
};

/*
 *	Structure used to save the current detector status information
 *
 *	currentMode ！ Current mode of the detector	
 *	detectorState ！ Current detector state
 *	frameRate ！ Current frame rate
 *	temperature ！ Detector temperature of temperature sensors currently
 */
struct StatusInfo
{
   int checkMode;
   int detectorState;
   float frameRate;
   Temperature temperature;
   BatteryInfo batInfo;
   WirelessInfo wireless_info;
};

/*
 *	Structure to save the current mode infomation.
 *
 *	modeId ！ The mode id for the information retrieved.
 *	acqType ！ Acquisition type. Variable is set to 0 for radiograph modes or 1 for fluoroscopy modes.
 *	imageWidth ！ The number of column of an image in current mode.
 *	imageHeight ！ The number of line of an image  in current mode.
 *	linesPerPixel ！ The number of lines per pixel, >1 when binning is used.
 *	colsPerPixel ！ The number of column per pixel, >1 when binning is used.
 *	imageSize ！ The size of image in byte.
 *	maxFrameRate ！ The maximum allowed frame rate for the mode.
 *	modeDescription ！ A string that describes the mode.
 */
struct ModeInfo
{
	int modeId;
	int acqType;
	int imageWidth;
	int imageHeight;
	int linesPerPixel;	
	int colsPerPixel;
	int imageSize;	
	float maxFrameRate;
	char modeDescription[STRLEN];
};

/*     
*  Structure used to save the correction option.
*	fixedCorr ！ TRUE enable fixed method correction;FALSE disable
*	non_fixedCorr ！ TRUE enable portable method correction for fixed detector; FALSE disable.
*	portableCorr ！ TRUE enable portable method correction for mobile detector; FALSE disable.
*/
struct UserCorrection
{
	BOOL fixedCorr;
	BOOL non_fixedCorr;
	BOOL portableCorr;
};

/*     
 *  Structure used to get rad mode exposure parameters.
 *
 *  expStatus ！ see ExposureStatus
 *	inside_offs_corrflag ！ 
  *	realtime_offset ！ if contain offset image int the first image
 *	frame_number ！ The number of frames acquired.
 *	fetchable ！ The acquisition or calibration process is complete or not. FALSE: completed, TRUE: complete.
 * calComplete  ！ TRUE: calibration complete, FALSE: calibration in progress or not in progress.
 * errorCode ！ Non zero means an error occured.
 * expose_flag ！ expose flag used in auto or manual sync
 */
struct ExpProgress
{
	int expStatus;
	BOOL inside_offs_corrflag;
	BOOL realtime_offset;
	int frame_number;
	BOOL fetchable;
	int errorCode;
	BOOL calComplete;	//before is scan_phase
	BOOL expose_flag;
};

struct FrameAttr
{
	int image_width;       //!<The object image width of the detector
	int image_height;      //!<The object image height of the detector
	int pixel_bits;			//!<the number of every pixel of the object image.
	int image_datatype;	//UINT_16 or UINT_32
};

struct UnuploadedImgInfo
{
	BOOL existFlag;	//resume image flag
	int width;
	int height;
	float finishedRate;	//transferred rate
	BOOL withHead;
	int bitDepth;
};

struct  CalParams
{
	char gain_image_dir[STRLEN];
	char portable_cal_kv[STRLEN];//used for 1500Rm or 1500P and so on
	char normal_cal_kv[STRLEN];	//used for 1500R or 500M and so on
	int ofst_cal_num;			//offset image number in dark calibration
	int linear_dose_num;		//dose number in gain calibration
	int linear_num_per_dose;	//exposure number per dose in gain calibration
	int time_interval;			//every calibration image's time interval
};

struct  DetectorActiveState
{
	int detectorNum;			//1, single; 2, dual detectors
	int detectorAType;
	int detectorBType;
	int activeDetectorID;
	BOOL detectorAstate;	//TRUE, connected
	BOOL detectorBstate;	//TRUE, connected
};

struct SurfaceFitResult
{
	float R2; //calibration threshold
	int x0; //reserved
	int y0;//reserved
	float param1;//reserved
	float param2;//reserved
	float param3;//reserved
	float param4;//reserved
};

struct irDA
{
	int irDAEnable;
	int irDAOffset;
	int irDAPulse;
	int irDAIHTL;
	int irDAILTL;
	int irDAGain;
	int irDADrive;
};

struct SHSParam
{
	char hardwareVersion[STRLEN];
	char pairedID[STRLEN];
	int heartbeatInterval;
	int timeoutPeriod;	//unit: min, timeout for detector to shut down
	int buzzerEnabled;
	int touchSensorEnabled;
	irDA irDACfg;
	int handSwitchEnabled;
};

struct  SHSStatus
{
	int connectedToDetector;
	int beingTouched;
	int batteryRemainingCapacity;
	int signalIntensity;
	int secondsLeft;	//seconds left to shut down detector
	SHSParam handSwitchSettings;
};

struct WirelessStationConf
{ 
	char wireless_station_SSID[STRLEN];
	char wireless_mode[STRLEN];
	char wireless_channel[STRLEN];
};

struct TrainHead
{
	float temperature;
	int integTime;
	long generatedTime;
	int reserved[5];
};

struct TemperatureSlotWarnMsg
{
	int nDetectorID;
	int nMissingExpTime;
	float fMissingTemperature;
	float fCurrentTemperature;
	float fToleranceTemperature;
};

struct TemperatureSlotTrainMsg
{
	int nDetectorID;
	int nExpTimeOfSlot;
	float fTemperatureOfSlot;
};

enum DetectorType
{
	CareView_1500R,
	CareView_1500Rm,
	CareView_1500P,
	CareView_1500C,
	CareView_1800R,
	CareView_500M,
	CareView_500I,
	CareView_500P,
	CareView_900F,
	CareView_1800I,
	CareView_1500L,
	CareView_1500Cw,
	CareView_300P,
	CareView_750M,
	CareView_1800L,
	CareView_1800RV2,
	CareView_1500PV2,
	CareView_750Cw,
	CareView_0331IF,
	CareView_750I,
	CareView_1800IF,
	CareView_750C,
	CareView_1800Cw,
	CareView_1500S,
	CareView_750S,
	CareView_750Mc,
	CareView_240I,
	CareView_1800RF,
	CareView_1800LE,
	CareView_1800CwE,
	AniView_D4343,
	CareView_1500CwE,
	CareView_1500Sf,
	CareView_750Sf,
	Unkown_Type           //隆岑侏催
};

/* 
 * Enum for detector mode
 * 
 * CR_RAD_MODE ！ Rad mode
 * CR_FLUORO_MODE ！ Fluoro mode
 * CR_LAGFLUORO_MODE ！ Lag testing mode
 * CR_CUSTFLUORO_MODE ！ Customized fluoro mode
 */
enum CheckMode
{
	MODE_UNDEFINED = 0,
	MODE_RAD=0x10,  
	MODE_TEST = 0x14,        //!<used just for self-test
	MODE_BIN22 = 0x15,
	MODE_NDT = 0x16,     //NDT mode, for industry application.
	MODE_PREV= 0x17,      //preview mode, for 500M
	MODE_MAXID
};

/* 
 * Enum for detector state
 * 
 * CR_BOOT ！ Detector is booting
 * CR_STANDBY ！ Detector is ready, waiting for command
 * CR_ACQUISTION ！ Detector is in image acquisition state
 * CR_SLEEP ！ Detector is in sleeping mode
 * CR_ERROR ！ An error occured
 */
enum DetectorState
{
	CR_BOOT,
	CR_STANDBY,
	CR_ACQUISTION,
	CR_SLEEP,
	CR_ERROR
};

/* 
 * Enum for exposure state
 *
 * CR_EXP_ERROR ！ An error occurred
 * CR_EXP_INIT ！ Detector is initiating
 * CR_EXP_READY ！ Detector is ready for x-ray pulse
 * CR_EXP_WAIT_PERMISSION ！ X-ray pulse comes. Detector is waiting for PC｀s exposure permission.
 * CR_EXP_PERMITTED ！ PC¨s permission is received
 * CR_EXP_EXPOSE ！ X-ray exposure is in progress
 * CR_EXP_COMPLETE ！ Exposure complete, image will be ready soon
  * EXP_STANDBY ！ no use
 */
enum ExposureStatus
{
	CR_EXP_ERROR = -1,
	CR_EXP_INIT,
	CR_EXP_READY,
	CR_EXP_WAIT_PERMISSION,
	CR_EXP_PERMITTED,
	CR_EXP_EXPOSE,
	CR_EXP_COMPLETE,
};

/* 
 * Enum for progress type to query
 *
 * CR_RAD_PROG ！ Query rad acquisition progress
 * CR_CAL_PROG ！ Query calibration progress
 */
enum ProgType
{
	CR_RAD_PROG,  //Query rad acquisition progress
	CR_CAL_PROG	  //Query calibration progress
};

//揖化庁塀
enum SyncMode
{
	UNDEF_SYNC,		//undefined sync mode
	EXT_SYNC,			//external sync mode (exist sync cable)
	SOFT_SYNC,			//software sync mode
	AUTO_SYNC,			//auto sync mode
	MANUAL_SYNC,		//manual sync mode
	SCAN_SYNC,
	AED_SYNC
};

/*!
@enum POWER_MODE_ID
@孔債庁塀
@brief power mode IDs, provide multiplex power management pattern for detector.
*/
enum PowerMode
{
	PWR_STANDBY,                //set front-end into nap mode
	PWR_FULL_RUNNING,		//set front-end into normal mode.
	PWR_SMART_RUNNING,	//set front-end into nap mode at the integration phase.
	PWR_DOWN_FE = 4,               //set front-end into power-down mode.               
	PWR_SLEEPING,                //unsupported
	PWR_DEEP_SLEEPING,		//unsupported
	PWR_SUSPEND                 //unsupported
};

enum ImageDataType
{
	UINT_16,		//full image
	UINT_32,
	INT_16,
	INT_32,
	FLOAT_32,
	DOUBLE_64
};

enum TempStatus
{
	IN_NORMAL_TEMP=0,       //status indicator turns green
	IN_WARN_TEMP,				//status indicator turns yellow
	OVER_MAX_TEMP_LIMIT,  //status indicator turns red, and take the detector into low power, neither enter to 'ready' state.
	OVER_MIN_TEMP_LIMIT,   //status indicator turns red.
	INVAILD_TEMP					//invalid temperature turns orange
};

enum DetectorIndex
{
	DETECTOR_I,
	DETECTOR_II
};

enum TemperatureSlotStatus
{
	TEMP_SLOT_EXIST,
	TEMP_SLOT_ABSENCE,
};

//////////////////////////////////////////////////////////////////////////
enum event_id
{
	EVT_DISCONNECT,
	EVT_READY,
	EVT_EXP_EN,
	EVT_IMAGE_ARRIVE,
	EVT_AEC_PREV_MODE_READY,
	EVT_AEC_RAD_MODE_READY,
	EVT_AEC_PREV_IMG_ARRIVE,
	EVT_AEC_RAD_IMG_ARRIVE,
	EVT_DETECTOR_ERROR,
	EVT_EXPOSE_FLAG_FALSE,
	EVT_EXPOSE_FLAG_TRUE,
	EVT_INITIAL,
	EVT_UNUPLOADED_IMG_EXIST,
	EVT_CONNECTED,
	EVT_SECOND_PANEL_CONNECTED,
	EVT_SECOND_PANEL_DISCONNECTED,
	EVT_SHS_STATUS_CHANGED,
	EVT_IMAGE_TRANSFER_FAILED,
	EVT_TEMPERATURE_SLOT_MISSING,
	EVT_TEMPERATURE_SLOT_TRAIN_START,
	EVT_TEMPERATURE_SLOT_TRAIN_FINISH,
	EVT_TEMPERATURE_SLOT_TRAIN_ABORT
};

struct Event
{
	int width;
	int height;
	int bits;
	void* data;
};

typedef void (__stdcall*eventCallback)(int eventID, Event* eventData);

#endif // __STRUCTURES_H__