/**
 * ModificaBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package adapatations.adaptationService_wsdl;

public class ModificaBindingSkeleton implements adapatations.adaptationService_wsdl.ModificaPT, org.apache.axis.wsdl.Skeleton {
    private adapatations.adaptationService_wsdl.ModificaPT impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nomeProcesso"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "operazioniArray"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://sample/wsdl/bpelServer.wsdl", "ArrayOfString"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "attributiArray"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://sample/wsdl/bpelServer.wsdl", "ArrayOfString"), java.lang.String[].class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("modificaOP", _params, new javax.xml.namespace.QName("", "tipo"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "modificaOP"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("modificaOP") == null) {
            _myOperations.put("modificaOP", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("modificaOP")).add(_oper);
    }

    public ModificaBindingSkeleton() {
        this.impl = new adapatations.adaptationService_wsdl.ModificaBindingImpl();
    }

    public ModificaBindingSkeleton(adapatations.adaptationService_wsdl.ModificaPT impl) {
        this.impl = impl;
    }
    public java.lang.String modificaOP(java.lang.String nomeProcesso, java.lang.String[] operazioniArray, java.lang.String[] attributiArray) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.modificaOP(nomeProcesso, operazioniArray, attributiArray);
        return ret;
    }

}
