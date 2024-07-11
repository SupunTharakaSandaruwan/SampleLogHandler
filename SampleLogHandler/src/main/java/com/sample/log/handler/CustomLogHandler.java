package com.sample.log.handler;

import org.apache.axis2.AxisFault;
import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.AbstractHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.gateway.APIMgtGatewayConstants;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.axis2.description.WSDL2Constants;
import java.util.Map;


public class CustomLogHandler extends AbstractHandler {

//    private static final Logger log = LoggerFactory.getLogger(CustomLogHandler.class);
    private static final Log log = LogFactory.getLog(CustomLogHandler.class);
    private static final String DIRECTION_IN = "In";
    private static final String DIRECTION_OUT = "Out";

    public String logMessageMediate(MessageContext messageContext, String direction){
        String logMessage = " ";
        org.apache.axis2.context.MessageContext axisMC =
                ((Axis2MessageContext) messageContext)
                        .getAxis2MessageContext();
        Map headers = (Map) axisMC
                .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);


        String apiName = (String) messageContext.getProperty(APIMgtGatewayConstants.API);
        String apiContext= (String) messageContext.getProperty(APIMgtGatewayConstants.CONTEXT);
        String httpMethod= (String) messageContext.getProperty(APIMgtGatewayConstants.HTTP_METHOD);
        String restURLPattern = (String) messageContext.getProperty("REST_FULL_REQUEST_PATH");
        String apiRequstStartTime= (String) messageContext.getProperty(APIMgtGatewayConstants.REQUEST_START_TIME);
        String apiBackendRequestStartTime= (String)
                messageContext.getProperty(APIMgtGatewayConstants.BACKEND_REQUEST_START_TIME);
        String userId= (String) messageContext.getProperty(APIMgtGatewayConstants.USER_ID) ;
        String enduserName= (String) messageContext.getProperty(APIMgtGatewayConstants.END_USER_NAME);
        String applicationId= (String) messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_ID);
        String applicationName= (String) messageContext.getProperty(APIMgtGatewayConstants.APPLICATION_NAME);
        String consumerKey= (String) messageContext.getProperty(APIMgtGatewayConstants.CONSUMER_KEY);
        String endpointAddress= (String) messageContext.getProperty(APIMgtGatewayConstants.SYNAPSE_ENDPOINT_ADDRESS);
        String logID = (String) headers.get(APIConstants.ACTIVITY_ID);

        if (DIRECTION_OUT.equals(direction) && logID == null){
            try {
                org.apache.axis2.context.MessageContext inMessageContext =
                        axisMC.getOperationContext().getMessageContext(WSDL2Constants.MESSAGE_LABEL_IN);
                if (inMessageContext != null) {
                    Object inTransportHeaders =
                            inMessageContext.getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
                    if (inTransportHeaders != null) {
                        String inID = (String) ((Map) inTransportHeaders).get(APIConstants.ACTIVITY_ID);
                        if (inID != null) {
                            logID = inID;
                        }
                    }
                }
            } catch (AxisFault axisFault) {
                //Ignore Axis fault to continue logging
                log.error("Cannot get Transport headers from Gateway", axisFault);
            }
        }
        logMessage = logID+ "|"+
                apiName + "|" +
                apiContext + "|" +
                restURLPattern + "|"+
                httpMethod + "|" +
                endpointAddress +"|"+
                apiRequstStartTime + "|" +
                apiBackendRequestStartTime + "|" +
                userId + "|" +
                enduserName + "|" +
                applicationId + "|" +
                applicationName + "|" +
                consumerKey + "|" ;


       return  logMessage;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext) {
        String direction = DIRECTION_IN;
        org.apache.axis2.context.MessageContext axisMC = ((Axis2MessageContext) messageContext).getAxis2MessageContext();

        log.info("Request flow|"+ logMessageMediate(messageContext, direction) );
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) {
        String direction = DIRECTION_OUT;
        log.info("Request flow|"+logMessageMediate(messageContext, direction));
        return true;
    }
}
