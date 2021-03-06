package org.achartengine.helper;

/**
 * 判断是否重复的动作，防止短时间大量重复或者也可是说是无效的操作
 *
 */
public class RepeatHelper {
    private static final long DEFAULT_TIME_MILLIS = 800;//毫秒
    private static long lastTimeMillis = 0L;

    public static boolean isFastDoubleAction(long maxTimeMillis) {
        long currentTimeMillis = System.currentTimeMillis();
        long diff = currentTimeMillis - lastTimeMillis;
        if (diff < maxTimeMillis) {
            return true;
        } else {
            lastTimeMillis = currentTimeMillis;
            return false;
        }
    }

    public static boolean isFastDoubleAction() {
        //间隔时间视具体项目中需求情况而定
        return isFastDoubleAction(DEFAULT_TIME_MILLIS);
    }

}
