package com.tianbao.buy.utils;

public class VerifyingCodeGenerator {
    private static String NUM = "1234567890";

    private static String NUM_AND_CHAR = "1234567890abcdefghijkmnpqrstuvwxyz";

    public static String createRandom(boolean numberFlag, int length){
        String retStr;
        String strTable = numberFlag ? NUM : NUM_AND_CHAR;
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }
}
