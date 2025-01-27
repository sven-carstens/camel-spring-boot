/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.pubnub.springboot;

import org.apache.camel.component.pubnub.PubNubConfiguration;
import org.apache.camel.spring.boot.ComponentConfigurationPropertiesCommon;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Send and receive messages to/from PubNub data stream network for connected
 * devices.
 * 
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@ConfigurationProperties(prefix = "camel.component.pubnub")
public class PubNubComponentConfiguration
        extends
            ComponentConfigurationPropertiesCommon {

    /**
     * Whether to enable auto configuration of the pubnub component. This is
     * enabled by default.
     */
    private Boolean enabled;
    /**
     * The component configurations. The option is a
     * org.apache.camel.component.pubnub.PubNubConfiguration type.
     */
    private PubNubConfiguration configuration;
    /**
     * UUID to be used as a device identifier, a default UUID is generated if
     * not passed.
     */
    private String uuid;
    /**
     * Allows for bridging the consumer to the Camel routing Error Handler,
     * which mean any exceptions occurred while the consumer is trying to pickup
     * incoming messages, or the likes, will now be processed as a message and
     * handled by the routing Error Handler. By default the consumer will use
     * the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that
     * will be logged at WARN or ERROR level and ignored.
     */
    private Boolean bridgeErrorHandler = false;
    /**
     * Also subscribe to related presence information
     */
    private Boolean withPresence = false;
    /**
     * Whether the producer should be started lazy (on the first message). By
     * starting lazy you can use this to allow CamelContext and routes to
     * startup in situations where a producer may otherwise fail during starting
     * and cause the route to fail being started. By deferring this startup to
     * be lazy then the startup failure can be handled during routing messages
     * via Camel's routing error handlers. Beware that when the first message is
     * processed then creating and starting the producer may take a little time
     * and prolong the total processing time of the processing.
     */
    private Boolean lazyStartProducer = false;
    /**
     * The operation to perform. PUBLISH: Default. Send a message to all
     * subscribers of a channel. FIRE: allows the client to send a message to
     * BLOCKS Event Handlers. These messages will go directly to any Event
     * Handlers registered on the channel. HERENOW: Obtain information about the
     * current state of a channel including a list of unique user-ids currently
     * subscribed to the channel and the total occupancy count. WHERENOW: Obtain
     * information about the current list of channels to which a uuid is
     * subscribed to. GETSTATE: Used to get key/value pairs specific to a
     * subscriber uuid. State information is supplied as a JSON object of
     * key/value pairs SETSTATE: Used to set key/value pairs specific to a
     * subscriber uuid GETHISTORY: Fetches historical messages of a channel.
     */
    private String operation;
    /**
     * Whether autowiring is enabled. This is used for automatic autowiring
     * options (the option must be marked as autowired) by looking up in the
     * registry to find if there is a single instance of matching type, which
     * then gets configured on the component. This can be used for automatic
     * configuring JDBC data sources, JMS connection factories, AWS Clients,
     * etc.
     */
    private Boolean autowiredEnabled = true;
    /**
     * If Access Manager is utilized, client will use this authKey in all
     * restricted requests.
     */
    private String authKey;
    /**
     * If cipher is passed, all communications to/from PubNub will be encrypted.
     */
    private String cipherKey;
    /**
     * The publish key obtained from your PubNub account. Required when
     * publishing messages.
     */
    private String publishKey;
    /**
     * The secret key used for message signing.
     */
    private String secretKey;
    /**
     * Use SSL for secure transmission.
     */
    private Boolean secure = true;
    /**
     * The subscribe key obtained from your PubNub account. Required when
     * subscribing to channels or listening for presence events
     */
    private String subscribeKey;

    public PubNubConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(PubNubConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getBridgeErrorHandler() {
        return bridgeErrorHandler;
    }

    public void setBridgeErrorHandler(Boolean bridgeErrorHandler) {
        this.bridgeErrorHandler = bridgeErrorHandler;
    }

    public Boolean getWithPresence() {
        return withPresence;
    }

    public void setWithPresence(Boolean withPresence) {
        this.withPresence = withPresence;
    }

    public Boolean getLazyStartProducer() {
        return lazyStartProducer;
    }

    public void setLazyStartProducer(Boolean lazyStartProducer) {
        this.lazyStartProducer = lazyStartProducer;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Boolean getAutowiredEnabled() {
        return autowiredEnabled;
    }

    public void setAutowiredEnabled(Boolean autowiredEnabled) {
        this.autowiredEnabled = autowiredEnabled;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getCipherKey() {
        return cipherKey;
    }

    public void setCipherKey(String cipherKey) {
        this.cipherKey = cipherKey;
    }

    public String getPublishKey() {
        return publishKey;
    }

    public void setPublishKey(String publishKey) {
        this.publishKey = publishKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public String getSubscribeKey() {
        return subscribeKey;
    }

    public void setSubscribeKey(String subscribeKey) {
        this.subscribeKey = subscribeKey;
    }
}