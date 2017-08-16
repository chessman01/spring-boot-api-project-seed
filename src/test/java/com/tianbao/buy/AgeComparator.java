package com.tianbao.buy;

import java.util.Comparator;

public class AgeComparator implements Comparator<UserGuava> {
    @Override
    public int compare(UserGuava user, UserGuava user1) {
        return user.getAge() > user1.getAge() ? 1 : (user.getAge() == user1.getAge() ? 0 : -1);
    }
}