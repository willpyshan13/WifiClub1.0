/**
 * Project Name: itee
 * File Name:	 IteeApplication.java
 * Package Name: cn.situne.itee.manager
 * Date:		 2015-03-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:IteeApplication <br/>
 * Function: FIXME. <br/>
 * Date: 2015-03-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeApplication extends Application {

    private static Stack<Activity> activityStack;
    private static IteeApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    // Returns the application instance
    public static IteeApplication getInstance() {
        return singleton;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
        System.exit(0);
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }
    }

}