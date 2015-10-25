#!/bin/sh

mingw32-g++ -shared -Wl,--kill-at -O2 -s -static -ID:/jdk1.7.0_55/include -ID:/jdk1.7.0_55/include/win32 -DMULTI=1 -o mutex_multi.dll testmutex.cpp
mingw32-g++ -shared -Wl,--kill-at -O2 -s -static -ID:/jdk1.7.0_55/include -ID:/jdk1.7.0_55/include/win32 -o mutex_single.dll testmutex.cpp

ls -l *.dll

g++ -shared -fPIC -I/usr/lib/jvm/java-7-oracle/include -I/usr/lib/jvm/java-7-oracle/include/linux -O2 -s -DMULTI=1 -o libmutex_multi.so testmutex.cpp
g++ -shared -fPIC -I/usr/lib/jvm/java-7-oracle/include -I/usr/lib/jvm/java-7-oracle/include/linux -O2 -s -o libmutex_single.so testmutex.cpp

ls -l *.so
