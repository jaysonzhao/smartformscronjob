/**
 * SendMailSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface SendMailSoap extends java.rmi.Remote {
    public void sendMailForEIP(java.lang.String strMailFrom, java.lang.String strMailTo, java.lang.String strMailSubject, java.lang.String strMailBody, java.lang.String strCredentialAccount, java.lang.String strCredentialPwd, java.lang.String strMailCC, java.lang.String strMailBcc, javax.xml.rpc.holders.BooleanHolder sendMailForEIPResult, javax.xml.rpc.holders.StringHolder strErrorMsg) throws java.rmi.RemoteException;
}
