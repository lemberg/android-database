/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lemberg Solutions
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ls.database.util;

import com.ls.database.BuildConfig;

import android.os.Bundle;
import android.util.Log;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class Logger {

    private String mTag;

    public Logger(String tag) {
        this.mTag = tag;
    }

    /**
     * <p><b>ERROR:</b> This level of logging should be used when something fatal has happened, i.e.
     * something that will have user-visible consequences and won't be recoverable without
     * explicitly deleting some data, uninstalling applications, wiping the data partitions or
     * reflashing the entire phone (or worse). Issues that justify some logging at the ERROR level
     * are typically good candidates to be reported to a statistics-gathering server.</p> <p/>
     * <p><b>This level is always logged.</b></p>
     */
    public void error(String message, Throwable cause) {
        Log.e(mTag, "[" + message + "]", cause);
    }

    /** @see #error(String, Throwable) */
    public void error(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.e(mTag, "[" + callerClassName + "] " + msg);
    }

    /**
     * <p><b>WARNING:</b> This level of logging should used when something serious and unexpected
     * happened, i.e. something that will have user-visible consequences but is likely to be
     * recoverable without data loss by performing some explicit action, ranging from waiting or
     * restarting an app all the way to re-downloading a new version of an application or rebooting
     * the device. Issues that justify some logging at the WARNING level might also be considered
     * for reporting to a statistics-gathering server.</p> <p/> <p><b>This level is always
     * logged.</b></p>
     */
    public void warning(String message, Throwable cause) {
        Log.w(mTag, "[" + message + "]", cause);
    }

    /** @see #warning(String, Throwable) */
    public void warning(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.w(mTag, "[" + callerClassName + "] " + msg);
    }

    /**
     * <p><b>INFORMATIVE:</b> This level of logging should used be to note that something
     * interesting to most people happened, i.e. when a situation is detected that is likely to have
     * widespread impact, though isn't necessarily an error. Such a condition should only be logged
     * by a module that reasonably believes that it is the most authoritative in that domain (to
     * avoid duplicate logging by non-authoritative components).</p> <p/> <p><b>This level is always
     * logged.</b></p>
     */
    public void info(String message, Throwable cause) {
        Log.i(mTag, "[" + message + "]", cause);
    }

    /** @see #info(String, Throwable) */
    public void info(String msg) {
        Throwable t = new Throwable();
        StackTraceElement[] elements = t.getStackTrace();

        String callerClassName = elements[1].getFileName();
        Log.i(mTag, "[" + callerClassName + "] " + msg);
    }

    /**
     * <p><b>DEBUG:</b> This level of logging should be used to further note what is happening on
     * the device that could be relevant to investigate and debug unexpected behaviors. You should
     * log only what is needed to gather enough information about what is going on about your
     * component. If your debug logs are dominating the log then you probably should be using
     * verbose logging.</p> <p/> <p><b>This level is NOT logged in release build.</b></p>
     */
    public void debug(String msg, Throwable cause) {
        if (BuildConfig.DEBUG) {
            Log.d(mTag, msg, cause);
        }
    }

    /** @see #debug(String, Throwable) */
    public void debug(String msg) {
        if (BuildConfig.DEBUG) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();

            String callerClassName = elements[1].getFileName();
            Log.d(mTag, "[" + callerClassName + "] " + msg);
        }
    }

    /**
     * <p><b>VERBOSE:</b> This level of logging should be used for everything else.</p> <p/>
     * <p><b>This level is NOT logged in release build.</b></p>
     */
    public void verbose(String msg, Throwable cause) {
        if (BuildConfig.DEBUG) {
            Log.v(mTag, msg, cause);
        }
    }

    /** @see #verbose(String, Throwable) */
    public void verbose(String msg) {
        if (BuildConfig.DEBUG) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();

            String callerClassName = elements[1].getFileName();
            Log.v(mTag, "[" + callerClassName + "] " + msg);
        }
    }

    public void debug(Bundle bundle) {
        if (BuildConfig.DEBUG) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();

            String callerClassName = elements[1].getFileName();
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                String msg = String.format("%s %s (%s)", key, value.toString(), value.getClass().getName());
                Log.d(mTag, "[" + callerClassName + "] " + msg);
            }
        }

    }
}
