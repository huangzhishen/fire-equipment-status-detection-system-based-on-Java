package com.micer.core.network;

import org.apache.log4j.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPConnection extends AbstractStreamConnection{
    Logger tcpConnLogger = Logger.getLogger("tcpConnFileLogger");
    Logger flowWriteLogger = Logger.getLogger("flowWriteFileLogger");
    Logger flowReadLogger = Logger.getLogger("flowReadFileLogger");

    public String host;
    public int port;
    public Socket socket;
    public int con_timeout;
    public int op_timeout;

    public TCPConnection(String host, int port, int con_timeout, int op_timeout)
    {
        this.host = host;


        this.port = port;
        this.con_timeout = con_timeout;
        this.op_timeout = op_timeout;
    }

    public DataOutputStream getDataOutputStream() throws NetworkException
    {
        DataOutputStream dataOutputStream = null;
        if (dataOutputStream != null) {
            return dataOutputStream;
        }
        isActive = false;
        throw new NetworkException("Network error at " + host + ":" + port + " (TCP)");
    }

    public DataInputStream getDataInputStream() throws NetworkException {
        if (dataInputStream != null) {
            return dataInputStream;
        }
        isActive = false;
        throw new NetworkException("Network error at " + host + ":" + port + " (TCP)");
    }

    public void write(int arg) throws NetworkException
    {
        if (dataOutputStream != null) {
            try {
                dataOutputStream.write(arg);
                flowWriteLogger.info(currentTime() + " connection write success");
            } catch (IOException e) {
                flowWriteLogger.info(currentTime() + " connection write fail");
                isActive = false;
                throw new NetworkException("Network error at " + host + ":" + port + " (TCP)", e);
            }
        } else {
            flowWriteLogger.info("connection write fail");
            isActive = false;
            throw new NetworkException("Network error at " + host + ":" + port + " (TCP)", new NullPointerException());
        }
    }

    public void flush() throws NetworkException
    {
        if (dataOutputStream != null) {
            try {
                dataOutputStream.flush();
            } catch (IOException e) {
                isActive = false;
                throw new NetworkException("Network error at " + host + ":" + port + " (TCP)", e);
            }
        } else {
            isActive = false;
            throw new NetworkException("Network error at " + host + ":" + port + " (TCP)", new NullPointerException());
        }
    }

    public int read()
            throws NetworkException
    {
        if (dataInputStream != null) {
            try {
                int result = dataInputStream.read();
                flowReadLogger.info(currentTime() + " connection read success");
                return result;
            } catch (IOException e) {
                flowReadLogger.info(currentTime() + " connection read fail");
                isActive = false;
                throw new NetworkException("Network error at " + host + ":" + port + " (TCP read)", e);
            }
        }
        flowReadLogger.info("connection read fail, dataInputStream = null");
        isActive = false;
        throw new NetworkException("Network error at " + host + ":" + port + " (TCP)", new NullPointerException());
    }

    public void newConnect() throws NetworkException
    {
        disConnect();
        try {
            socket = TimedSocket.getTCPSocket(host, port, con_timeout);
            socket.setSoTimeout(op_timeout);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            isActive = true;
            tcpConnLogger.info(currentTime() + " tcp connection success");
        }
        catch (InterruptedIOException e)
        {
            tcpConnLogger.info(currentTime() + " tcp connection fail");
            throw new NetworkException("Could not connect " + host + ":" + port + " for " + con_timeout + " milliseconds", e);
        }
        catch (IOException e) {
            tcpConnLogger.info(currentTime() + " tcp connection fail");
            throw new NetworkException("Network error while connecting to " + host + ":" + port + " (TCP)", e);
        }
    }

    public void disConnect()
    {
        if (socket != null) {
            try {
                socket.close();
            }
            catch (IOException localIOException) {}
        }
        socket = null;
        isActive = false;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCon_timeout() {
        return con_timeout;
    }

    public void setCon_timeout(int con_timeout) {
        this.con_timeout = con_timeout;
    }

    public int getOp_timeout() {
        return op_timeout;
    }

    public void setOp_timeout(int op_timeout) {
        this.op_timeout = op_timeout;
    }

    public void writeUTF(String str)
            throws NetworkException
    {
        if (dataOutputStream != null) {
            try {
                dataOutputStream.writeUTF(str);
            } catch (IOException e) {
                isActive = false;
                throw new NetworkException("Network error at " + host + ":" + port + " (TCP)", e);
            }
        } else {
            isActive = false;
            throw new NetworkException("Network error at " + host + ":" + port + " (TCP)", new NullPointerException());
        }
    }

    public static String currentTime()
    {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdFormatter.format(nowTime);
    }
}
