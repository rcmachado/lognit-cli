package net.intelie.lognit.cli.formatters;

import net.intelie.lognit.cli.http.Jsonizer;
import net.intelie.lognit.cli.input.UserConsole;
import net.intelie.lognit.cli.model.Message;

public class JsonFormatter implements Formatter {
    private final Jsonizer json;
    private final UserConsole console;

    public JsonFormatter(UserConsole console, Jsonizer json) {
        this.json = json;
        this.console = console;
    }

    @Override
    public void printStatus(String format, Object... args) {
        console.println(format, args);
    }

    @Override
    public void printMessage(Message message) {
        console.printOut("%s", json.to(message));
    }
}