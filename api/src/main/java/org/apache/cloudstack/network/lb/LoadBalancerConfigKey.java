// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.network.lb;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.cloudstack.framework.config.ConfigKey;
import org.apache.cloudstack.network.lb.LoadBalancerConfig.SSLConfiguration;

import org.apache.commons.lang3.StringUtils;

import com.cloud.utils.Pair;

public enum LoadBalancerConfigKey {

    LbStatsEnable(Category.Stats, "lb.stats.enable", "LB stats enable", Boolean.class, "true", "Enable statistics reporting with default settings, default is 'true'", ConfigKey.Scope.Network),

    LbStatsUri(Category.Stats, "lb.stats.uri", "LB stats URI", String.class, "/admin?stats", "Enable statistics and define the URI prefix to access them, default is '/admin?stats'", ConfigKey.Scope.Network),

    LbStatsAuth(Category.Stats, "lb.stats.auth", "LB stats auth", String.class, "admin1:AdMiN123", "Enable statistics with authentication and grant access to an account, default is 'admin1:AdMiN123'", ConfigKey.Scope.Network),

    GlobalStatsSocket(Category.Stats, "global.stats.socket", "Stats socket enabled/disabled", Boolean.class, "false", "Binds a UNIX socket to /var/run/haproxy.socket, default is 'false'", ConfigKey.Scope.Network),

    LbTimeoutConnect(Category.General, "lb.timeout.connect", "Maximum time (in ms) to wait for a connection to succeed", Long.class, "5000", "Set the maximum time to wait for a connection attempt to a server to succeed.", ConfigKey.Scope.Network),

    LbTimeoutServer(Category.General, "lb.timeout.server", "Maximum inactivity time (in ms) on server side", Long.class, "50000", "Set the maximum inactivity time on the server side.", ConfigKey.Scope.Network),

    LbTimeoutClient(Category.General, "lb.timeout.client", "Maximum inactivity time (in ms) on client side", Long.class, "50000", "Set the maximum inactivity time on the client side.", ConfigKey.Scope.Network),

    LbHttp(Category.LoadBalancer, "lb.http", "LB http enabled/disabled", Boolean.class, "true", "If LB is http, default is 'true' for port 80 and 'false' for others'", ConfigKey.Scope.Network),

    LbHttp2(Category.LoadBalancer, "lb.http2", "Enable/disable HTTP2 support", Boolean.class, "false", "Enable or disable HTTP2 support in HAproxy", ConfigKey.Scope.Network),

    LbHttpKeepalive(Category.LoadBalancer, "lb.http.keepalive", "LB http keepalive enabled/disabled", Boolean.class, "true", "Enable or disable HTTP keep-alive, default is inherited from network offering", ConfigKey.Scope.Network),

    LbBackendHttps(Category.LoadBalancer, "lb.backend.https", "If backend server is https", Boolean.class, "false", "If backend server is https. If yes, use 'check ssl verify none' instead of 'check'", ConfigKey.Scope.Network),

    LbTransparent(Category.LoadBalancer, "lb.transparent.mode", "LB transparent mode enabled/disabled", Boolean.class, "false", "Enable or disable transparent mode, default is 'false'", ConfigKey.Scope.Network),

    GlobalMaxConn(Category.LoadBalancer, "global.maxconn", "LB max connection", Long.class, "4096", "Maximum per process number of concurrent connections, default is '4096'", ConfigKey.Scope.Network),

    GlobalMaxPipes(Category.LoadBalancer, "global.maxpipes", "LB max pipes", Long.class, "1024", "Maximum number of per process pipes, default is 'maxconn/4'", ConfigKey.Scope.Network),

    LbMaxConn(Category.LoadBalancer, "lb.maxconn", "LB max connection", Long.class, "2000", "Maximum per process number of concurrent connections per site/vm", ConfigKey.Scope.Network),

    LbFullConn(Category.LoadBalancer, "lb.fullconn", "LB full connection", Long.class, "10", "Specify at what backend load the servers will reach their maxconn, default is 'maxconn/10'", ConfigKey.Scope.Network),

    LbServerMaxConn(Category.LoadBalancer, "lb.server.maxconn", "LB max connection per server", Long.class, "0", "LB max connection per server, default is ''", ConfigKey.Scope.Network),

    LbServerMinConn(Category.LoadBalancer, "lb.server.minconn", "LB minimum connection per server", Long.class, "", "LB minimum connection per server, default is ''", ConfigKey.Scope.Network),

    LbServerMaxQueue(Category.LoadBalancer, "lb.server.maxqueue", "Max conn wait in queue per server", Long.class, "0", "Maximum number of connections which will wait in queue for this server, default is ''", ConfigKey.Scope.Network),

