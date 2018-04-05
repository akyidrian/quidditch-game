package quidditchgame.menus;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Class MenuInstructionPanel.
 * Used to display the instructions for the game to the user.
 *
 * @author Jimmy Yuan, Ben Han
 */
public class MenuInstructionPanel extends javax.swing.JPanel {

    private Menu parent;

    /** Creates new form MenuInstructionPanel
     *
     * @param m parent Menu object that handles events
     */
    public MenuInstructionPanel(Menu m) {
        parent = m;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainMenuButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        howtoplayButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(1000, 630));
        setMinimumSize(new java.awt.Dimension(1000, 630));
        setPreferredSize(new java.awt.Dimension(1000, 630));
        setRequestFocusEnabled(false);

        mainMenuButton.setText("Return to Main Menu");
        mainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainMenuButtonActionPerformed(evt);
            }
        });

        howtoplayButton.setText("Next Page");
        howtoplayButton.setActionCommand("howtoplayButton");
        howtoplayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howtoplayButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(mainMenuButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 481, Short.MAX_VALUE)
                .addComponent(howtoplayButton)
                .addGap(153, 153, 153))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(587, 587, 587)
                .addComponent(jLabel7)
                .addGap(0, 43, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mainMenuButton)
                    .addComponent(howtoplayButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Event handler for "Return to Main Menu" button clicks.
     * @param evt Button event
     */
    private void mainMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainMenuButtonActionPerformed
        parent.mainMenu();
    }//GEN-LAST:event_mainMenuButtonActionPerformed

    /**
     * howtoplay Button to go to the next page of the instruction
     * @param evt Button event
     */
    private void howtoplayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_howtoplayButtonActionPerformed
        parent.howtoplay();
    }//GEN-LAST:event_howtoplayButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton howtoplayButton;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JButton mainMenuButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Draws the background image and the instruction image
     * @param g Graphics object to draw with
     */
    @Override
    public void paintComponent(Graphics g )
    {
        super.paintComponent( g );
        // draws the background and the instructions
        Image bgImage1 = new ImageIcon("./assets/gameIns.png").getImage();
        Image insImage1 = new ImageIcon("./assets/Instruction1.png").getImage();
        g.drawImage(bgImage1, 0, 0, null); 
        g.drawImage(insImage1, 100, 80, null); 
    }
}
