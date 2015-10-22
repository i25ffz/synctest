/****************************************************************************
 **
 ** Copyright (C) 2010-2012 Anonymous Mobile Ltd. All Rights Reserved
 ** File        : testmutex.cpp
 **
 ** Description : testmutex
 **
 ** Creation    : 2012/12/03
 ** Author      : none@anonymous.com
 ** History     :
 **               Creation, 2015/10/21, none, Create the file
 **
 ****************************************************************************/

#include <stdio.h>
#include <jni.h>
#include <pthread.h>

namespace ucg {

    class AutoLock {
    public:
        explicit AutoLock(pthread_mutex_t * _mutex) :
            mutex(_mutex)
        {
            if (pthread_mutex_lock(mutex) == 0)
                locked = true;
            else
                locked = false;
        }

        ~AutoLock() {
            if (locked)
                pthread_mutex_unlock(mutex);
        }

        bool isLocked() const
        {
            return locked;
        }

    private:
        mutable pthread_mutex_t * mutex;
        mutable bool locked;

    };

}

using namespace ucg;

static pthread_mutex_t gMutex = PTHREAD_MUTEX_INITIALIZER;
static long count = 0;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     TestMutex
 * Method:    addCount
 * Signature: ()V
 */
void JNICALL native_addCount(JNIEnv *env, jobject obj)
{
#ifdef MULTI
    // autolock for mutli threads
    AutoLock lock(&gMutex);
#endif

    count++;

    return;
}

/*
 * Class:     TestMutex
 * Method:    getCount
 * Signature: ()J
 */
jlong JNICALL native_getCount(JNIEnv *, jobject)
{
    return count;
}


__attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env;
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK)
		return -1;

    JNINativeMethod methods[] =
    {
		{ "addCount", "()V", (void *) native_addCount },
		{ "getCount", "()J", (void *) native_getCount }
    };

#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))

#ifdef MULTI
    jclass clz = env->FindClass("TestMutexMulti");
#else
    jclass clz = env->FindClass("TestMutexSingle");
#endif

    env->RegisterNatives(clz, methods, NELEM(methods));

    //init mutex
    if (pthread_mutex_init(&gMutex, NULL))
    {
        printf("pthread_mutex_init error!");
    }

	return JNI_VERSION_1_6;
}

#ifdef __cplusplus
}
#endif
