package olivia.command;

import java.time.format.DateTimeParseException;
import java.util.List;

import olivia.logic.Storage;
import olivia.resource.TaskList;
import olivia.resource.Wrapper;
import olivia.task.Event;
import olivia.task.Task;
import olivia.ui.Ui;

/**
 * EventCommand class that represents an Event task being added to the
 * TaskList.
 */

public class EventCommand implements Command {

    /**
     * Creates and stores an Event object in the TaskList if the input is valid.
     * @param wrapper contains Olivia's Storage, TaskList and Ui objects.
     * @param input list that contains the input arguments for the command.
     * @return output String to the user.
     */

    @Override
    public String apply(Wrapper wrapper, List<String> input) {
        Storage storage = wrapper.getStorage();
        TaskList tasks = wrapper.getTaskList();
        Ui ui = wrapper.getUi();
        Task task;
        StringBuilder description = new StringBuilder();
        StringBuilder time = new StringBuilder();
        if (input.size() == 0) {
            return "An event requires a description and a time!";
        }
        int i = 0;
        while (!input.get(i).equals("/at")) {
            description.append(input.get(i++)).append(" ");
            if (i == input.size()) {
                return "event requires the use of \"/at\"!";
            }
        }
        if (description.length() == 0) {
            return "The description of an event cannot be empty!";
        }
        while (++i < input.size()) {
            time.append(input.get(i)).append(" ");
        }
        if (time.length() == 0) {
            return "The time of an event cannot be empty!";
        }
        description.deleteCharAt(description.length() - 1);
        time.deleteCharAt(time.length() - 1);
        try {
            task = new Event(description.toString(), time.toString(), false);
            tasks.addTask(task);
            storage.save(tasks);
            return ui.showAdd(tasks, task);
        } catch (DateTimeParseException e) {
            return "Error: Please use the following format instead:\ndd-MM-yyyy HHmm";
        }
    }

}
