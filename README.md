# Logger Handler

A Custom Logger Handler for WSO2 API Manager to log the times when an API request comes through the `Gateway` component and responded out by the `Gateway` component.

## Build

Build the project by running ...

```shell
mvn clean install
```

## Deploy

After a successful build, copy the `custom-logger-handler-1.0-SNAPSHOT.jar` artifact from the `target` folder and paste it inside `<API-M HOME>/repository/components/lib` folder

And specify the deployed custom handler inside the Handlers section of your API synapse configuration file. You can find your `API Synapse Configurtions` inside the `<API-M HOME>/repository/deployment/server/synapse-configs/default/api` folder.

For this demo, I will choose `_TokenAPI_.xml` from the API Synapse Configuraitons folder, which will look as follows ...

```xml
<api xmlns="http://ws.apache.org/ns/synapse" name="_WSO2AMTokenAPI_" context="/token">
    <resource methods="POST" url-mapping="/*" faultSequence="_token_fault_">
        <inSequence>
            <property name="uri.var.portnum" expression="get-property('keyManager.port')"/>
            <property name="uri.var.hostname" expression="get-property('keyManager.hostname')"/>
            <send>
                <endpoint>
                    <http uri-template="https://{uri.var.hostname}:{uri.var.portnum}/oauth2/token">
                        <timeout>
                            <duration>60000</duration>
                            <responseAction>fault</responseAction>
                        </timeout>
                    </http>
                </endpoint>
            </send>
        </inSequence>
        <outSequence>
            <send/>
        </outSequence>
    </resource>
    <handlers>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerCacheExtensionHandler"/>
        <handler class="org.wso2.carbon.apimgt.gateway.handlers.common.SynapsePropertiesHandler"/>
    </handlers>
</api>
```

Add our custom handler inside the `Handlers` section, which will result in ...

```xml
<handlers>

    <!-- custom logger handler to log the time -->
    <handler class="com.sample.handlers.CustomLoggerHandler" />

    <handler class="org.wso2.carbon.apimgt.gateway.handlers.ext.APIManagerCacheExtensionHandler"/>
    <handler class="org.wso2.carbon.apimgt.gateway.handlers.common.SynapsePropertiesHandler"/>
</handlers>
```

## Enable Log4J Property

Navigate and open the `<API-M HOME>/repository/conf/log4j.properties` file, and append the following line at the bottom

```properties
# enabling custom handler's logger
log4j.logger.com.sample.handlers.CustomLoggerHandler = DEBUG
```

## Run

Start your WSO2 API Manager server by executing the command from your `<API-M HOME>/bin` folder

```shell
# if linux or mac
sh wso2server.sh

# if windows
wso2server.bat
```

or

```shell
wso2am-2.6.0
```

## Test & Results

Assuming that you have published an API to the Store and generated `Access Token` for the related Application in the WSO2 API Manager Store Portal. Use the following cUrl command to invoke the `token` endpoint to generate a new access token

```cUrl
curl -k -d "grant_type=client_credentials" -H "Authorization: Basic <BASE64(client_id:client_secret)>" https://<YOUR IP ADDRESS>/token
```

After a successful execution, You can find the relative logs inside the console in which the WSO2 API Manager was started.

---

You can find more about Creating Custom Handlers for WSO2 API Manager in [here](https://docs.wso2.com/display/AM260/Writing+Custom+Handlers)
