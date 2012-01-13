package net.intelie.lognit.cli;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.intelie.lognit.cli.http.Jsonizer;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.http.RestClientImpl;
import net.intelie.lognit.cli.input.*;
import net.intelie.lognit.cli.state.RestStateStorage;

import java.io.File;

public class Main extends AbstractModule {
    public static void main(String... args) {
        Guice.createInjector(new Main())
                .getInstance(EntryPoint.class)
                .run(args);
    }

    @Override
    protected void configure() {
        bind(RestClient.class).to(RestClientImpl.class).in(Singleton.class);
    }

    @Provides
    private Command[] commands(LoginCommand login, InfoCommand info, LogoutCommand logout) {
        return new Command[]{login, info, logout};
    }

    @Provides
    private RestStateStorage restStateStorage(Jsonizer jsonizer) {
        return new RestStateStorage(local("state"), jsonizer);
    }

    private File local(String file) {
        return new File(new File(System.getProperty("user.home"), ".lognit"), file);
    }
}