    LbSslConfiguration(Category.LoadBalancer, "lb.ssl.configuration", "SSL configuration, could be 'none', 'old' or 'intermediate'", String.class, "Inherited from global setting" , "if 'none', no SSL configurations will be added, if 'old', refer to https://ssl-config.mozilla.org/#server=haproxy&server-version=1.8.17&config=old&openssl-version=1.0.2l if 'intermediate', refer to https://ssl-config.mozilla.org/#server=haproxy&server-version=1.8.17&config=intermediate&openssl-version=1.0.2l default value is 'none'", ConfigKey.Scope.Network);

    public enum Category {
        General, Advanced, Stats, LoadBalancer
    }

    private final Category _category;
    private final ConfigKey.Scope[] _scope;
    private final String _key;
    private final String _displayText;
    private final String _description;
    private final Class<?> _type;
    private final String _defaultValue;

    private LoadBalancerConfigKey(Category category, String key, String displayText, Class<?> type, String defaultValue, String description, ConfigKey.Scope... scope) {
        _category = category;
        _scope = scope;
        _key = key;
        _displayText = displayText;
        _type = type;
        _defaultValue = defaultValue;
        _description = description;
    }

    public Category category() {
        return _category;
    }

    public Class<?> type() {
        return _type;
    }

    public String key() {
        return _key;
    }

    public String displayText() {
        return _displayText;
    }

    public String defaultValue() {
        return _defaultValue;
    }

    public String description() {
        return _description;
    }

    public ConfigKey.Scope[] scope() {
        return _scope;
    }

    @Override
    public String toString() {
        return _key;
    }

    private static final HashMap<ConfigKey.Scope, Map<String, LoadBalancerConfigKey>> Configs = new HashMap<>();
    static {
        Configs.put(ConfigKey.Scope.Network, new LinkedHashMap<>());
        for (var c : LoadBalancerConfigKey.values()) {
            ConfigKey.Scope[] scopes = c.scope();
            for (ConfigKey.Scope scope : scopes) {
                Map<String, LoadBalancerConfigKey> currentConfigs = Configs.get(scope);
                currentConfigs.put(c.key(), c);
                Configs.put(scope, currentConfigs);
            }
        }
    }

    public static Map<String, LoadBalancerConfigKey> getConfigsByScope(ConfigKey.Scope scope) {
        return Configs.get(scope);
    }

    public static LoadBalancerConfigKey getConfigsByScopeAndName(ConfigKey.Scope scope, String name) {
        Map<String, LoadBalancerConfigKey> configs = Configs.get(scope);
        return configs.get(name);
    }

    public static ConfigKey.Scope getScopeFromString(String scope) {
        if (scope == null) {
            return null;
        }
        for (ConfigKey.Scope offScope : ConfigKey.Scope.values()) {
            if (offScope.name().equalsIgnoreCase(scope)) {
                return offScope;
            }
        }
        return null;
    }

    public static Pair<LoadBalancerConfigKey, String> validate(ConfigKey.Scope scope, String key, String value) {
        Map<String, LoadBalancerConfigKey> configs = Configs.get(scope);
        if (configs == null) {
            return new Pair<>(null, "Invalid scope " + scope);
        }
        LoadBalancerConfigKey config = null;
        for (var c : configs.values()) {
            if (c.key().equals(key)) {
                config = c;
                break;
            }
        }
        if (config == null) {
            return new Pair<>(null, "Invalid key " + key);
        }
        if (value == null) {
            return new Pair<>(null, "Invalid null value for parameter " + key);
        }
        Class<?> type = config.type();
        String errMsg = null;
        try {
            if (type.equals(Integer.class)) {
                errMsg = "Please enter a valid integer value for parameter " + key;
                Integer.parseInt(value);
            } else if (type.equals(Float.class)) {
                errMsg = "Please enter a valid float value for parameter " + key;
                Float.parseFloat(value);
            } else if (type.equals(Long.class)) {
                errMsg = "Please enter a valid long value for parameter " + key;
                Long.parseLong(value);
            }
        } catch (final NullPointerException|NumberFormatException e) {
            return new Pair<>(null, errMsg);
        }
        if (type.equals(Boolean.class) && (!("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)))) {
            return new Pair<>(null, "Please enter either 'true' or 'false' for parameter " + key);
        }

        if (type.equals(String.class) && (StringUtils.containsWhitespace(value))) {
            return new Pair<>(null, "Please enter valid value without whitespace characters for parameter " + key);
        }

        if (LbSslConfiguration.key().equals(key) && !SSLConfiguration.validate(value.toLowerCase())) {
            return new Pair<>(null, "Please enter valid value in " + String.join(",", SSLConfiguration.getValues()) + " for parameter " + key);
        }

        return new Pair<>(config, null);
    }
}
