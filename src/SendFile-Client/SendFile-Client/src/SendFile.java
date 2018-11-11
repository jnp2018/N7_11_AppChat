
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



public class SendFile extends javax.swing.JFrame {

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String myusername;
    private String host;
    private int port;
    private StringTokenizer st;
    private String sendTo;
    private String file;
    private MainForm main;
    private Object chooser;
    
    
     // Tạo một form SendFile
     
    public SendFile() {
        initComponents();
        progressbar.setVisible(false);
    }
    
    
    /*
        Phương thức này được gọi đến khi người dùng click vào menu “Gửi File”, 
    sau đó kết nối đến Server và bắt đầu sẵn sàng để gửi file
    */
    public boolean prepare(String u, String h, int p, MainForm m){
        this.host = h;
        this.myusername = u;
        this.port = p;
        this.main = m;
        /*  Kết nối đến Server  */
        try {
            socket = new Socket(host, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            //  Format: CMD_SHARINGSOCKET [sender]
            String format = "CMD_SHARINGSOCKET "+ myusername;
            dos.writeUTF(format);
            System.out.println(format);
            
            /*  Khởi động SendFile Thread    */
            new Thread(new SendFileThread(this)).start();
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    
    
    
    /*   Phương thức SendFileThread này sẽ gửi yêu cầu chuyển dữ liệu đến Server   */
    class SendFileThread implements Runnable{
        private SendFile form;
        public SendFileThread(SendFile form){
            this.form = form;
        }
        
        private void closeMe(){
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("[closeMe]: "+e.getMessage());
            }
            dispose();
        }
        
        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted()){
                    String data = dis.readUTF();  // Đọc nội dung của dữ liệu được nhận từ Server
                    st = new StringTokenizer(data);
                    String cmd = st.nextToken();  //  Lấy chữ đầu tiên từ dữ liệu
                    switch(cmd){
                        case "CMD_RECEIVE_FILE_ERROR":  // Định dạng: CMD_RECEIVE_FILE_ERROR [Message]
                            String msg = "";
                            while(st.hasMoreTokens()){
                                msg = msg+" "+st.nextToken();
                            }
                            form.updateAttachment(false);
                            JOptionPane.showMessageDialog(SendFile.this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
                            this.closeMe();
                            break;
                            
                        case "CMD_RECEIVE_FILE_ACCEPT":  // Định dạng: CMD_RECEIVE_FILE_ACCEPT [Message]
                            /*  Bắt đầu khởi động thread File đính kèm   */

                            new Thread(new SendingFileThread(socket, file, sendTo, myusername, SendFile.this)).start();
                            break;
                            
                        case "CMD_SENDFILEERROR":
                            String emsg = "";
                            while(st.hasMoreTokens()){
                                emsg = emsg +" "+ st.nextToken();
                            }                                                     
                            System.out.println(emsg);                            
                            JOptionPane.showMessageDialog(SendFile.this, emsg,"Lỗi", JOptionPane.ERROR_MESSAGE);
                            form.updateAttachment(false);
                            form.disableGUI(false);
                            form.updateBtn("Gửi File");
                            break;
                        
                        
                        case "CMD_SENDFILERESPONSE":
                            /*
                            Format: CMD_SENDFILERESPONSE [username] [Message]
                            */
                            String rReceiver = st.nextToken();
                            String rMsg = "";
                            while(st.hasMoreTokens()){
                                rMsg = rMsg+" "+st.nextToken();
                            }
                            form.updateAttachment(false);
                            JOptionPane.showMessageDialog(SendFile.this, rMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
                            dispose();
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtFile = new javax.swing.JTextField();
        btnBrowse = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtSendTo = new javax.swing.JTextField();
        progressbar = new javax.swing.JProgressBar();
        btnSendFile = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dịch vụ Gửi File - QchatApp");

        jLabel1.setText("Chọn File :");

        txtFile.setEditable(false);
        txtFile.setBackground(new java.awt.Color(255, 255, 255));
        txtFile.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFileActionPerformed(evt);
            }
        });

        btnBrowse.setText("Tìm File");
        btnBrowse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        jLabel2.setText("Gửi đến :");

        progressbar.setStringPainted(true);

        btnSendFile.setBackground(new java.awt.Color(132, 6, 6));
        btnSendFile.setForeground(new java.awt.Color(255, 255, 255));
        btnSendFile.setText("Gửi File");
        btnSendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(307, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSendFile))
                            .addComponent(txtSendTo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtFile)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowse)))
                        .addGap(37, 37, 37))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBrowse))
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSendTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(progressbar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSendFile, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFileActionPerformed

    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
        // TODO add your handling code here:
        showOpenDialog();
    }//GEN-LAST:event_btnBrowseActionPerformed

    private void btnSendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFileActionPerformed
        // TODO add your handling code here:
        sendTo = txtSendTo.getText();
        file = txtFile.getText();

        if((sendTo.length() > 0) && (file.length() > 0)){
            try {
                // Format: CMD_SEND_FILE_XD [sender] [receiver] [filename]
                txtFile.setText("");
                String fname = getThisFilename(file);
                String format = "CMD_SEND_FILE_XD "+myusername+" "+sendTo+" "+fname;
                dos.writeUTF(format);
                System.out.println(format);
                updateBtn("Đang gửi đi...");
                btnSendFile.setEnabled(false);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(this, "Không để trống.!","Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSendFileActionPerformed
    public void showOpenDialog(){
        JFileChooser chooser = new JFileChooser();
        int intval = chooser.showOpenDialog(this);
        if(intval == chooser.APPROVE_OPTION){
            txtFile.setText(chooser.getSelectedFile().toString());
        }else{
            txtFile.setText("");
        }
    }
    
    
    /*   Phương thức này sẽ disable/enabled tất cả GUI   */
    public void disableGUI(boolean d){
        if(d){ // Disable
            txtSendTo.setEditable(false);
            btnBrowse.setEnabled(false);
            btnSendFile.setEnabled(false);
            txtFile.setEditable(false);
            progressbar.setVisible(true);
        } else { // Enable
            txtSendTo.setEditable(true);
            btnSendFile.setEnabled(true);
            btnBrowse.setEnabled(true);
            txtFile.setEditable(true);
            progressbar.setVisible(false);
        }
    }
    
    
    /*  Nhận Form Title   */
    public void setMyTitle(String s){
        setTitle(s);
    }
    
    /*   Hàm này sẽ đóng Form  */
    protected void closeThis(){
        dispose();
    }
    
    /*  Hàm này sẽ nhận Filename */
    public String getThisFilename(String path){
        File p = new File(path);
        String fname = p.getName();
        return fname.replace(" ", "_");
    }
    
    /*  Cập nhật file đính kèm   */
    public void updateAttachment(boolean b){
        main.updateAttachment(b);
    }
    
    /*  Cập nhật nút btnSendFile text  */
    public void updateBtn(String str){
        btnSendFile.setText(str);
    }
    
    /**
     * Update progress bar
     * @param val 
     */
    public void updateProgress(int val){
        progressbar.setValue(val);
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SendFile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SendFile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnSendFile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar progressbar;
    private javax.swing.JTextField txtFile;
    private javax.swing.JTextField txtSendTo;
    // End of variables declaration//GEN-END:variables
}
