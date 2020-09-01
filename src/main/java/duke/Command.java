package duke;

import java.time.format.DateTimeParseException;
import java.util.List;
import duke.resource.TaskList;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.ToDo;
import duke.util.DukeException;


/**
 * Command class that handles the logic flow of commands input.
 */

public class Command {

    private final String[] input;
    private boolean shouldExit = false;

    /**
     * Constructor that creates a Command object.
     * @param input command string that has been split
     */

    public Command(String[] input) {
        this.input = input;
    }

    /**
     * Evaluates the syntax of the input command and executes it, throwing
     * a DukeException if the syntax is incorrect.
     * @param tasks the TaskList object associated with the current Duke object
     * @param ui the Ui object associated with the current Duke object
     * @param storage the Storage object associated with the current Duke object
     * @throws DukeException thrown if any errors are detected with the input
     */

    public void execute(TaskList tasks, Ui ui, Storage storage)
            throws DukeException {
        switch(input[0]) {
        case "bye":
            ui.printBye();
            this.shouldExit = true;
            break;
        case "list": {
            ui.printList(tasks);
            break;
        }
        case "done": {
            int index;
            if (input.length == 1) {
            throw new DukeException("    Please select a task to mark as completed!");
            }
            try {
                index = Integer.parseInt(input[1]);
            } catch (NumberFormatException e) {
                throw new DukeException("    Please choose an integer value!");
            }
            if (index <= 0) {
                throw new DukeException("    Please choose an integer greater than 0!");
            } else if (index > tasks.size()) {
                throw new DukeException("    Your task list is not that long yet!");
            }
            tasks.completeTask(index);
            ui.printDone(tasks.getTask(index));
            storage.save(tasks);
            break;
        }
        case "delete": {
            int index;
            if (input.length == 1) {
                throw new DukeException("    Please select a task to mark as completed!");
            }
            try {
                index = Integer.parseInt(input[1]);
            } catch (NumberFormatException e) {
                throw new DukeException("    Please choose an integer value!");
            }
            if (index <= 0) {
                throw new DukeException("    Please choose an integer greater than 0!");
            } else if (index > tasks.size()) {
                throw new DukeException("    Your task list is not that long yet!");
            }
            Task task = tasks.getTask(index);
            tasks.deleteTask(index);
            ui.printDelete(tasks, task);
            storage.save(tasks);
            break;
        }
        case "find": {
            if (input.length == 1) {
                throw new DukeException("    Please use a keyword you'd like to search with!");
            }
            if (input.length > 2) {
                throw new DukeException("    Please use only one keyword!");
            }
            List<Task> matches = tasks.search(input[1]);
            ui.printFind(matches);
            break;
        }
        case "deadline": {
            Task task;
            StringBuilder description = new StringBuilder();
            StringBuilder time = new StringBuilder();
            if (input.length == 1) {
                throw new DukeException("    A deadline requires a description and a time!");
            }
            int i = 1;
            while (!input[i].equals("/by")) {
                description.append(input[i++]).append(" ");
                if (i == input.length) {
                    throw new DukeException("    deadline requires the use of \"/by\"!");
                }
            }
            if (description.length() == 0) {
                throw new DukeException("    The description of a deadline cannot be empty!");
            }
            while (++i < input.length) {
                time.append(input[i]).append(" ");
            }
            if (time.length() == 0) {
                throw new DukeException("    The time of a deadline cannot be empty!");
            }
            description.deleteCharAt(description.length() - 1);
            time.deleteCharAt(time.length() - 1);
            try {
                task = new Deadline(description.toString(), time.toString(), false);
                tasks.addTask(task);
                ui.printAdd(tasks, task);
                storage.save(tasks);
            } catch (DateTimeParseException e) {
                throw new DukeException(
                        "    Error: Please use the following format instead:\n" +
                        "        dd-MM-yyyy HHmm");
            }
            break;
        }
        case "event": {
            Task task;
            StringBuilder description = new StringBuilder();
            StringBuilder time = new StringBuilder();
            if (input.length == 1) {
                throw new DukeException("    An event requires a description and a time!");
            }
            int i = 1;
            while (!input[i].equals("/at")) {
                description.append(input[i++]).append(" ");
                if (i == input.length) {
                    throw new DukeException("    event requires the use of \"/at\"!");
                }
            }
            if (description.length() == 0) {
                throw new DukeException("    The description of an event cannot be empty!");
            }
            while (++i < input.length) {
                time.append(input[i]).append(" ");
            }
            if (time.length() == 0) {
                throw new DukeException("    The time of an event cannot be empty!");
            }
            description.deleteCharAt(description.length() - 1);
            time.deleteCharAt(time.length() - 1);
            try {
                task = new Event(description.toString(), time.toString(), false);
                tasks.addTask(task);
                ui.printAdd(tasks, task);
                storage.save(tasks);
            } catch (DateTimeParseException e) {
                throw new DukeException(
                        "    Error: Please use the following format instead:\n" +
                        "        dd-MM-yyyy HHmm");
            }
            break;
        }
        case "todo": {
            if (input.length == 1) {
                throw new DukeException("    The description of a todo cannot be empty!");
            }
            Task task;
            StringBuilder description = new StringBuilder();
            for (int i = 1; i < input.length; i++) {
                description.append(input[i]).append(" ");
            }
            description.deleteCharAt(description.length() - 1);
            task = new ToDo(description.toString(), false);
            tasks.addTask(task);
            ui.printAdd(tasks, task);
            storage.save(tasks);
            break;
        }
        default:
            throw new DukeException("    I'm sorry, but I don't know what that means :-(");
    }
    }

    public boolean shouldExit() {
        return this.shouldExit;
    }

}
