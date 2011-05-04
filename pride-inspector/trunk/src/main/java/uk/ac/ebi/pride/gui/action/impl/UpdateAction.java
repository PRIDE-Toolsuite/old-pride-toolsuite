package uk.ac.ebi.pride.gui.action.impl;

import uk.ac.ebi.pride.gui.action.PrideAction;
import uk.ac.ebi.pride.gui.desktop.Desktop;
import uk.ac.ebi.pride.gui.task.Task;
import uk.ac.ebi.pride.gui.task.TaskEvent;
import uk.ac.ebi.pride.gui.task.TaskListener;
import uk.ac.ebi.pride.gui.task.impl.CheckUpdateTask;
import uk.ac.ebi.pride.gui.utils.DefaultGUIBlocker;
import uk.ac.ebi.pride.gui.utils.GUIBlocker;
import uk.ac.ebi.pride.gui.utils.UpdateChecker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Action for check updates.
 * <p/>
 * User: rwang
 * Date: 11-Nov-2010
 * Time: 17:53:19
 */
public class UpdateAction extends PrideAction implements TaskListener<Boolean, Void> {

    public UpdateAction(String name, Icon icon) {
        super(name, icon);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        Task newTask = new CheckUpdateTask();
        newTask.addTaskListener(this);
        // set task's gui blocker
        newTask.setGUIBlocker(new DefaultGUIBlocker(newTask, GUIBlocker.Scope.NONE, null));
        // add task listeners
        Desktop.getInstance().getDesktopContext().addTask(newTask);
    }

    @Override
    public void succeed(TaskEvent<Boolean> booleanTaskEvent) {
        Boolean hasUpdate = booleanTaskEvent.getValue();
        if (hasUpdate) {
            UpdateChecker.showUpdateDialog();
        }
    }

    @Override
    public void started(TaskEvent<Void> event) {
    }

    @Override
    public void process(TaskEvent<List<Void>> listTaskEvent) {
    }

    @Override
    public void finished(TaskEvent<Void> event) {
    }

    @Override
    public void failed(TaskEvent<Throwable> event) {
    }

    @Override
    public void cancelled(TaskEvent<Void> event) {
    }

    @Override
    public void interrupted(TaskEvent<InterruptedException> iex) {
    }

    @Override
    public void progress(TaskEvent<Integer> progress) {
    }
}
