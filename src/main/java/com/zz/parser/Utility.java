package com.zz.parser;

public class Utility {

    public static String accessToString(int access_flags) {
        return accessToString(access_flags, false);
    }

    public static String accessToString(int access_flags, boolean for_class) {
        StringBuilder buf = new StringBuilder();

        int p = 0;
        for (int i = 0; p < Constants.MAX_ACC_FLAG; i++) {
            p = pow2(i);

            if ((access_flags & p) != 0) {
                /* Special case: Classes compiled with new compilers and with the
                 * `ACC_SUPER' flag would be said to be "synchronized". This is
                 * because SUN used the same value for the flags `ACC_SUPER' and
                 * `ACC_SYNCHRONIZED'.
                 */
                if (for_class && ((p == Constants.ACC_SUPER) || (p == Constants.ACC_INTERFACE))){
                    continue;
                }

                buf.append(Constants.ACCESS_NAMES[i] + " ");
            }
        }

        return buf.toString().trim();
    }

    // 向左移一位
    private static int pow2(int n) {
        return 1 << n;
    }
}
