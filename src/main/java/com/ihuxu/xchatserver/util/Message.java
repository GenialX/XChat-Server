package com.ihuxu.xchatserver.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Message {
    @SuppressWarnings("unused")
    private final static byte start = 0x02;
    @SuppressWarnings("unused")
    private final static byte end = 0x03;

    private final static HashMap<Byte, ArrayList<Byte>> escapeMap;
    static {
        escapeMap = new HashMap<Byte, ArrayList<Byte>>();
        escapeMap.put(new Byte((byte) 0x02), new ArrayList<Byte>() {
            {
                add(new Byte((byte) 0x04));
                add(new Byte((byte) 0x02));
            }
        });
        escapeMap.put(new Byte((byte) 0x04), new ArrayList<Byte>() {
            {
                add(new Byte((byte) 0x04));
                add(new Byte((byte) 0x04));
            }
        });
    }

    /**
     * 转义.
     * 
     * @param data
     */
    public static Byte[] escape(Byte[] data) {
        Byte result[] = null;
        return result;
    }
    
    public static Byte[] reverseEscape(Byte[] data) {
        Byte result[] = null;
        return result;
    }
}
