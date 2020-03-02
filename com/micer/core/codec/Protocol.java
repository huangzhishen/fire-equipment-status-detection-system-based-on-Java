package com.micer.core.codec;

import com.micer.core.event.ResponseEvent;
import com.micer.core.mission.Mission;

import java.util.List;
import java.util.Map;

public interface Protocol {
    public abstract List<Mission> encodeMission(Map<String, Object> paramMap)
            throws ProtocolException;

    public abstract ResponseEvent executeMission(Mission paramMission, MissionRuntime<String, Object> paramMissionRuntime)
            throws ProtocolException;

    public abstract void decodeMission(Mission paramMission, MissionRuntime<String, Object> paramMissionRuntime)
            throws ProtocolException;
}
