package net.intelie.lognit.cli.commands;

import com.google.inject.Inject;
import net.intelie.lognit.cli.UserInput;
import net.intelie.lognit.cli.http.RestClient;
import net.intelie.lognit.cli.model.Welcome;

import java.net.MalformedURLException;

public class Login implements Command {
    private final UserInput console;
    private final RestClient http;

    @Inject
    public Login(UserInput console, RestClient http) {
        this.console = console;
        this.http = http;
    }

    @Override
    public String name() {
        return "login";
    }

    @Override
    public void execute(String... args) throws Exception {
        String server = args[0];

        authenticate(server);

        Welcome welcome = http.request("/rest/users/welcome", Welcome.class);
        console.printf("%s\n", welcome.getMessage());
    }

    private void authenticate(String server) throws MalformedURLException {
        String login = console.readLine("login: ");
        String password = console.readPassword("password: ");
        http.authenticate(server,  login, password);
    }
}
