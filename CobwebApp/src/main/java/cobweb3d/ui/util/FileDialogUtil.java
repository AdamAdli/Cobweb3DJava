package cobweb3d.ui.util;

import org.jetbrains.annotations.Nullable;
import util.swing.FileExtFilter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileDialogUtil {

    @Nullable
    public static File openFile(Window parent, String title, FileExtFilter... fileExtFilters) {
        JFileChooser openDialog = new JFileChooser();
        openDialog.setDialogTitle(title);
        openDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        openDialog.setAcceptAllFileFilterUsed(false);
        for (FileExtFilter fileExtFilter : fileExtFilters)
            openDialog.addChoosableFileFilter(fileExtFilter);

        openDialog.setVisible(true);
        if (openDialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = openDialog.getSelectedFile();
            if (file.exists()) {
                return file;
            } else {
                JOptionPane.showMessageDialog(
                        parent,
                        "File \" " + file.getAbsolutePath() + file + "\" could not be found!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        return null;
    }

    @Nullable
    public static String saveFile(Window parent, String title, FileExtFilter... fileExtFilters) {
        JFileChooser saveDialog = new JFileChooser();
        saveDialog.setDialogTitle(title);
        saveDialog.setDialogType(JFileChooser.SAVE_DIALOG);
        saveDialog.setAcceptAllFileFilterUsed(false);
        for (FileExtFilter fileExtFilter : fileExtFilters)
            saveDialog.addChoosableFileFilter(fileExtFilter);

        saveDialog.setVisible(true);
        if (saveDialog.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
            return saveDialog.getSelectedFile().getAbsolutePath();
        return null;
    }
}
