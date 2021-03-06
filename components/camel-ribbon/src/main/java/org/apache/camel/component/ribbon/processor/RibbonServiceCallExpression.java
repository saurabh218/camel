/**
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
package org.apache.camel.component.ribbon.processor;

import org.apache.camel.Exchange;
import org.apache.camel.component.ribbon.RibbonConstants;
import org.apache.camel.support.ServiceCallExpressionSupport;
import org.apache.camel.util.ExchangeHelper;

public class RibbonServiceCallExpression extends ServiceCallExpressionSupport {

    public RibbonServiceCallExpression(String name, String scheme, String contextPath, String uri) {
        super(name, scheme, contextPath, uri);
    }

    @Override
    public String getIp(Exchange exchange) throws Exception {
        return ExchangeHelper.getMandatoryHeader(exchange, RibbonConstants.RIBBON_SERVER_IP, String.class);
    }

    @Override
    public int getPort(Exchange exchange) throws Exception {
        return ExchangeHelper.getMandatoryHeader(exchange, RibbonConstants.RIBBON_SERVER_PORT, int.class);
    }

}
