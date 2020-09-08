package duke.command;

import duke.resource.Wrapper;

import java.util.List;

public class ExitCommand implements Command {

    @Override
    public String apply(Wrapper wrapper, List<String> input) {
        wrapper.exit();
        return "";
    }
}
