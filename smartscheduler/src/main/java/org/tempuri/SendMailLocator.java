/**
 * SendMailLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class SendMailLocator extends org.apache.axis.client.Service implements org.tempuri.SendMail {

    public SendMailLocator() {
    }


    public SendMailLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SendMailLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SendMailSoap
    private java.lang.String SendMailSoap_address = "http://10.128.97.145:8891/sendmail.asmx";

    public java.lang.String getSendMailSoapAddress() {
        return SendMailSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SendMailSoapWSDDServiceName = "SendMailSoap";

    public java.lang.String getSendMailSoapWSDDServiceName() {
        return SendMailSoapWSDDServiceName;
    }

    public void setSendMailSoapWSDDServiceName(java.lang.String name) {
        SendMailSoapWSDDServiceName = name;
    }

    public org.tempuri.SendMailSoap getSendMailSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SendMailSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSendMailSoap(endpoint);
    }

    public org.tempuri.SendMailSoap getSendMailSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.SendMailSoapStub _stub = new org.tempuri.SendMailSoapStub(portAddress, this);
            _stub.setPortName(getSendMailSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSendMailSoapEndpointAddress(java.lang.String address) {
        SendMailSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.SendMailSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.SendMailSoapStub _stub = new org.tempuri.SendMailSoapStub(new java.net.URL(SendMailSoap_address), this);
                _stub.setPortName(getSendMailSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SendMailSoap".equals(inputPortName)) {
            return getSendMailSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "SendMail");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "SendMailSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SendMailSoap".equals(portName)) {
            setSendMailSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
