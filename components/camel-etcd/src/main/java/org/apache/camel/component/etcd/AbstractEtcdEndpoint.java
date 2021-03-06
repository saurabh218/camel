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
package org.apache.camel.component.etcd;

import java.net.URI;
import javax.net.ssl.SSLContext;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.EtcdSecurityContext;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a etcd endpoint.
 */
@UriEndpoint(scheme = "etcd", title = "etcd", syntax = "etcd:namespace/path", consumerClass = AbstractEtcdConsumer.class, label = "etcd")
public abstract class AbstractEtcdEndpoint extends DefaultEndpoint {

    @UriPath(description = "The API namespace to use", enums = "keys,stats,watch")
    @Metadata(required = "true")
    private final EtcdNamespace namespace;
    @UriPath(description = "The path the enpoint refers to")
    @Metadata(required = "false")
    private final String path;
    @UriParam
    private final EtcdConfiguration configuration;

    protected AbstractEtcdEndpoint(String uri, EtcdComponent component, EtcdConfiguration configuration, EtcdNamespace namespace, String path) {
        super(uri, component);

        this.configuration = configuration;
        this.namespace = namespace;
        this.path = path;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public EtcdConfiguration getConfiguration() {
        return this.configuration;
    }

    public EtcdNamespace getNamespace() {
        return this.namespace;
    }

    public String getPath() {
        return this.path;
    }

    public EtcdClient createClient() throws Exception {
        String[] uris;
        if (configuration.getUris() != null) {
            uris = configuration.getUris().split(",");
        } else {
            uris = EtcdConstants.ETCD_DEFAULT_URIS.split(",");
        }

        URI[] etcdUriList = new URI[uris.length];

        int i = 0;
        for (String uri : uris) {
            etcdUriList[i++] = URI.create(getCamelContext().resolvePropertyPlaceholders(uri));
        }

        return new EtcdClient(
            new EtcdSecurityContext(
                createSslContext(configuration),
                configuration.getUserName(),
                configuration.getPassword()),
            etcdUriList
        );
    }

    private SSLContext createSslContext(EtcdConfiguration configuration) throws Exception {
        if (configuration.getSslContextParameters() != null) {
            return configuration.getSslContextParameters().createSSLContext(getCamelContext());
        }
        return null;
    }
}
