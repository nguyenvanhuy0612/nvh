package com.avaya.outbound.lib;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * This generic class is used to create SSH session with unix server and executes command.
 *
 * @version 0.1  -  22/01/14   : AAPC Automation Team
 *
 * <font size="-2"> <br><br>
 * Copyright &copy; 2013 Avaya Inc., All Rights Reserved.
 * </br> </br> </font>
 */
public class SSHExecute extends UtilityFun {
    private final Logger log = LogManager.getLogger(SSHExecute.class);

    private Session session;

    private Channel channel;

    private final JSch jsch;

    private UserInfo userInfo;

    private final Properties config;

    public int iExitStatus;


    public SSHExecute() {
        jsch = new JSch();
        Session session = null;
        config = new Properties();
        iExitStatus = 0;
    }


    /**
     * Connect to remote machine and establish session.
     *
     * @return if connect successfully, return true, else return false
     */
    public Boolean connect(String sHostName, String sUserName, String sPassword) {
        try {
            session = jsch.getSession(sUserName, sHostName);
            log.info("Session initialized with Host [" + sHostName + "] and associated with user ["
                    + sUserName + "] and credential " + sPassword);
            //session.setUserInfo(userInfo);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setPassword(sPassword);
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            log.info("SSHExec initialized successfully");
            log.info("SSHExec trying to connect " + sUserName + "@" + sHostName);
            session.connect();
            log.info("SSH connection established");
            return true;
        } catch (Exception e) {
            log.info("Connect fails with the following exception: " + e);
            return false;
        }
    }


    /**
     * Disconnect to remote machine and destroy session.
     *
     * @return if disconnect successfully, return true, else return false
     */
    public Boolean disconnect() {
        try {
            log.info("Closing SSH connection.");
            session.disconnect();
            session = null;
            iExitStatus = 0;
            log.info("SSH connection closed successfully.");
            return true;
        } catch (Exception e) {
            log.info("Disconnect fails with the following exception: " + e);
            return false;
        }
    }


    /**
     * Execute task on remote machine
     *
     * @param sCommand - Task object that extends from CustomCode
     * @throws Exception
     */
    public synchronized String exec(String sCommand) {
        String sRetVal = "";
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(sCommand);
            channel.setInputStream(null);
            channel.setOutputStream(System.out);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();
            log.info("Connection channel established successfully");
            log.info("Start to run command: " + sCommand);
            StringBuilder sb = new StringBuilder();
            byte[] tmp = new byte[1024];
            Date startDate = new Date();
            int timeout = 800;
            while (true) {
                while (in.available() > 0) {
                    Date endDate = new Date();
                    long difference = (endDate.getTime() - startDate.getTime()) / 1000;
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    String str = new String(tmp, 0, i);
                    sb.append(str);
                    System.out.print(str);
                    log.info(str);
                    if (difference >= timeout) {
                        log.info("timed out. command took more than expected");
                        break;
                    }
                }
                if (channel.isClosed()) {
                    log.info("Connection channel closed");
                    //logger.putMsg(Logger.INFO,"Exit-status: " + channel.getExitStatus());
                    log.info("Check if exec success or not ... ");
                    iExitStatus = channel.getExitStatus();
                    //r.sysout = sb.toString();
                    if (iExitStatus == 0) {
                        log.info("Execute successfully for command: " + sCommand);
                        log.info("Execute successfully for command: " + sCommand);
                    } else {
                        log.info("Execution failed while executing command: " + sCommand);
                        log.info("Execution failed while executing command: " + sCommand);
                        log.info("Error message: " + ((ChannelExec) channel).getErrStream());
                        log.info("Error message: " + ((ChannelExec) channel).getErrStream());
                    }
                    break;
                }
                Date endDate = new Date();
                long difference = (endDate.getTime() - startDate.getTime()) / 1000;
                if (difference >= timeout) {
                    log.info("timed out. command took more than expected");
                    break;
                }
            }
            channel.disconnect();
            sRetVal = sb.toString();
        } catch (Exception e) {
        }
        return sRetVal;
    }


    public synchronized String execcmd(String sCommand) {
        String sRetVal = "";
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(sCommand);
            channel.setInputStream(null);
            channel.setOutputStream(System.out);
            ((ChannelExec) channel).setErrStream(System.err);
            //InputStream in = channel.getInputStream();
            InputStream in = channel.getExtInputStream();
            channel.connect();
            log.info("Connection channel established successfully");
            log.info("Start to run command: " + sCommand);
            StringBuilder sb = new StringBuilder();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    String str = new String(tmp, 0, i);
                    sb.append(str);
                    System.out.print("result : " + str);
                    log.info("result : " + str);
                }
                if (channel.isClosed()) {
                    System.out.print("Connection channel closed");
                    log.info("Connection channel closed");
                    break;
                }
            }
            channel.disconnect();
            sRetVal = sb.toString();
        } catch (Exception e) {
            log.info("in exception");
            System.out.print(e);
        }
        return sRetVal;
    }


    public synchronized String execWindows(String sCommand) {
        String sRetVal = "";
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(sCommand);
            channel.setInputStream(null);
            channel.setOutputStream(System.out);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();
            log.info("Connection channel established successfully");
            log.info("Start to run command: " + sCommand);
            StringBuilder sb = new StringBuilder();
            byte[] tmp = new byte[1024];
            Date startDate = new Date();
            int timeout = 60;
            while (true) {
                while (in.available() > 0) {
                    Date endDate = new Date();
                    long difference = (endDate.getTime() - startDate.getTime()) / 1000;
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    String str = new String(tmp, 0, i);
                    sb.append(str);
                    System.out.print(str);
                    log.info(str);
                    if (difference >= timeout) {
                        log.info("timed out. command took more than expected");
                        break;
                    }
                }
                if (channel.isClosed()) {
                    log.info("Connection channel closed");
                    //logger.putMsg(Logger.INFO,"Exit-status: " + channel.getExitStatus());
                    log.info("Check if exec success or not ... ");
                    iExitStatus = channel.getExitStatus();
                    //r.sysout = sb.toString();
                    if (iExitStatus == 0) {
                        log.info("Execute successfully for command: " + sCommand);
                        log.info("Execute successfully for command: " + sCommand);
                    } else {
                        log.info("Execution failed while executing command: " + sCommand);
                        log.info("Execution failed while executing command: " + sCommand);
                        log.info("Error message: " + ((ChannelExec) channel).getErrStream());
                        log.info("Error message: " + ((ChannelExec) channel).getErrStream());
                    }
                    break;
                }
                Date endDate = new Date();
                long difference = (endDate.getTime() - startDate.getTime()) / 1000;
                if (difference >= timeout) {
                    log.info("timed out. command took more than expected");
                    break;
                }
            }
            channel.disconnect();
            sRetVal = sb.toString();
        } catch (Exception e) {
        }
        return sRetVal;
    }
}
