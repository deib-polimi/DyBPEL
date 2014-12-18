@echo off
set AEDevelopment=C:\AEDevelopment
set CP=%AEDevelopment%\dist\ae_rtaxisbpel.jar
set CP=%CP%;%AEDevelopment%\dist\ae_rt.jar
set CP=%CP%;%AEDevelopment%\dist\ae_rtbpel.jar
REM *** I specify the ae_rtbpelsvr.jar where IAeBpelAdmin has been modified
set CP=%CP%;C:\apache-tomcat-5.5.27\shared\lib\ae_rtbpelsvr.jar
set CP=%CP%;%AEDevelopment%\dist\ae_wsio.jar
set CP=%CP%;%AEDevelopment%\lib\activation.jar
set CP=%CP%;%AEDevelopment%\lib\axis.jar
set CP=%CP%;%AEDevelopment%\lib\commons-logging.jar
set CP=%CP%;%AEDevelopment%\lib\commons-discovery.jar
set CP=%CP%;%AEDevelopment%\lib\mail.jar
set CP=%CP%;%AEDevelopment%\lib\wsdl4j.jar
set CP=%CP%;%AEDevelopment%\lib\jaxrpc.jar
set CP=%CP%;%AEDevelopment%\lib\saaj.jar

set OUT=ActiveBpelAdmin.wsdl
set LOC=http://127.0.0.1:8080/rdebug/ActiveBpelAdmin
set NS="http://docs.active-endpoints/wsdl/activebpeladmin/2007/01/activebpeladmin.wsdl"
set MAP=-Nhttp://docs.active-endpoints/wsdl/activebpeladmin/2007/01/activebpeladmin.wsdl=org.activebpel.rt.axis.bpel.admin -Nhttp://schemas.active-endpoints.com/activebpeladmin/2007/01/activebpeladmin.xsd=org.activebpel.rt.axis.bpel.admin.types

REM *** This command generates the WSDL file, but we tweak it to have proper param names ***
"%JAVA_HOME%\bin\java" -classpath %CP% org.apache.axis.wsdl.Java2WSDL --style DOCUMENT --use LITERAL -o%OUT% -l%LOC% -n%NS% org.activebpel.rt.bpel.server.admin.rdebug.server.IAeBpelAdmin


REM"%JAVA_HOME%\bin\java" -classpath %CP% org.apache.axis.wsdl.WSDL2Java --wrapArrays --helperGen %MAP% %OUT%

REM"%JAVA_HOME%\bin\java" -classpath %CP% org.apache.axis.wsdl.WSDL2Java --wrapArrays --helperGen --server-side --skeletonDeploy true %MAP% %OUT%
