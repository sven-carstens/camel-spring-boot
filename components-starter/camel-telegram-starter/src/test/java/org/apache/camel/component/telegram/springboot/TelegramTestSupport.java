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
package org.apache.camel.component.telegram.springboot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.telegram.TelegramComponent;
import org.apache.camel.component.telegram.model.InlineKeyboardButton;
import org.apache.camel.component.telegram.model.OutgoingTextMessage;
import org.apache.camel.component.telegram.model.ReplyKeyboardMarkup;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.test.AvailablePortFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.junit.jupiter.api.BeforeAll;

/**
 * A support test class for Telegram tests.
 */
public class TelegramTestSupport {

    protected static volatile int port;

    protected String chatId;
    
    
    @Autowired
    protected ProducerTemplate template;
    
    @Autowired
    protected CamelContext context;

    @BeforeAll
    public static void initPort() {
        port = AvailablePortFinder.getNextAvailable();
    }

    /**
     * Construct an inline keyboard sample to be used with an OutgoingTextMessage.
     *
     * @param  message OutgoingTextMessage previously created
     * @return         OutgoingTextMessage set with an inline keyboard
     */
    public OutgoingTextMessage withInlineKeyboardContainingTwoRows(OutgoingTextMessage message) {

        InlineKeyboardButton buttonOptionOneI = InlineKeyboardButton.builder()
                .text("Option One - I").build();

        InlineKeyboardButton buttonOptionOneII = InlineKeyboardButton.builder()
                .text("Option One - II").build();

        InlineKeyboardButton buttonOptionTwoI = InlineKeyboardButton.builder()
                .text("Option Two - I").build();

        ReplyKeyboardMarkup replyMarkup = ReplyKeyboardMarkup.builder()
                .keyboard()
                .addRow(Arrays.asList(buttonOptionOneI, buttonOptionOneII))
                .addRow(Arrays.asList(buttonOptionTwoI))
                .close()
                .oneTimeKeyboard(true)
                .build();
        message.setReplyMarkup(replyMarkup);

        return message;
    }

    /**
     * Retrieves a response from a JSON file on classpath.
     *
     * @param  fileName the filename in the classpath
     * @param  clazz    the target class
     * @param  <T>      the type of the returned object
     * @return          the object representation of the JSON file
     */
    public static <T> T getJSONResource(String fileName, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream stream = TelegramTestSupport.class.getClassLoader().getResourceAsStream(fileName)) {
            T value = mapper.readValue(stream, clazz);
            return value;
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load file " + fileName, e);
        }
    }

    
    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                configureCamelContext(context);
            }
            @Override
            public void afterApplicationStart(CamelContext camelContext) {
                //do nothing here
            }

        };
    }

    protected void configureCamelContext(CamelContext context) {
        final TelegramComponent component = new TelegramComponent();

        TelegramApiConfig apiConfig = getTelegramApiConfig();
        component.setBaseUri(apiConfig.getBaseUri());
        component.setAuthorizationToken(apiConfig.getAuthorizationToken());
        chatId = apiConfig.getChatId();

        context.addComponent("telegram", component);
    }

    protected TelegramApiConfig getTelegramApiConfig() {
        return TelegramApiConfig.mock(port);
    }

    

    protected TelegramMockRoutes createMockRoutes() {
        throw new UnsupportedOperationException();
    }

}
