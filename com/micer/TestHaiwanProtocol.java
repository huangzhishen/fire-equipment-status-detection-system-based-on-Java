package com.micer;

import com.micer.core.codec.MissionRuntime;
import com.micer.core.codec.ProtocolException;
import com.micer.core.network.NetworkException;
import com.micer.core.network.TCPConnection;
import com.micer.core.utils.Utils;
import com.micer.engine.codec.haiwan.Haiwan001Protocol;
import com.micer.engine.codec.haiwan.HaiwanMission;
import org.apache.log4j.Logger;

public class TestHaiwanProtocol {
    public TestHaiwanProtocol()
    {
    }

    public static void main(String args[])
    {
        Logger logger = Logger.getLogger("root");
        Haiwan001Protocol protocol = new Haiwan001Protocol();
        HaiwanMission mission = new HaiwanMission(Utils.uuid(), "0000", 32);
        MissionRuntime runtime = new MissionRuntime();
        System.out.println((new StringBuilder()).append("request: ").append(mission.getRequest()).toString());
        try
        {
            int i = 0;
            do
            {
                String host = "192.168.0.1";
                int port = 8080;
                int con_timeout = 3000;
                int op_timeout = 1000;
                TCPConnection connection = new TCPConnection(host, port, con_timeout, op_timeout);
                connection.newConnect();
                runtime.putConnection(connection);
                try
                {
                    protocol.executeMission(mission, runtime);
                    System.out.println((new StringBuilder()).append(i).append(": result: ").append(runtime.getExecutedResult()).toString());
                    connection.disConnect();
                    i++;
                }
                catch(ProtocolException e)
                {
                    e.printStackTrace();
                }
            } while(true);
        }
        catch(NetworkException e)
        {
            e.printStackTrace();
        }
    }
}
