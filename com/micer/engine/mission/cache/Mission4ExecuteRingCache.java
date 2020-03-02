package com.micer.engine.mission.cache;

import com.micer.core.concurrent.ConcurrentRingList;
import com.micer.core.mission.Mission;
import com.micer.core.mission.cache.Mission4ExecuteCache;

import java.util.Iterator;
import java.util.List;

public class Mission4ExecuteRingCache implements Mission4ExecuteCache {

    protected ConcurrentRingList<Mission> missionList = new ConcurrentRingList();

    public Mission4ExecuteRingCache() {}

    public Mission take()
    {
        return (Mission)missionList.next();
    }

    public void put(List<Mission> missionList)
    {
        Iterator<Mission> iterator = missionList.iterator();
        while (iterator.hasNext()) {
            Mission mission = (Mission)iterator.next();
            this.missionList.add(mission);
        }
    }
}
