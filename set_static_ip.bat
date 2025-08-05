@echo off
echo Setting Static IP...
netsh interface ipv4 set address name="Wi-Fi" static 192.168.12.10 255.255.255.0 
echo Static IP set successfully!
pause
exit
