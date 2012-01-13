package net.intelie.lognit.cli.input;

import com.google.inject.Inject;
import net.intelie.lognit.cli.Lognit;
import net.intelie.lognit.cli.model.Welcome;

public class InfoCommand implements Command {
    private final UserInput console;
    private final Lognit lognit;

    @Inject
    public InfoCommand(UserInput console, Lognit lognit) {
        this.console = console;
        this.lognit = lognit;
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public void execute(ArgsParser parser) throws Exception {
        Welcome welcome = lognit.info();
        console.println(welcome.getMessage());
    }
}