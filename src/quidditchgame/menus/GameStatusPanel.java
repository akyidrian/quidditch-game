package quidditchgame.menus;

import quidditchgame.Game.GameCondition;
import quidditchgame.Team;

/**
 * Class GameStatusPanel
 * This class defines the status panel at the bottom of the screen during
 * gameplay.
 * 
 * @author  Jimmy Yuan,  Ben Han
 */
public class GameStatusPanel extends javax.swing.JPanel {

    private Menu parent;
    
    /** Creates new form GameStatusPanel
     * @param m a reference to the parent Menu class object
     */
    public GameStatusPanel(Menu m) {
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

        team1Score = new javax.swing.JLabel();
        team2Score = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        pauseButton = new javax.swing.JButton();
        mainMenuButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(254, 254, 254));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setFocusable(false);
        setMaximumSize(new java.awt.Dimension(1600, 50));
        setMinimumSize(new java.awt.Dimension(1000, 30));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1000, 30));
        setRequestFocusEnabled(false);
        setVerifyInputWhenFocusTarget(false);

        team1Score.setForeground(new java.awt.Color(1, 11, 139));
        team1Score.setText("Team 1 Score");

        team2Score.setForeground(new java.awt.Color(1, 11, 139));
        team2Score.setText("Team 2 Score");

        time.setForeground(new java.awt.Color(1, 11, 139));
        time.setText("Time");

        pauseButton.setText("Pause Game");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });

        mainMenuButton.setText("Exit to Menu");
        mainMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainMenuButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(pauseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(team1Score)
                .addGap(152, 152, 152)
                .addComponent(time)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 406, Short.MAX_VALUE)
                .addComponent(team2Score)
                .addGap(18, 18, 18)
                .addComponent(mainMenuButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(team1Score)
                    .addComponent(team2Score)
                    .addComponent(pauseButton)
                    .addComponent(mainMenuButton)
                    .addComponent(time))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Event handler for the in-game pause button.
     * Pauses the game and brings up the pause screen.
     * @param evt Button event
     */
    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pauseButtonActionPerformed
        pauseGame();
    }//GEN-LAST:event_pauseButtonActionPerformed

    /**
     * Event handler for the in-game main menu button.
     * End the game.
     * @param evt Button event
     */
    private void mainMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainMenuButtonActionPerformed
        exitGame();
    }//GEN-LAST:event_mainMenuButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton mainMenuButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JLabel team1Score;
    private javax.swing.JLabel team2Score;
    private javax.swing.JLabel time;
    // End of variables declaration//GEN-END:variables
    private static final int ONE_MINUTE = 60; //seconds.
    private int team1ScoreInt, team2ScoreInt = 0;
    
    public void updateScore(Team team1, Team team2) {
        setTeam1Score(team1);
        setTeam2Score(team2);
        
        this.repaint();
        this.invalidate();
    }
   
    /**
     * Converts input game time into minutes and seconds then displays these.
     * 
     * @param gameTime in milliseconds.
     */
    public void updateGameTime(long gameTime) {
        long gameTimeMins = 0;
        long gameTimeSecs = 0;
        
        //Convert from seconds -> minutes and seconds
        if (gameTime < ONE_MINUTE) {
            gameTimeMins = 0;
            gameTimeSecs = gameTime % ONE_MINUTE;
        }
        else if (gameTime >= ONE_MINUTE){
            gameTimeMins = gameTime / ONE_MINUTE;
            gameTimeSecs = gameTime % ONE_MINUTE;
        }
        String displayTime = String.format( "%02d:%02d", gameTimeMins, gameTimeSecs);
        
        time.setText("Game Time: " + displayTime);
        
        this.repaint();
        this.invalidate();        
    }

    /**
     * Provides public access to the Menu class functions
     */
    public void exitGame() {
        parent.mainMenu();
    }

    /**
     * Provides public access to the Menu class functions
     */
    public void pauseGame() {
        parent.pauseGame();
    }

    /**
     * Housekeeping at the end of a game
     * @param team1 reference to Team object
     * @param team2 reference to Team object
     * @param gameCondition GameCondition object defining how the game finished
     */
    public void finishGame(Team team1, Team team2, GameCondition gameCondition){
                
        String teamone = new String(team1.getName() + " ");
        String score = new String(team1.getScore() + " : " + team2.getScore());
        String teamtwo = new String(" " + team2.getName());
        String winner;
        String temp;

        if (gameCondition == gameCondition.TIME_UP)
            temp = new String("Time Up: \n");
        else if(gameCondition == gameCondition.SNITCH_CAUGHT_0)
            temp = new String("Snitch Caught By " + team1.getName() + " : \n");
        else if(gameCondition == gameCondition.SNITCH_CAUGHT_1)
            temp = new String("Snitch Caught By " + team2.getName() + " : \n");
        else
            temp = new String("Time up: ");

        if (team1.getScore() > team2.getScore())
            winner = new String(temp + team1.getName() + " Wins!");
        else if (team2.getScore() > team1.getScore())
            winner = new String(temp + team2.getName() + " Wins!");
        else
            winner = new String(temp + "It's a Draw!");

        parent.finishGame(teamone, teamtwo, score, winner);
    }
            
    /**
     * Change the score text in the status panel
     * @param team1 reference to Team object
     */
    private void setTeam1Score(Team team1) {
        team1ScoreInt = team1.getScore();
        team1Score.setText(team1.getName() + " Score: " + team1ScoreInt);
    }


    /**
     * Change the score text in the status panel
     * @param team2 reference to Team object
     */
    private void setTeam2Score(Team team2) {
        team2ScoreInt = team2.getScore();
        team2Score.setText(team2.getName() + " Score: " + team2ScoreInt);
    }
}
