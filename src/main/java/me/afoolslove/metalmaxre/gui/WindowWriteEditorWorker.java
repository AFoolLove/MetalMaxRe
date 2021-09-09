package me.afoolslove.metalmaxre.gui;

import me.afoolslove.metalmaxre.EditorProcess;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.WriteEditorWorker;
import me.afoolslove.metalmaxre.editor.EditorManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class WindowWriteEditorWorker extends WriteEditorWorker {
    private final MainWindow window;
    private final JProgressBar progressBar;
    private JDialog dialog;

    private final File out;

    public WindowWriteEditorWorker(@NotNull MainWindow window, @NotNull File out) {
        this.window = window;
        this.out = out;
        this.progressBar = new JProgressBar(0, EditorManager.getEditors().size());

        // 显示正在应用编辑器内容
        SwingUtilities.invokeLater(() -> {
            JOptionPane jOptionPane = new JOptionPane(progressBar, JOptionPane.INFORMATION_MESSAGE);
            dialog = jOptionPane.createDialog(window, "正在应用编辑器内容");
            dialog.setVisible(true);
        });
    }


    @Override
    protected void process(List<Map.Entry<EditorProcess, Object>> chunks) {
        for (Map.Entry<EditorProcess, Object> chunk : chunks) {
            if (chunk.getKey() == EditorProcess.RESULT) {
                progressBar.setValue(progressBar.getValue() + 1);
            }
            if (chunk.getKey() == EditorProcess.MESSAGE) {
                System.out.println(chunk.getValue());
            }
        }
    }

    @Override
    protected void done() {
        try {
            Files.write(Paths.get(out.getAbsolutePath()), MetalMaxRe.getInstance().getBuffer().array(), StandardOpenOption.CREATE);
            // 关闭提示的Dialog
            dialog.setVisible(false);
            dialog.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
