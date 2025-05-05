package configs;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${env}.properties",
        "classpath:default.properties",
})
public interface TestPropertiesConfig extends org.aeonbits.owner.Config {
    @Key("login")
    String getUsername();

    @Key("password")
    String getPassword();

    @Key("baseUrl")
    String getBaseUrl();
}
