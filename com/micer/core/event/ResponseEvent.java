package com.micer.core.event;

import com.micer.core.mission.Mission;

public class ResponseEvent {

    public String response;
    public String executedStatus;
    public Mission mission;

    public ResponseEvent()
    {
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    public String getExecutedStatus()
    {
        return executedStatus;
    }

    public void setExecutedStatus(String executedStatus)
    {
        this.executedStatus = executedStatus;
    }

    public Mission getMission()
    {
        return mission;
    }

    public void setMission(Mission mission)
    {
        this.mission = mission;
    }
}
