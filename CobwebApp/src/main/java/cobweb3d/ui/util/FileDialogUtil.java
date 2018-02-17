package cobweb3d.ui.util;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileDialogUtil {

    @Nullable
    public static File openFile(Frame parent, String title, String fileFilter) {
        FileDialog openDialog = new FileDialog(parent, title, FileDialog.LOAD);
        openDialog.setFile(fileFilter);
        openDialog.setVisible(true);
        String directory = openDialog.getDirectory();
        String file = openDialog.getFile();
        if (file != null && directory != null) {
            File of = new File(directory + file);
            if (of.exists()) {
                return of;
            } else {
                JOptionPane.showMessageDialog(
                        parent,
                        "File \" " + directory + file + "\" could not be found!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        return null;
    }

    @Nullable
    public static String saveFile(Frame parent, String title, String fileFilter) {
        FileDialog openDialog = new FileDialog(parent, title, FileDialog.SAVE);
        openDialog.setFile(fileFilter);
        openDialog.setVisible(true);
        String directory = openDialog.getDirectory();
        String file = openDialog.getFile();
        if (file != null && directory != null) {
            return directory + file;
        }
        return null;
    }

    @Nullable
    public static String saveFile(Dialog parent, String title, String fileFilter) {
        FileDialog openDialog = new FileDialog(parent, title, FileDialog.SAVE);
        openDialog.setFile(fileFilter);
        openDialog.setVisible(true);
        String directory = openDialog.getDirectory();
        String file = openDialog.getFile();
        if (file != null && directory != null) {
            return directory + file;
        }
        return null;
    }
}
