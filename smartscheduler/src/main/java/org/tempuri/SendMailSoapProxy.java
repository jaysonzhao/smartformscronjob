package org.tempuri;

public class SendMailSoapProxy implements org.tempuri.SendMailSoap {
  private String _endpoint = null;
  private org.tempuri.SendMailSoap sendMailSoap = null;
  
  public SendMailSoapProxy() {
    _initSendMailSoapProxy();
  }
  
  public SendMailSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initSendMailSoapProxy();
  }
  
  private void _initSendMailSoapProxy() {
    try {
      sendMailSoap = (new org.tempuri.SendMailLocator()).getSendMailSoap();
      if (sendMailSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)sendMailSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)sendMailSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (sendMailSoap != null)
      ((javax.xml.rpc.Stub)sendMailSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.SendMailSoap getSendMailSoap() {
    if (sendMailSoap == null)
      _initSendMailSoapProxy();
    return sendMailSoap;
  }
  
  public void sendMailForEIP(java.lang.String strMailFrom, java.lang.String strMailTo, java.lang.String strMailSubject, java.lang.String strMailBody, java.lang.String strCredentialAccount, java.lang.String strCredentialPwd, java.lang.String strMailCC, java.lang.String strMailBcc, javax.xml.rpc.holders.BooleanHolder sendMailForEIPResult, javax.xml.rpc.holders.StringHolder strErrorMsg) throws java.rmi.RemoteException{
    if (sendMailSoap == null)
      _initSendMailSoapProxy();
    sendMailSoap.sendMailForEIP(strMailFrom, strMailTo, strMailSubject, strMailBody, strCredentialAccount, strCredentialPwd, strMailCC, strMailBcc, sendMailForEIPResult, strErrorMsg);
  }
  
  
}