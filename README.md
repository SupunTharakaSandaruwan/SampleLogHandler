# Log Handler

This is Custom Logger Handler for WSO2 API Manager to log following details when an API request comes through the `Gateway` component and responded out by the `Gateway` component.

LOG format

Request <IN|OUT>|CorrelationID|apiName|apiContext|restURLPattern|httpMethod|endpointAddress|apiRequstStartTime|apiBackendRequestStartTime|userId|enduserName|applicationId|applicationName|consumerKey|

## Build

Build the project by running ...

```shell
mvn clean install
```

## Deploy

After a successful build, copy the `com.sample.log.handler-1.0-SNAPSHOT` artifact from the `target` folder and paste it inside `<API-M HOME>/repository/components/lib` folder

And specify the deployed custom handler inside the Handlers section of your API synapse configuration file. You can find your `API Synapse Configurtions` inside the `<API-M HOME>/repository/deployment/server/synapse-configs/default/api` folder.

For this demo, I have taken `admin--PizzaShackAPI_v1.0.0.xml` from the API Synapse Configuraitons folder, which will look as follows ...

```xml
<?xml version="1.0" encoding="UTF-8"?><api xmlns="http://ws.apache.org/ns/synapse" name="admin--PizzaShackAPI" context="/pizzashack/1.0.0" version="1.0.0" version-type="context">
    <resource methods="POST" url-mapping="/order" faultSequence="fault">
        <inSequence>
            <property name="api.ut.backendRequestTime" expression="get-property('SYSTEM_TIME')"/>
            <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
                <then>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIproductionEndpoint"/>
                    </send>
                </then>
                <else>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIsandboxEndpoint"/>
                    </send>
                </else>
            </filter>
        </inSequence>
        <outSequence>
            <class name="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtResponseHandler"/>
            <send/>
        </outSequence>
    </resource>
    <resource methods="GET" url-mapping="/menu" faultSequence="fault">
        <inSequence>
            <property name="api.ut.backendRequestTime" expression="get-property('SYSTEM_TIME')"/>
            <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
                <then>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIproductionEndpoint"/>
                    </send>
                </then>
                <else>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIsandboxEndpoint"/>
                    </send>
                </else>
            </filter>
        </inSequence>
        <outSequence>
            <class name="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtResponseHandler"/>
            <send/>
        </outSequence>
    </resource>
    <resource methods="GET" uri-template="/order/{orderId}" faultSequence="fault">
        <inSequence>
            <property name="api.ut.backendRequestTime" expression="get-property('SYSTEM_TIME')"/>
            <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
                <then>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIproductionEndpoint"/>
                    </send>
                </then>
                <else>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIsandboxEndpoint"/>
                    </send>
                </else>
            </filter>
        </inSequence>
        <outSequence>
            <class name="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtResponseHandler"/>
            <send/>
        </outSequence>
    </resource>
    <resource methods="PUT" uri-template="/order/{orderId}" faultSequence="fault">
        <inSequence>
            <property name="api.ut.backendRequestTime" expression="get-property('SYSTEM_TIME')"/>
            <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
                <then>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIproductionEndpoint"/>
                    </send>
                </then>
                <else>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIsandboxEndpoint"/>
                    </send>
                </else>
            </filter>
        </inSequence>
        <outSequence>
            <class name="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtResponseHandler"/>
            <send/>
        </outSequence>
    </resource>
    <resource methods="DELETE" uri-template="/order/{orderId}" faultSequence="fault">
        <inSequence>
            <property name="api.ut.backendRequestTime" expression="get-property('SYSTEM_TIME')"/>
            <filter source="$ctx:AM_KEY_TYPE" regex="PRODUCTION">
                <then>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIproductionEndpoint"/>
                    </send>
                </then>
                <else>
                    <send>
                        <endpoint key="PizzaShackAPI--v1.0.0_APIsandboxEndpoint"/>
                    </send>
                </else>
            </filter>
        </inSequence>
        <outSequence>
            <class name="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtResponseHandler"/>
            <send/>
        </outSequence>
    </resource>
    <handlers>

        <handler class="org.wso2.carbon.apimgt.gateway.handlers.common.APIMgtLatencyStatsHandler">
            <property name="apiUUID" value="53002210-f0c8-4e86-97db-b6a31092ba2a"/>
        </handler>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.security.CORSRequestHandler">
            <property name="apiImplementationType" value="ENDPOINT"/>
        </handler>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.security.APIAuthenticationHandler">
            <property name="RemoveOAuthHeadersFromOutMessage" value="true"/>
            <property name="APILevelPolicy" value=""/>
            <property name="provider" value="admin"/>
            <property name="keyManagers" value="all"/>
            <property name="CertificateInformation" value="{}"/>
            <property name="APISecurity" value="oauth2,oauth_basic_auth_api_key_mandatory"/>
            <property name="apiUUID" value="53002210-f0c8-4e86-97db-b6a31092ba2a"/>
        </handler>

        <handler class="org.wso2.carbon.apimgt.gateway.handlers.throttling.ThrottleHandler"/>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtUsageHandler"/>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtGoogleAnalyticsTrackingHandler">
            <property name="configKey" value="ga-config-key"/>
        </handler>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerExtensionHandler"/>
    </handlers>
</api>

```

Add our custom handler inside the `Handlers` section, which will result in ...

```xml
<handlers>



  ...
  <handler class="org.wso2.carbon.apimgt.gateway.handlers.analytics.APIMgtGoogleAnalyticsTrackingHandler">
      <property name="configKey" value="ga-config-key"/>
  </handler>
  <handler class="org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerExtensionHandler"/>
    <!-- custom logger handler to log the time -->
    <handler class="com.sample.log.handler.CustomLogHandler" />
</handlers>
```

## Enable Log4J Property

Navigate and open the `<API-M HOME>/repository/conf/log4j2.properties` file, and append the following line at the bottom

```properties
# configuring custom handler's logger
logger.custom-log-handler.name=com.sample.log.handler.CustomLogHandler
logger.custom-log-handler.level=INFO
# enabling custom handler's logger
loggers = ...,MessageTracker, custom-log-handler


```

## Run

Start your WSO2 API Manager server by executing the command from your `<API-M HOME>/bin` folder

```shell
# if linux or mac
sh wso2server.sh

# if windows
wso2server.bat
```



## Test & Results

Assuming that you have published an API to the Devportal and generated `Access Token` for the related Application in the WSO2 API Manager Devportal. Invoke the API.



After a successful execution, You can find the relative logs inside the console in which the WSO2 API Manager was started.

Request flow|cd5b8f88-a4c7-4a51-a85c-fcb7e9f436fb|PizzaShackAPI|/pizzashack/1.0.0|null|GET|/pizzashack/1.0.0/menu|1636989927857|null|admin@carbon.super|admin@carbon.super|1|DefaultApplication|N5TBwVZMq_frfRG4Y4LLJdU92Ica|

Response flow|cd5b8f88-a4c7-4a51-a85c-fcb7e9f436fb|PizzaShackAPI|/pizzashack/1.0.0|/menu|GET|https://localhost:9443/am/sample/pizzashack/v1/api/|1636989927857|1636989928201|admin@carbon.super|admin@carbon.super|1|DefaultApplication|N5TBwVZMq_frfRG4Y4LLJdU92Ica|

---

You can find more about Creating Custom Handlers for WSO2 API Manager in [here](https://apim.docs.wso2.com/en/3.2.0/develop/extending-api-manager/extending-gateway/writing-custom-handlers/#introducing-handlers)
