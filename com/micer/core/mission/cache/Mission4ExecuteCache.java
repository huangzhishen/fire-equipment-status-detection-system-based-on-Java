package com.micer.core.mission.cache;

import com.micer.core.mission.Mission;

import java.util.List;

public interface Mission4ExecuteCache {
    public abstract Mission take();

    public abstract void put(List<Mission> paramList);
}
