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
package org.apache.camel.component.infinispan.remote;

import static org.apache.camel.component.infinispan.InfinispanConstants.OPERATION;
import static org.apache.camel.component.infinispan.InfinispanConstants.QUERY_BUILDER;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.infinispan.InfinispanOperation;
import org.apache.camel.component.infinispan.InfinispanQueryBuilder;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.support.DefaultMessage;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.MarshallerUtil;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.commons.util.Util;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.sampledomain.User;
import org.infinispan.protostream.sampledomain.marshallers.GenderMarshaller;
import org.infinispan.protostream.sampledomain.marshallers.UserMarshaller;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.infinispan.query.remote.client.impl.MarshallerRegistration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(
		classes = {
				CamelAutoConfiguration.class,
				InfinispanRemoteQueryProducerIT.class
		}
)
public class InfinispanRemoteQueryProducerIT extends InfinispanRemoteQueryTestSupport {

	@Bean
	public InfinispanQueryBuilder noResultQueryBuilder() {
		return qf -> qf.from(User.class).having("name").like("%abc%").build();
	}

	@Bean
	public InfinispanQueryBuilder withResultQueryBuilder() {
		return qf -> qf.from(User.class).having("name").like("%A").build();
	}

	// *****************************
	//
	// *****************************

	@Test
	public void producerQueryOperationWithoutQueryBuilder() throws Exception {
		Exchange request = template.request("direct:start",
				exchange -> exchange.getIn().setHeader(OPERATION, InfinispanOperation.QUERY));
		assertNull(request.getException());

		List<?> queryResult = (List<?>) request.getIn().getBody();
		assertNull(queryResult);
	}

	@Test
	public void producerQueryWithoutResult() {
		producerQueryWithoutResult("direct:start", noResultQueryBuilder());
	}

	@Test
	public void producerQueryWithoutResultAndQueryBuilderFromConfig() {
		producerQueryWithoutResult("direct:noQueryResults", null);
	}

	private void producerQueryWithoutResult(String endpoint, final InfinispanQueryBuilder builder) {
		Exchange request = template.request(endpoint, createQueryProcessor(builder));

		assertNull(request.getException());

		List<User> queryResult = request.getIn().getBody(List.class);
		assertNotNull(queryResult);
		assertEquals(0, queryResult.size());
	}

	@Test
	public void producerQueryWithResult() {
		producerQueryWithResult("direct:start", withResultQueryBuilder());
	}

	@Test
	public void producerQueryWithResultAndQueryBuilderFromConfig() {
		producerQueryWithResult("direct:queryWithResults", null);
	}

	// *****************************
	//
	// *****************************

	@BeforeAll
	protected static void setupResources() throws Exception {
		String proto = Util.read(InfinispanRemoteTestSupport.class.getResourceAsStream("/sample_bank_account/bank.proto"));

		BasicCache<Object, Object> cache = getCacheByName(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
		cache.put("sample_bank_account/bank.proto", proto);

		MarshallerRegistration.init(MarshallerUtil.getSerializationContext(cacheContainer));
		SerializationContext serCtx = MarshallerUtil.getSerializationContext(cacheContainer);
		serCtx.registerProtoFiles(FileDescriptorSource.fromResources("/sample_bank_account/bank.proto"));
		serCtx.registerMarshaller(new UserMarshaller());
		serCtx.registerMarshaller(new GenderMarshaller());
	}

	@BeforeEach
	protected void beforeEach() {
		// cleanup the default test cache before each run
		getCache().clear();

		for (final User user : USERS) {
			getCache().put(createKey(user), user);
		}
	}

	@Bean
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() {
				from("direct:start")
						.toF("infinispan:%s", getCacheName());
				from("direct:noQueryResults")
						.toF("infinispan:%s?queryBuilder=#noResultQueryBuilder", getCacheName());
				from("direct:queryWithResults")
						.toF("infinispan:%s?queryBuilder=#withResultQueryBuilder", getCacheName());
			}
		};
	}

	private void producerQueryWithResult(String endpoint, final InfinispanQueryBuilder builder) {
		Exchange request = template.request(endpoint, createQueryProcessor(builder));
		assertNull(request.getException());

		List<User> queryResult = request.getIn().getBody(List.class);
		assertNotNull(queryResult);
		assertEquals(2, queryResult.size());
		assertTrue(hasUser(queryResult, "nameA", "surnameA"));
		assertTrue(hasUser(queryResult, "nameA", "surnameB"));
	}

	private Processor createQueryProcessor(final InfinispanQueryBuilder builder) {
		return exchange -> {
			exchange.getIn().setHeader(OPERATION, InfinispanOperation.QUERY);
			if (builder != null) {
				exchange.getIn().setHeader(QUERY_BUILDER, builder);
			}
		};
	}
}
