// SensorLib.h : main header file for the SensorLib DLL
//

#pragma once
#ifdef _USRDLL
#define SENSOR_LIB_API __declspec(dllexport)
#else
#define SENSOR_LIB_API __declspec(dllimport)
#endif


////////////////////////////////////////////////////////////////////////////////
/// @class	SensorLib
/// @version	1.0.0
/// @author	Kevin
/// @brief		
////////////////////////////////////////////////////////////////////////////////
extern "C"
{
	//error handler
	SENSOR_LIB_API int GetErrorCode();
	SENSOR_LIB_API int GetCurrentModuleID();

	//general API
	SENSOR_LIB_API int OpenModule(int nModule);
	SENSOR_LIB_API int RunModule(void * vp1 = nullptr, int p2= 0, int p3= 0);
	SENSOR_LIB_API int CloseModule(int nModule);

	SENSOR_LIB_API void SENSOR_NETWORKCALLBACK(
		void(__stdcall *)(int nID, int nMsg, void *pData, int nSize)	// Callback Function (__stdcall) 
	);
}
