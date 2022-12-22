package com.example.demospring.services;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.example.demospring.AppConfigUtility;
import com.example.demospring.cache.ConfigurationCache;
import com.example.demospring.model.ConfigurationKey;
import com.example.demospring.movies.Movie;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import software.amazon.awssdk.services.appconfig.AppConfigClient;
import software.amazon.awssdk.services.appconfig.model.GetConfigurationResponse;
import org.json.JSONObject;
 
class PostgresqlConfigModel {

    public String dsn;
    public String username;
    public String password;

    public PostgresqlConfigModel(String urlString) throws Exception {
        try {
            var uri = new URI(urlString);
            // var uri = url.toURI();
            MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUri(uri).build()
                    .getQueryParams();
            var host = uri.getHost();
            var port = uri.getPort();
            if (port <= 0) {
                port = 5432;
            }
            var path = uri.getPath();
            dsn = "jdbc:postgresql://" + host + ":" + port + path;
            var userList = parameters.get("user");
            var passwordList = parameters.get("password");
            if (userList != null && userList.size() > 0) {
                username = userList.get(0);
            }
            if (passwordList != null && passwordList.size() > 0) {
                password = passwordList.get(0);
                password = URLDecoder.decode(password, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            throw new Exception("PostgresqlConfigModel: \n" + e.toString());
        }
    }
}

public class AwsConfig {
    @Autowired
    private Environment env;

    public Duration cacheItemTtl = Duration.ofSeconds(30);
    private Boolean boolEnableFeature;
    private int intItemLimit;
    private AppConfigClient client;
    private String clientId;
    private ConfigurationCache cache;
    public PostgresqlConfigModel postgresqlModel;
 

    AwsConfig() throws Exception {
        var configText = loadConfigFromAws("main.config", "default");

        if (configText == null || configText.isEmpty()) {
            System.out.println("Failed to load config from AWS");
            return;
        }
        var configArray = configText.split("\n");
        for (String string : configArray) {
            var index = string.indexOf("=");
            if (index < 0) {
                continue;
            }
            var key = string.substring(0, index);
            var value = string.substring(index + 1);
            switch (key) {
                case "PGDSN":
                     postgresqlModel = new PostgresqlConfigModel(value);
                    break;
                default:
                    break;
            }
        }
    }

    public String loadConfigFromAws(String fileName, String envName) {
        final String application = "debug.venus.lighting";
        final String environment = "default";
        final String config = "main.config";
        cacheItemTtl = Duration.ofSeconds(60);

        final AppConfigUtility appConfigUtility = new AppConfigUtility(
                getOrDefault(this::getClient, this::getDefaultClient),
                getOrDefault(this::getConfigurationCache, ConfigurationCache::new),
                getOrDefault(this::getCacheItemTtl, () -> cacheItemTtl),
                getOrDefault(this::getClientId, this::getDefaultClientId));

        final GetConfigurationResponse response = appConfigUtility
                .getConfiguration(new ConfigurationKey(application, environment, config));
        final String appConfigResponse = response.content().asUtf8String();

        return appConfigResponse;

    }

    public static AwsConfig _instance;

    public static AwsConfig getInstance() throws Exception {
        if (_instance == null) {
            _instance = new AwsConfig();
        }
        return _instance;
    }

    private <T> T getOrDefault(final Supplier<T> optionalGetter, final Supplier<T> defaultGetter) {
        return Optional.ofNullable(optionalGetter.get()).orElseGet(defaultGetter);
    }

    private String getDefaultClientId() {
        return UUID.randomUUID().toString();
    }

    protected AppConfigClient getDefaultClient() {
        return AppConfigClient.create();
    }

    public ConfigurationCache getConfigurationCache() {
        return cache;
    }

    public AppConfigClient getClient() {
        return client;
    }

    public Duration getCacheItemTtl() {
        return cacheItemTtl;
    }

    public String getClientId() {
        return clientId;
    }
}
