package com.tianbao.buy;

import java.util.Comparator;

public class NameComparator implements Comparator<UserGuava> {
    @Override
    public int compare(UserGuava user, UserGuava user1) {
        return user.getName().compareTo(user1.getName());
    }
}