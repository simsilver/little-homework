package com.simsilver.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Dictionary {
    private Hashtable<String, String> mHashTable;
    private StringBuilder mStringBuilder;
    private boolean mStringUpdated;
    private String mKeyString;

    public Dictionary() {
        mHashTable = new Hashtable<>();
        mStringBuilder = new StringBuilder(4096);
        mStringUpdated = false;
        mKeyString = null;
    }


    public void insert(String key, String value) {
        String lastValue = mHashTable.put(key, value);
        if (lastValue == null) {
            mStringBuilder.append(',');
            mStringBuilder.append(key);
            mStringBuilder.append(',');
            mStringUpdated = false;
        }
    }

    public void reset() {
        mHashTable.clear();
        mStringBuilder.setLength(0);
        mStringUpdated = false;
        mKeyString = null;
    }

    public int size() {
        return mHashTable.size();
    }

    public String[] getValue(String subKey) {
        if (!mStringUpdated || (mKeyString == null)) {
            mKeyString = mStringBuilder.toString();
        }
        String regex = ",(" + subKey + "[^,]*),";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mKeyString);
        ArrayList<String> mKeys = new ArrayList<>();
        while (m.find()) {
            mKeys.add(m.group(1));
        }
        int count = mKeys.size();
        if(count == 0) {
            return null;
        }
        String[] values = new String[count];
        for (int i = 0; i < count; i++) {
            String key = mKeys.get(i);
            values[i] = mHashTable.get(key);
        }
        return values;
    }

    public Map.Entry<String, String> getEntry(int i) {
        Set<Map.Entry<String, String>> entries = mHashTable.entrySet();
        if(i < 0 || i > entries.size()) {
            return null;
        }
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while(i-- > 0 && iterator.hasNext()) {
            iterator.next();
        }
        Map.Entry<String, String> entry = iterator.next();
        return entry;
    }
}
