package cobweb3d.ui.view;

import cobweb3d.ui.AppContext;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditMenu extends JMenu {
    private AppContext appContext;

    public EditMenu(AppContext appContext) {
        super("File");
        initialize(appContext);
    }

    private Action openSimulationAction = new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent e) {
            appContext.openFileDialog();
        }

        private static final long serialVersionUID = 1L;
    };
    private Action saveSimulationAction = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent e) {
            appContext.openFileDialog();
        }

        private static final long serialVersionUID = 1L;
    };
    private Action setLogAction = new AbstractAction("Log") {
        @Override
        public void actionPerformed(ActionEvent e) {
            appContext.openFileDialog();
        }

        private static final long serialVersionUID = 1L;
    };
    private Action quitAction = new AbstractAction("Quit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            appContext.quitApplication();
        }

        private static final long serialVersionUID = 1L;
    };

    private void initialize(AppContext appContext) {
        this.appContext = appContext;
        add(new JMenuItem(openSimulationAction));
        add(new JMenuItem(saveSimulationAction));
        add(new JSeparator());
        add(new JMenuItem(setLogAction));
        add(new JMenuItem(quitAction));
    }
}
