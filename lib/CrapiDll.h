/** \file crapi_dll.h
 *  \date 2014/07/27
 *	\version 1.0
 *	\author	liujieqing
 *  All Rights Reserved. Copyright(C) CARERAY LTD.2010,2014
 *  \brief This file contains the declaration of functions for export and import
 */
#ifndef __CRAPI_DLL_H__
#define __CRAPI_DLL_H__

#ifdef __cplusplus
extern "C" {
#endif

#ifdef _CRAPI_
#define CRAPI_PORT __declspec(dllexport)
#else
#define CRAPI_PORT __declspec(dllimport)
#endif

////////////////////////////////////////////////////////////////////////////////////////
/** connect detector */
CRAPI_PORT int CR_connect_detector(char*);

/** disconnect detector */
CRAPI_PORT int CR_disconnect_detector();

/** reset the detector */
CRAPI_PORT int CR_reset_detector(int);

/** set the check mode for system **/
CRAPI_PORT int CR_set_check_mode(int);

/** set the sync mode for system **/
CRAPI_PORT int CR_set_sync_mode(int);

/** set the device cycle time */
CRAPI_PORT int CR_set_cycle_time(int, int, int);

/** set the device into the normal power mode **/
CRAPI_PORT int CR_set_normal_power(void);

/** set the device into the save-power mode **/
CRAPI_PORT int CR_set_save_power(void);

/** permit exposure */
CRAPI_PORT int CR_permit_exposure();

/** start grab frame */
CRAPI_PORT int CR_start_acq_full_image();

CRAPI_PORT int CR_start_acq_dark_full_image();

CRAPI_PORT int CR_start_acq_prev_image();

CRAPI_PORT int CR_start_acq_dark_prev();

CRAPI_PORT int CR_start_acq_def_image();

/** stop grab frame */
CRAPI_PORT int CR_stop_acq_frame();

CRAPI_PORT int CR_stop_acq_frame_cleanup();

/** set the correction option in client **/
CRAPI_PORT int CR_set_user_correction(UserCorrection*);

/** get the correction option in client **/
CRAPI_PORT int CR_get_user_correction(UserCorrection*);

/** set the exposure dose */
CRAPI_PORT int CR_set_dose(int);

//get current active detector's id which used for dual detectors system
//out of date
CRAPI_PORT int CR_get_active_detector_ID();

CRAPI_PORT int CR_set_active_detector(int);

CRAPI_PORT int CR_get_dual_detector_state(DetectorActiveState*);

//smart hand switch
CRAPI_PORT int CR_set_smart_hand_switch(SHSParam*);

CRAPI_PORT int CR_get_SHS_status(SHSStatus*);

CRAPI_PORT int CR_get_wireless_ip(char*);

CRAPI_PORT int CR_set_wireless_ip(char*);

CRAPI_PORT int CR_get_station_mode_conf(WirelessStationConf*);

CRAPI_PORT int CR_set_station_mode_conf(WirelessStationConf*);

CRAPI_PORT int CR_get_temperature_cal_data(TrainHead*, int*);

CRAPI_PORT int CR_check_temperature_slot(int nExpTime, float fTemperature);
////////////////////////////////////////////////////////////////////////////////////////

/** read the Careray API version **/
CRAPI_PORT int CR_get_API_Version(char*);

/** check the socket connection state **/
CRAPI_PORT int CR_get_conn_state();

/** read detector type **/
CRAPI_PORT int CR_get_detector_type();

/** get detector information */
CRAPI_PORT int CR_get_detector_info(DetectorInfo*);

/** get mode information */
CRAPI_PORT int CR_get_mode_info(int, ModeInfo*);

/** get status information */
CRAPI_PORT int CR_get_status_info(StatusInfo*);

/** get specified detector's status information */
CRAPI_PORT int CR_get_status_info_Ex(StatusInfo*, int);

/** get the object frame attribution **/
CRAPI_PORT int CR_get_image_attr(FrameAttr*);

/** get one image **/
CRAPI_PORT int CR_get_image(int,BOOL,void*);

CRAPI_PORT int CR_get_unuploaded_img_info(UnuploadedImgInfo*);

/** query the information of the exposure progress for device**/
CRAPI_PORT int CR_query_prog_info(int,ExpProgress*);

CRAPI_PORT int CR_inpaint_bad_pixels(WORD*);	//ÐÞ¸´»µÏñËØ

//////////////////////////////////////////////////////////////////////////
/** CareRay calibration */

// CRAPI_PORT int CR_set_user_cal_dir(char*);
// 
// CRAPI_PORT int CR_load_user_cal_imgs(char*);

//Load user specified calibration files used for correction
CRAPI_PORT int CR_select_cal_type(char*);

//////////////////////////////////////////////////////////////////////////
/** CareRay calibration */
CRAPI_PORT int CR_get_cal_params(CalParams*);

CRAPI_PORT int CR_set_offset_correction(BOOL ofst_corr);

CRAPI_PORT int CR_cal_offset(int);

CRAPI_PORT int CR_linear_calibration(void);

CRAPI_PORT int CR_portable_calibration(void);

CRAPI_PORT int CR_execute_linear_cal();

CRAPI_PORT int CR_execute_portable_cal();

CRAPI_PORT int CR_set_cal_thread(int);

CRAPI_PORT int CR_stop_cal_procedure(BOOL);

CRAPI_PORT int CR_do_gridpattern_filtration(WORD*, int, int, int);

//////////////////////////////////////////////////////////////////////////
//soft sync API
CRAPI_PORT int CR_register_callback(eventCallback);

CRAPI_PORT int CR_send_exp_request(void);

CRAPI_PORT int CR_ready_state_request(void);

CRAPI_PORT int CR_start_soft_acquisition(void);

//AEC acquire API
CRAPI_PORT int CR_set_AEC_integ_time(int prevIntegTime,int radIntegTime);
CRAPI_PORT int CR_register_AEC_callback(eventCallback);
CRAPI_PORT int CR_start_AEC_acquire_process();

//One Key Calibration API
CRAPI_PORT int CR_get_one_key_cal();
CRAPI_PORT int CR_check_img_for_factory_cal(WORD* radImg);
CRAPI_PORT int CR_download_factory_cal_files();
CRAPI_PORT int CR_execute_one_key_cal(WORD *imgData, WORD *darkImgData, SurfaceFitResult* fitResult);

//API for long integration time mode(NDT mode)
CRAPI_PORT int CR_start_acq_NDT_dark_image(int);

CRAPI_PORT int CR_execute_NDT_CAL(UINT*, UINT*, int);

CRAPI_PORT int CR_correct_NDT_image(UINT*, UINT*, int);

CRAPI_PORT int CR_set_NDT_frame_num(int);

CRAPI_PORT int CR_finish_NDT_acquisition();

CRAPI_PORT int CR_check_NDT_CAL_Files();

#ifdef __cplusplus
}
#endif

#endif // __CRAPI_DLL_H__