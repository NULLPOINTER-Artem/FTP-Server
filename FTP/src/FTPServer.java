import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer extends JFrame {
    // VARIABLES
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> list;
    private javax.swing.JTextArea txt;

    // Creation new form Server
    public FTPServer() { createNewFormServer(); }

    // Method create new form FTP-Server
    private void createNewFormServer() {
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        list = new javax.swing.JList<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setText("Server");

        jButton1.setText("Start Server");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txt.setEditable(false);
        txt.setColumns(20);
        txt.setRows(5);
        jScrollPane1.setViewportView(txt);

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(list);

        jButton2.setText("Open");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Save");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButton1))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1)))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Creation Server
    private ServerSocket server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DefaultListModel defModel = new DefaultListModel();
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        list.setModel(defModel);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(9999);
                    txt.append("Server is running!\n");

                    Socket client = server.accept();

                    in = new ObjectInputStream(client.getInputStream());
                    DataManager data = (DataManager) in.readObject();
                    String name = data.getName();

                    txt.append("New " + name + " has been connected!\n");

                    while (true) {
                        data = (DataManager) in.readObject();
                        defModel.addElement(data);
                        txt.append("Get 1 file!\n");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(FTPServer.this, e, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    // Processing Actions
    private void listMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            if (!list.isSelectionEmpty()) {
                if (SwingUtilities.isLeftMouseButton(evt)) {
                    open();
                } else if (SwingUtilities.isRightMouseButton(evt)) {
                    save();
                }
            }
        }
    }

    private void open() {
        DataManager data = (DataManager) defModel.getElementAt(list.getSelectedIndex());
        if (data.getStatus().equals("Image")) {
            ShowImageManager obj = new ShowImageManager(this, true);
            ImageIcon icon = new ImageIcon(data.getFile());
            obj.set(icon);
            obj.setVisible(true);
        }
    }
    private void save() {
        DataManager data = (DataManager) defModel.getElementAt(list.getSelectedIndex());
        JFileChooser chooser = new JFileChooser();
        int c = chooser.showSaveDialog(this);
        if (c == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(chooser.getSelectedFile());
                fileOutputStream.write(data.getFile());
                fileOutputStream.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!list.isSelectionEmpty()) {
            open();
        }
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (!list.isSelectionEmpty()) {
            save();
        }
    }

    // Main Stream
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FTPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FTPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FTPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FTPServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FTPServer().setVisible(true);
            }
        });
    }
}
