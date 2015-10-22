#!/bin/sh

mingw32-g++ -shared -Wl,--kill-at -O2 -s -static -ID:/jdk1.7.0_55/include -ID:/jdk1.7.0_55/include/win32 -DMULTI=1 -o mutex_multi.dll testmutex.cpp
mingw32-g++ -shared -Wl,--kill-at -O2 -s -static -ID:/jdk1.7.0_55/include -ID:/jdk1.7.0_55/include/win32 -o mutex_single.dll testmutex.cpp

ls -l *.dll
