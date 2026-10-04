import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class TicTacToeGame extends JPanel implements MouseListener 
{
    Image title;
    Image background;
    Image RedWins;
    Image BlueWins;
    Image GreenWins;
    Image TieGame;
    Image Home;
    Image Settings;
    Image SettingsBackground;
    Image Filter;
    int screen = 1; //which screen to draw
    int players = 2; //how many players
    int turn = 6; //which players turn
    boolean boardfirst = true; //makes sure the board is only drawn once.
    int winState = -1; //checks for win
    int[] Bestmove; // holds the position of which move the bot will make

    int[] botPlayers = {-1,-1,-1};
    int difficulty; // the depth of the minimax (controlls how hard it will be)
    boolean hole = false;
    boolean ply1 = true;
    boolean ply2 = true;
    boolean ply3 = false;
    // stores the color values of the players (0 = blue, 1 = red, 2 = green)
    int Ply1Color = 1;
    int Ply2Color = 2;
    int Ply3Color = 0;
    // holds if each player is a human or computer player
    boolean ply1AI;
    boolean ply2AI;
    boolean ply3AI;
    // holds what position the difficulty slider is at
    int sliderPosition = 3;
    boolean[][] colorPressed = { //stores which button is pressed
            {true,false,false},
            {false,false,true},
            {false,true,false},
        };

    double s = 155;//scale
    int xoff = 450;//X offset
    int yoff = 270;//Y offset
    int gap = 255;// the space between each layer

    //the cordianates of the 4 vertexs of each board square 
    //(the points have been projected from the top four points of a 3D cube into an isometric 2D image.
    int[] p1 = {(int)(-1.6383040885779836 * s)+xoff,(int)(-0.816496580927726 * s) + yoff};//left
    int[] p2 = {(int)(0 * s)+xoff,(int)(-0.15418756107152065 * s) + yoff};//bottom middle
    int[] p3 = {(int)(1.6383040885779836 * s)+xoff,(int)(-0.8164965809277261 * s) + yoff};//right
    int[] p4 = {(int)(0 * s)+xoff,(int)(-1.4788056007839314 * s) + yoff};//top middle

    //helps forms points 5,6,9,10 as they are built off other points in gridP array
    int[] gridP4 = {(int) ((2 * p4[0] + p1[0]) / 3), (int) ((2 * p4[1] + p1[1]) / 3)};
    int[] gridP7 = {(int) ((2 * p3[0] + p2[0]) / 3), (int) ((2 * p3[1] + p2[1]) / 3)};
    int[] gridP8 = {(int) ((p4[0] + p1[0] * 2) / 3), (int) ((p4[1] + p1[1] * 2) / 3)};
    int[] gridP11 = {(int) ((p3[0] + p2[0] * 2) / 3), (int) ((p3[1] + p2[1] * 2) / 3)};

    //Holds the cordianates of all four corners of each player squares. (helps form the points in Spaces3DX and Y)
    int[][] gridP = {
            {p4[0],p4[1]},
            {(int) ((2 * p4[0] + p3[0]) / 3), (int) ((2 * p4[1] + p3[1]) / 3)},
            {(int) ((p4[0] + p3[0] * 2) / 3), (int) ((p4[1] + p3[1] * 2) / 3)},
            {p3[0],p3[1]},
            {(int) ((2 * p4[0] + p1[0]) / 3), (int) ((2 * p4[1] + p1[1]) / 3)},
            {(int) ((2 * gridP4[0] + gridP7[0]) / 3), (int) ((2 * gridP4[1] + gridP7[1]) / 3)},
            {(int) ((gridP4[0] + gridP7[0] * 2) / 3), (int) ((gridP4[1] + gridP7[1] * 2) / 3)},
            {(int) ((2 * p3[0] + p2[0]) / 3), (int) ((2 * p3[1] + p2[1]) / 3)},
            {(int) ((p4[0] + p1[0] * 2) / 3), (int) ((p4[1] + p1[1] * 2) / 3)},
            {(int) ((2 * gridP8[0] + gridP11[0]) / 3), (int) ((2 * gridP8[1] + gridP11[1]) / 3)},
            {(int) ((gridP8[0] + gridP11[0] * 2) / 3), (int) ((gridP8[1] + gridP11[1] * 2) / 3)},
            {(int) ((p3[0] + p2[0] * 2) / 3), (int) ((p3[1] + p2[1] * 2) / 3)},
            {p1[0],p1[1]},
            {(int) ((p2[0] + p1[0] * 2) / 3), (int) ((p2[1] + p1[1] * 2) / 3)},
            {(int) ((2 * p2[0] + p1[0]) / 3), (int) ((2 * p2[1] + p1[1]) / 3)},
            {p2[0],p2[1]},
        };

    //holds the cordiates of all four x vertiecs of every playing space. to make the buttons
    int[][][] spaces3DX = {

            {
                {gridP[0][0],gridP[1][0]-1,gridP[5][0],gridP[4][0]+1},
                {gridP[1][0],gridP[2][0]-1,gridP[6][0],gridP[5][0]+1},
                {gridP[2][0],gridP[3][0]-1,gridP[7][0],gridP[6][0]+1},

                {gridP[4][0],gridP[5][0]-1,gridP[9][0],gridP[8][0]+1},
                {gridP[5][0],gridP[6][0]-1,gridP[10][0],gridP[9][0]+1},
                {gridP[6][0],gridP[7][0]-1,gridP[11][0],gridP[10][0]+1},

                {gridP[8][0],gridP[9][0]-1,gridP[13][0],gridP[12][0]+1},
                {gridP[9][0],gridP[10][0]-1,gridP[14][0],gridP[13][0]+1},
                {gridP[10][0],gridP[11][0]-1,gridP[15][0],gridP[14][0]+1}

            },

            {
                {gridP[0][0],gridP[1][0]-1,gridP[5][0],gridP[4][0]+1},
                {gridP[1][0],gridP[2][0]-1,gridP[6][0],gridP[5][0]+1},
                {gridP[2][0],gridP[3][0]-1,gridP[7][0],gridP[6][0]+1},

                {gridP[4][0],gridP[5][0]-1,gridP[9][0],gridP[8][0]+1},
                {gridP[5][0],gridP[6][0]-1,gridP[10][0],gridP[9][0]+1},
                {gridP[6][0],gridP[7][0]-1,gridP[11][0],gridP[10][0]+1},

                {gridP[8][0],gridP[9][0]-1,gridP[13][0],gridP[12][0]+1},
                {gridP[9][0],gridP[10][0]-1,gridP[14][0],gridP[13][0]+1},
                {gridP[10][0],gridP[11][0]-1,gridP[15][0],gridP[14][0]+1}
            },

            {
                {gridP[0][0],gridP[1][0]-1,gridP[5][0],gridP[4][0]+1},
                {gridP[1][0],gridP[2][0]-1,gridP[6][0],gridP[5][0]+1},
                {gridP[2][0],gridP[3][0]-1,gridP[7][0],gridP[6][0]+1},

                {gridP[4][0],gridP[5][0]-1,gridP[9][0],gridP[8][0]+1},
                {gridP[5][0],gridP[6][0]-1,gridP[10][0],gridP[9][0]+1},
                {gridP[6][0],gridP[7][0]-1,gridP[11][0],gridP[10][0]+1},

                {gridP[8][0],gridP[9][0]-1,gridP[13][0],gridP[12][0]+1},
                {gridP[9][0],gridP[10][0]-1,gridP[14][0],gridP[13][0]+1},
                {gridP[10][0],gridP[11][0]-1,gridP[15][0],gridP[14][0]+1}
            }

        };

    //holds the cordiates of all four y vertiecs of every playing space. to make the buttons
    int[][][] spaces3DY = {

            {
                {gridP[0][1]+1,gridP[1][1],gridP[5][1]-1,gridP[4][1]},
                {gridP[1][1]+1,gridP[2][1],gridP[6][1]-1,gridP[5][1]},
                {gridP[2][1]+1,gridP[3][1],gridP[7][1]-1,gridP[6][1]},

                {gridP[4][1]+1,gridP[5][1],gridP[9][1]-1,gridP[8][1]},
                {gridP[5][1]+1,gridP[6][1],gridP[10][1]-1,gridP[9][1]},
                {gridP[6][1]+1,gridP[7][1],gridP[11][1]-1,gridP[10][1]},

                {gridP[8][1]+1,gridP[9][1],gridP[13][1]-1,gridP[12][1]},
                {gridP[9][1]+1,gridP[10][1],gridP[14][1]-1,gridP[13][1]},
                {gridP[10][1]+1,gridP[11][1],gridP[15][1]-1,gridP[14][1]},

            },

            {
                {gridP[0][1]+gap+1,gridP[1][1]+gap,gridP[5][1]+gap-1,gridP[4][1]+gap},
                {gridP[1][1]+gap+1,gridP[2][1]+gap,gridP[6][1]+gap-1,gridP[5][1]+gap},
                {gridP[2][1]+gap+1,gridP[3][1]+gap,gridP[7][1]+gap-1,gridP[6][1]+gap},

                {gridP[4][1]+gap+1,gridP[5][1]+gap,gridP[9][1]+gap-1,gridP[8][1]+gap},
                {gridP[5][1]+gap+1,gridP[6][1]+gap,gridP[10][1]+gap-1,gridP[9][1]+gap},
                {gridP[6][1]+gap+1,gridP[7][1]+gap,gridP[11][1]+gap-1,gridP[10][1]+gap},

                {gridP[8][1]+gap+1,gridP[9][1]+gap,gridP[13][1]+gap-1,gridP[12][1]+gap},
                {gridP[9][1]+gap+1,gridP[10][1]+gap,gridP[14][1]+gap-1,gridP[13][1]+gap},
                {gridP[10][1]+gap+1,gridP[11][1]+gap,gridP[15][1]+gap-1,gridP[14][1]+gap},
            },

            {
                {gridP[0][1]+gap*2+1,gridP[1][1]+gap*2,gridP[5][1]+gap*2-1,gridP[4][1]+gap*2},
                {gridP[1][1]+gap*2+1,gridP[2][1]+gap*2,gridP[6][1]+gap*2-1,gridP[5][1]+gap*2},
                {gridP[2][1]+gap*2+1,gridP[3][1]+gap*2,gridP[7][1]+gap*2-1,gridP[6][1]+gap*2},

                {gridP[4][1]+gap*2+1,gridP[5][1]+gap*2,gridP[9][1]+gap*2-1,gridP[8][1]+gap*2},
                {gridP[5][1]+gap*2+1,gridP[6][1]+gap*2,gridP[10][1]+gap*2-1,gridP[9][1]+gap*2},
                {gridP[6][1]+gap*2+1,gridP[7][1]+gap*2,gridP[11][1]+gap*2-1,gridP[10][1]+gap*2},

                {gridP[8][1]+gap*2+1,gridP[9][1]+gap*2,gridP[13][1]+gap*2-1,gridP[12][1]+gap*2},
                {gridP[9][1]+gap*2+1,gridP[10][1]+gap*2,gridP[14][1]+gap*2-1,gridP[13][1]+gap*2},
                {gridP[10][1]+gap*2+1,gridP[11][1]+gap*2,gridP[15][1]+gap*2-1,gridP[14][1]+gap*2},
            }

        };

    //Holds the player moves in the board. (-1 = empty)
    int[][][] board3D = {

            {
                {-1,-1,-1},
                {-1,-1,-1},
                {-1,-1,-1}
            },

            {
                {-1,-1,-1},
                {-1,-1,-1},
                {-1,-1,-1}
            },

            {
                {-1,-1,-1},
                {-1,-1,-1},
                {-1,-1,-1}
            }   
        };

    //constructor method
    public TicTacToeGame()
    {
        addMouseListener(this);

        //load images
        try
        {
            title = ImageIO.read(new File("Title Screen.png")); 
            background = ImageIO.read(new File("Board Screen.png"));
            RedWins = ImageIO.read(new File("Red_Wins.png"));
            BlueWins = ImageIO.read(new File("Blue_Wins.png"));
            GreenWins = ImageIO.read(new File("Green_Wins.png"));
            TieGame = ImageIO.read(new File("TieGame.png"));
            Home = ImageIO.read(new File("Home Button.png"));
            Settings = ImageIO.read(new File("Settings Icon.png"));
            SettingsBackground = ImageIO.read(new File("Settings Background.png"));
            Filter =  ImageIO.read(new File("Filter.png"));
        }
        catch (IOException e)
        {

        }
    }

    boolean aiStarted = false; // used to start new thread for the bot
    // used to draw on the canvas
    @Override
    public void paint(Graphics g) {
        if (screen == 1) {
            startScreen(g);
        } else if (screen == 2 && boardfirst == true) {
            drawBoard(g);
            boardfirst = false;
        } else if (screen == 2 && winState == -1) {
            drawPiece(g);
            winscreen(g);

            
            if (!aiStarted) {
                aiStarted = true;
                new Thread(() -> {
                            computerPlayer();
                            aiStarted = false;
                            repaint(); // Redraw after AI finishes
                    }).start();
            }

        } else if (screen == 3) {
            settingsScreen(g);
        }
    }

    public void startScreen(Graphics g) //draws title screen
    {
        g.drawImage(title, 0, 0, null);
        g.drawImage(Settings, 780, 730, null);
        Font font = new Font("Monospaced", Font.PLAIN, 32);
        g.setFont(font);
        g.setColor(Color.gray);
        g.drawString("Click To Start",320, 450);
    }

    public void settingsScreen(Graphics g) //draws settings screen and changes based on buttons pushed
    {
        g.drawImage(SettingsBackground, 0, 0, null);
        g.drawImage(Filter, 0,0, null);
        g.drawImage(Home, 10,700, null);

        //toggle players

        g.setColor(new Color(140,0,0));
        g.fillRect(45, 50, 240, 50);
        g.fillRect(330, 50, 240, 50);
        g.setColor(new Color(104,104,104));
        if (ply3 == true)
            g.setColor(new Color(140,0,0));
        g.fillRect(615, 50, 240, 50);

        g.setColor(Color.gray);
        g.fillRect(0,325,900,5);
        g.setColor(Color.black);
        g.fillRect(450,440,400,8);

        //player color select
        g.setColor(Color.white);
        Font font = new Font("Monospaced", Font.PLAIN, 32);
        g.setFont(font);
        g.drawString("Player One",68, 85);
        g.drawString("Player Two",353, 85);
        g.drawString("Player Three",618, 85);

        font = new Font("Monospaced", Font.BOLD, 18);
        g.setFont(font);
        g.drawString("All Active Players Must Have A Color",48, 145);
        font = new Font("Monospaced", Font.BOLD, 18);
        g.setFont(font);
        g.drawString("Activate Computer Players",38, 380);
        g.drawString("Computer Player Difficulty",500, 380);
        g.drawString("Enable Center Hole",440, 590);

        font = new Font("Monospaced", Font.PLAIN, 9);
        g.setFont(font);
        g.drawString("Very Easy",440, 425);
        g.drawString("Easy",535, 425);
        g.drawString("Medium",635, 425);
        g.drawString("Hard",735, 425);
        g.drawString("Impossible",825, 425);

        font = new Font("Monospaced", Font.BOLD, 16);
        g.setFont(font);
        g.drawString("Player 1",38, 410);
        g.drawString("Player 2",38, 440);
        g.drawString("Player 3",38, 470);

        font = new Font("Monospaced", Font.PLAIN, 16);
        g.setFont(font);
        g.drawString("(Optional)",618, 45);
        g.drawString("(Required)",348, 45);
        g.drawString("(Required)",63, 45);
        g.drawString("   Color Piece Red",618, 185);
        g.drawString("  Color Piece Blue",618, 235);
        g.drawString(" Color Piece Green",618, 285);
        g.drawString("   Color Piece Red",68, 185);
        g.drawString("  Color Piece Blue",68, 235);
        g.drawString(" Color Piece Green",68, 285);
        g.drawString("   Color Piece Red",353, 185);
        g.drawString("  Color Piece Blue",353, 235);
        g.drawString(" Color Piece Green",353, 285);

        // colors red pressed buttons
        if (colorPressed[2][0] == true){
            g.setColor(Color.red);
            g.fillOval(808,176,12,12);
            g.setColor(Color.white);
            g.fillOval(543,176,12,12);
            g.fillOval(258,176,12,12);
        }
        else if(colorPressed[1][0] == true){
            g.setColor(Color.red);
            g.fillOval(543,176,12,12);
            g.setColor(Color.white);
            g.fillOval(808,176,12,12);
            g.fillOval(258,176,12,12);
        }
        else if(colorPressed[0][0] == true){
            g.setColor(Color.red);
            g.fillOval(258,176,12,12);
            g.setColor(Color.white);
            g.fillOval(808,176,12,12);
            g.fillOval(543,176,12,12);
        }

        else{
            g.setColor(Color.white);
            g.fillOval(808,176,12,12);
            g.fillOval(543,176,12,12);
            g.fillOval(258,176,12,12);
        }
        // colors blue pressed buttons
        if(colorPressed[2][1] == true){
            g.setColor(Color.blue);
            g.fillOval(808,226,12,12);
            g.setColor(Color.white);
            g.fillOval(543,226,12,12);
            g.fillOval(258,226,12,12);
        }
        else if(colorPressed[1][1] == true){
            g.setColor(Color.blue);
            g.fillOval(543,226,12,12);
            g.setColor(Color.white);
            g.fillOval(808,226,12,12);
            g.fillOval(258,226,12,12);
        }
        else if(colorPressed[0][1] == true){
            g.setColor(Color.blue);
            g.fillOval(258,226,12,12);
            g.setColor(Color.white);
            g.fillOval(808,226,12,12);
            g.fillOval(543,226,12,12);
        }
        else{
            g.setColor(Color.white);
            g.fillOval(808,226,12,12);
            g.fillOval(543,226,12,12);
            g.fillOval(258,226,12,12);
        }
        // colors green pressed buttons
        if(colorPressed[2][2] == true){
            g.setColor(Color.green);
            g.fillOval(808,276,12,12);
            g.setColor(Color.white);
            g.fillOval(543,276,12,12);
            g.fillOval(258,276,12,12);
        }
        else if(colorPressed[1][2] == true){
            g.setColor(Color.green);
            g.fillOval(543,276,12,12);
            g.setColor(Color.white);
            g.fillOval(808,276,12,12);
            g.fillOval(258,276,12,12);
        }
        else if(colorPressed[0][2] == true){
            g.setColor(Color.green);
            g.fillOval(258,276,12,12);
            g.setColor(Color.white);
            g.fillOval(808,276,12,12);
            g.fillOval(543,276,12,12);
        }
        else{
            g.setColor(Color.white);
            g.fillOval(808,276,12,12);
            g.fillOval(543,276,12,12);
            g.fillOval(258,276,12,12);
        }

        // ai enable buttons
        g.drawString("Player 1",38, 410);
        g.drawString("Player 2",38, 440);
        g.drawString("Player 3",38, 470);
        if (ply1AI == true)
            g.setColor(Color.green);
        g.fillRect(125,395,15,15);
        g.setColor(Color.white);
        if (ply2AI == true)
            g.setColor(Color.green);
        g.fillRect(125,425,15,15);
        g.setColor(Color.white);
        if (ply3AI == true)
            g.setColor(Color.green);
        g.fillRect(125,455,15,15);
        g.setColor(Color.white);

        //adjusts slider position
        g.setColor(new Color(180,180,180));
        if (sliderPosition == 1)
            g.fillRect(450,430,30,30);
        else if (sliderPosition == 2)
            g.fillRect(535,430,30,30);
        else if (sliderPosition == 3)
            g.fillRect(635,430,30,30);
        else if (sliderPosition == 4)
            g.fillRect(735,430,30,30);
        else if (sliderPosition == 5)
            g.fillRect(835,430,30,30);

        //toggles center hole button
        if(hole)
            g.setColor(Color.green);
        else
            g.setColor(new Color(180,180,180));
        g.fillRect(680,570,30,30);
    }

    public void drawBoard(Graphics g) //draws game board
    {
        g.drawImage(background, 0, 0, null);
        g.drawImage(Home, 10,700, null);
        //colors in the top and sides of each of the 3 boards
        for (int i = 0; i <= 2; i++)
        {
            //points to fill right side of the cube
            int[]rightSidex ={p2[0],p3[0],p3[0],p2[0]};
            int[]rightSidey ={p2[1]+gap*i,p3[1]+gap*i,p3[1]+20+gap*i,p2[1]+20+gap*i};
            //points to fill left side of the cube
            int[]leftSidex = {p1[0],p2[0],p2[0],p1[0]};
            int[]leftSidey = {p1[1]+gap*i,p2[1]+gap*i,p2[1]+20+gap*i,p1[1]+20+gap*i};
            //points to fill top of the cube
            int[] topx = {p1[0],p2[0],p3[0],p4[0]};
            int[] topy = {p1[1]+gap*i,p2[1]+gap*i,p3[1]+gap*i,p4[1]+gap*i};

            //fills in sides and top of cube with color
            if(Ply1Color == 2)
                g.setColor(new Color(0,104,0));
            else if(Ply1Color == 0)
                g.setColor(new Color(0,0,104));
            else if(Ply1Color == 1)
                g.setColor(new Color(104,0,0));
            g.fillPolygon(rightSidex,rightSidey,4);
            g.fillPolygon(leftSidex,leftSidey,4);
            g.setColor(Color.gray);
            g.fillPolygon(topx,topy,4);
        }
        //draws a line to seperate the side faces
        g.setColor(Color.black);
        g.drawLine(p2[0],p2[1],p2[0],p2[1]+20);
        g.drawLine(p2[0],p2[1]+gap,p2[0],p2[1]+20+gap);
        g.drawLine(p2[0],p2[1]+gap*2,p2[0],p2[1]+20+gap*2);

        //draws the 3x3x3 grid
        g.setColor(Color.white);
        //holds the correct multiplier to find the point a the needed line to draw the grid
        int m;
        int M;
        // loop runs two times once for each line on a single side of the square.
        for (int i = 1; i <= 2; i++)
        {
            if (i == 1){
                m = 2;
                M = 1;
            }
            else{
                m = 1;
                M = 2;
            }
            // finds the points that connect the grid
            int[][] thirdsPoints = 
                {
                    {(int) ((m * p1[0] + p2[0] * M) / 3),(int) ((m * p1[1] + p2[1] * M) / 3)},
                    {(int) ((m * p2[0] + p3[0] * M) / 3),(int) ((m * p2[1] + p3[1] * M) / 3)},
                    {(int) ((m * p4[0] + p3[0] * M) / 3),(int) ((m * p4[1] + p3[1] * M) / 3)},
                    {(int) ((m * p1[0] + p4[0] * M) / 3),(int) ((m * p1[1] + p4[1] * M) / 3)},
                };
            //draws the first line on each side of the squares
            if (i == 1){
                g.drawLine(thirdsPoints[0][0],thirdsPoints[0][1],thirdsPoints[2][0],thirdsPoints[2][1]);
                g.drawLine(thirdsPoints[1][0],thirdsPoints[1][1],thirdsPoints[3][0],thirdsPoints[3][1]);

                g.drawLine(thirdsPoints[0][0],thirdsPoints[0][1] + gap,thirdsPoints[2][0],thirdsPoints[2][1] + gap);
                g.drawLine(thirdsPoints[1][0],thirdsPoints[1][1] + gap,thirdsPoints[3][0],thirdsPoints[3][1] + gap);

                g.drawLine(thirdsPoints[0][0],thirdsPoints[0][1] + gap*2,thirdsPoints[2][0],thirdsPoints[2][1] + gap*2);
                g.drawLine(thirdsPoints[1][0],thirdsPoints[1][1] + gap*2,thirdsPoints[3][0],thirdsPoints[3][1] + gap*2);
            }
            ////draws the second line on each side of the squares
            else{
                g.drawLine(thirdsPoints[0][0],thirdsPoints[0][1],thirdsPoints[2][0],thirdsPoints[2][1]);
                g.drawLine(thirdsPoints[1][0],thirdsPoints[1][1],thirdsPoints[3][0],thirdsPoints[3][1]);

                g.drawLine(thirdsPoints[0][0],thirdsPoints[0][1] + gap,thirdsPoints[2][0],thirdsPoints[2][1] + gap);
                g.drawLine(thirdsPoints[1][0],thirdsPoints[1][1] + gap,thirdsPoints[3][0],thirdsPoints[3][1] + gap);

                g.drawLine(thirdsPoints[0][0],thirdsPoints[0][1] + gap*2,thirdsPoints[2][0],thirdsPoints[2][1] + gap*2);
                g.drawLine(thirdsPoints[1][0],thirdsPoints[1][1] + gap*2,thirdsPoints[3][0],thirdsPoints[3][1] + gap*2);
            }
        }

        if (hole == true){
            g.setColor(Color.black);
            g.fillPolygon(spaces3DX[1][4],spaces3DY[1][4],4);
        }
        repaint();
    }

    //Checks to see position of the game board and draw corsponding pice and color to visualy repersent it
    public void drawPiece(Graphics g)
    {
        int grid = 0; // identifies which points to fill in a polygon based on the row
        for (int l = 0; l <=2; l++)
        {
            for (int r = 0; r <= 2; r++)
            {
                for (int c = 0; c <= 2; c++)
                {
                    if (r == 1)
                        grid = r+2;
                    else if (r == 2)
                        grid = r+4;
                    if (board3D[l][r][c] == 0){
                        if (Ply1Color == 1)
                            g.setColor(Color.red);
                        else if (Ply1Color == 0)
                            g.setColor(Color.blue);
                        else if (Ply1Color == 2)
                            g.setColor(new Color(0,154,0));
                        g.fillPolygon(spaces3DX[l][c+grid],spaces3DY[l][c+grid],4);
                    }
                    else if (board3D[l][r][c] == 1){
                        if (Ply2Color == 1)
                            g.setColor(Color.red);
                        else if (Ply2Color == 0)
                            g.setColor(Color.blue);
                        else if (Ply2Color == 2)
                            g.setColor(new Color(0,154,0));
                        g.fillPolygon(spaces3DX[l][c+grid],spaces3DY[l][c+grid],4);
                    }
                    else if (board3D[l][r][c] == 2){
                        if (Ply3Color == 1)
                            g.setColor(Color.red);
                        else if (Ply3Color == 0)
                            g.setColor(Color.blue);
                        else if (Ply3Color == 2)
                            g.setColor(new Color(0,154,0));
                        g.fillPolygon(spaces3DX[l][c+grid],spaces3DY[l][c+grid],4);
                    }

                }
                grid = 0;
            }
        }

        if (winState(board3D)[0][0] == -1){
            //Colors in the side of the board to show which player is next
            for (int i = 0; i <= 2; i++)
            {
                //points to fill right side of the cube
                int[]rightSidex ={p2[0],p3[0],p3[0],p2[0]};
                int[]rightSidey ={p2[1]+gap*i,p3[1]+gap*i,p3[1]+20+gap*i,p2[1]+20+gap*i};
                //points to fill left side of the cube
                int[]leftSidex = {p1[0],p2[0],p2[0],p1[0]};
                int[]leftSidey = {p1[1]+gap*i,p2[1]+gap*i,p2[1]+20+gap*i,p1[1]+20+gap*i};
                //points to fill top of the cube
                int[] topx = {p1[0],p2[0],p3[0],p4[0]};
                int[] topy = {p1[1]+gap*i,p2[1]+gap*i,p3[1]+gap*i,p4[1]+gap*i};

                //fills in sides and top of cube with color
                if (turn % players == 1){
                    if(Ply2Color == 2)
                        g.setColor(new Color(0,104,0));
                    else if(Ply2Color == 0)
                        g.setColor(new Color(0,0,104));
                    else if(Ply2Color == 1)
                        g.setColor(new Color(104,0,0));
                }
                else if (turn % players == 0){
                    if(Ply1Color == 2)
                        g.setColor(new Color(0,104,0));
                    else if(Ply1Color == 0)
                        g.setColor(new Color(0,0,104));
                    else if(Ply1Color == 1)
                        g.setColor(new Color(104,0,0));
                }
                else if (turn % players == 2){
                    if(Ply3Color == 2)
                        g.setColor(new Color(0,104,0));
                    else if(Ply3Color == 0)
                        g.setColor(new Color(0,0,104));
                    else if(Ply3Color == 1)
                        g.setColor(new Color(104,0,0));
                }
                g.fillPolygon(rightSidex,rightSidey,4);
                g.fillPolygon(leftSidex,leftSidey,4);
                g.setColor(Color.black);
                g.drawLine(p2[0],p2[1],p2[0],p2[1]+20);
                g.drawLine(p2[0],p2[1]+gap,p2[0],p2[1]+20+gap);
                g.drawLine(p2[0],p2[1]+gap*2,p2[0],p2[1]+20+gap*2);
            }
        }
    }

    public void winscreen(Graphics g)
    {
        //calls method to check if the board is in a winning state and who won
        winState = winState(board3D)[0][0];
        int grid = 0;
        // highlights the winnig move and displays a message on who won
        for (int i = 1; i <= 3; i++)
        {
            if (winState(board3D)[i][1] == 1)
                grid = winState(board3D)[i][1]+2;
            else if (winState(board3D)[i][1] == 2)
                grid = winState(board3D)[i][1]+4;

            if (winState == 0){
                if(Ply1Color == 2){
                    g.setColor(new Color(150,255,150));
                    g.drawImage(GreenWins, -72, -70, null);
                }
                else if(Ply1Color == 0){
                    g.setColor(new Color(150,150,255));
                    g.drawImage(BlueWins, -72, -70, null);
                }
                else if(Ply1Color == 1){
                    g.setColor(new Color(255,150,150));
                    g.drawImage(RedWins, -72, -70, null);
                }
                g.fillPolygon(spaces3DX[winState(board3D)[i][0]][winState(board3D)[i][2] + grid],spaces3DY[winState(board3D)[i][0]][winState(board3D)[i][2] + grid],4);
            }
            else if (winState == 1 ){
                if(Ply2Color == 2){
                    g.setColor(new Color(150,255,150));
                    g.drawImage(GreenWins, -72, -70, null);
                }
                else if(Ply2Color == 0){
                    g.setColor(new Color(150,150,255));
                    g.drawImage(BlueWins, -72, -70, null);
                }
                else if(Ply2Color == 1){
                    g.setColor(new Color(255,150,150));
                    g.drawImage(RedWins, -72, -70, null);
                }
                g.fillPolygon(spaces3DX[winState(board3D)[i][0]][winState(board3D)[i][2] + grid],spaces3DY[winState(board3D)[i][0]][winState(board3D)[i][2] + grid],4);
            }
            else if (winState == 2 ){
                if(Ply3Color == 2){
                    g.setColor(new Color(150,255,150));
                    g.drawImage(GreenWins, -72, -70, null);
                }
                else if(Ply3Color == 0){
                    g.setColor(new Color(150,150,255));
                    g.drawImage(BlueWins, -72, -70, null);
                }
                else if(Ply3Color == 1){
                    g.setColor(new Color(255,150,150));
                    g.drawImage(RedWins, -72, -70, null);
                }
                g.fillPolygon(spaces3DX[winState(board3D)[i][0]][winState(board3D)[i][2] + grid],spaces3DY[winState(board3D)[i][0]][winState(board3D)[i][2] + grid],4);
            }
            else if (winState == -5 ){
                g.drawImage(TieGame, -72, -70, null);
            }
            grid = 0;
        }
    }

    //when called detrimins the best move throught minimax then implements it.
    public void computerPlayer()
    {

        if (turn % players == botPlayers[0] || turn % players == botPlayers[1] || turn % players == botPlayers[2]){
            //sets dificulty based on slider position
            if (sliderPosition == 1) 
                difficulty = 1;
            else if (sliderPosition == 2) 
                difficulty = 3;
            else if (sliderPosition == 3) 
                difficulty = 5;
            else if (sliderPosition == 4) 
                difficulty = 7;
            else
                difficulty = 21;

            //evaluates move with minimax and returns it
            minimax(board3D,difficulty,Integer.MIN_VALUE,Integer.MAX_VALUE,true, difficulty);
            board3D[Bestmove[0]][Bestmove[1]][Bestmove[2]] = turn % players;
            turn++;
            repaint();
        }
    }

    //rates the move from bestMove returning how much it benifits the bot. (assumes bot is the maximizer)
    public int minimax(int[][][] board3D, int depth, int alpha, int beta, boolean maximizer,int difficulty)
    {
        // evaluates the score of the final position of the board based on how many moves have led up to it or if a player has won
        if (depth == 0 || winState(board3D)[0][0] == -5 || winState(board3D)[0][0] >= 0)
        {
            if (winState(board3D)[0][0] == -5) 
                return 0;
            else if (winState(board3D)[0][0] == turn % players)
                return  1 + depth;
            else if (winState(board3D)[0][0] == (turn+1) % players)
                return -1 - depth;
            else
                return 0;
        }

        //runs this code and evaluates which option provides the best outcome when it is the maximizers turn.
        if (maximizer == true)
        {
            int maxEval = Integer.MIN_VALUE;
            int eval;
            int[] move = {-1,-1,-1};
            for (int l = 0; l <= 2; l++)
            {
                for (int r = 0; r <= 2; r++)
                {
                    for (int c = 0; c <= 2; c++)
                    {
                        // loops throught all moves and uses recursion to make each possible move an input. (this continues untill a win tie or a max depth)
                        if (board3D[l][r][c] == -1){
                            board3D[l][r][c] = turn % players;
                            eval = minimax(board3D,depth - 1,alpha,beta,false,difficulty);
                            board3D[l][r][c] = -1;
                            if (depth == difficulty && eval > maxEval){
                                move = new int[]{l, r, c};
                            }
                            maxEval = Math.max(eval,maxEval);
                            // determines whether a possible branch needs to be checked based on if their is another branch that would always be favoured more by the maximizer or minimizer
                            alpha = Math.max(eval,alpha);
                            if (beta <= alpha)
                                return maxEval;
                        }
                    }
                }
            }
            if (depth == difficulty)
            {
                Bestmove(move);
            }
            return maxEval;
        }
        //runs this code and evaluates which option provides the best outcome when it is the mainimizers turn.
        else
        {
            int minEval = Integer.MAX_VALUE;
            int eval;
            int[] move = {-1,-1,-1};
            for (int l = 0; l <= 2; l++)
            {
                for (int r = 0; r <= 2; r++)
                {
                    for (int c = 0; c <= 2; c++)
                    {
                        // loops throught all moves and uses recursion to make each possible move an input. (this continues untill a win tie or a max depth)
                        if (board3D[l][r][c] == -1){
                            board3D[l][r][c] = (turn + 1) % players;
                            eval = minimax(board3D,depth - 1,alpha,beta,true,difficulty);
                            board3D[l][r][c] = -1;
                            minEval = Math.min(eval,minEval);
                            beta = Math.min(eval,beta);
                            if (beta <= alpha)
                                return minEval;
                        }
                    }
                }
            }
            return minEval;
        }
    }

    // as minimax returns an int this function allows the best move to be written to the ai function
    public void Bestmove(int[] move)
    {
        Bestmove = move;
    }

    public int[][] winState(int[][][] board3D) //returns an array containing which player won and which board postions comprise the winning move.
    {
        // loops throught all position to if a win is achived.
        for (int l = 0; l <= 2; l++)
        {
            for (int r = 0; r <= 2; r++)
            {
                for (int c = 0; c <= 2; c++)
                {
                    if ( c == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l][r][c+1] && board3D[l][r][c] == board3D[l][r][c+2]) //checks for horziontal win in 2D
                        return new int[][] { //returns who won and the positions that won 
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l,r,c+1},
                            {l,r,c+2}
                        };

                    else if (r == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l][r+1][c] && board3D[l][r][c] == board3D[l][r+2][c]) //checks for vertical win in 2D
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l,r+1,c},
                            {l,r+2,c}
                        };
                    else if (c == 0 && r == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l][r+1][c+1] && board3D[l][r][c] == board3D[l][r+2][c+2])//checks 2D diagnoal L-R
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l,r+1,c+1},
                            {l,r+2,c+2}
                        };
                    else if (c == 0 && r == 2 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l][r-1][c+1] && board3D[l][r][c] == board3D[l][r-2][c+2]) //checks 2D diagnoal R-L
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l,r-1,c+1},
                            {l,r-2,c+2}
                        };
                    else if (l == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l+1][r][c] && board3D[l][r][c] == board3D[l+2][r][c]) //Checcks Vertical 3D win
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l+1,r,c},
                            {l+2,r,c}
                        };
                    else if (l == 0 && c == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l+1][r][c+1] && board3D[l][r][c] == board3D[l+2][r][c+2]) //checks 3D diagnoal L-R
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l+1,r,c+1},
                            {l+2,r,c+2}
                        };
                    else if (l == 2 && c == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l-1][r][c+1] && board3D[l][r][c] == board3D[l-2][r][c+2]) //checks 3D diagnoal R-L
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l-1,r,c+1},
                            {l-2,r,c+2}
                        };
                    else if (l == 0 && r == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l+1][r+1][c] && board3D[l][r][c] == board3D[l+2][r+2][c]) //checks 3D diagnoal B-F
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l+1,r+1,c},
                            {l+2,r+2,c}
                        };
                    else if (l == 2 && r == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l-1][r+1][c] && board3D[l][r][c] == board3D[l-2][r+2][c]) //checks 3D diagnoal F-B
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l-1,r+1,c},
                            {l-2,r+2,c}
                        };
                    else if (l == 0 && r == 0 && c == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l+1][r+1][c+1] && board3D[l][r][c] == board3D[l+2][r+2][c+2]) //checks 3D diagnoal throught middle T-B
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l+1,r+1,c+1},
                            {l+2,r+2,c+2}
                        };
                    else if (l == 2 && r == 0 && c == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l-1][r+1][c+1] && board3D[l][r][c] == board3D[l-2][r+2][c+2]) //checks 3D diagnoal throught middle B-T
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l-1,r+1,c+1},
                            {l-2,r+2,c+2}
                        };
                    else if (l == 0 && r == 2 && c == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l+1][r-1][c+1] && board3D[l][r][c] == board3D[l+2][r-2][c+2]) //checks 3D diagnoal throught middle L-R
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l+1,r-1,c+1},
                            {l+2,r-2,c+2}
                        };
                    else if (l == 2 && r == 2 && c == 0 && board3D[l][r][c] != -1 && board3D[l][r][c] == board3D[l-1][r-1][c+1] && board3D[l][r][c] == board3D[l-2][r-2][c+2]) //checks 3D diagnoal throught middle R-L
                        return new int[][] {
                            {board3D[l][r][c]},
                            {l,r,c},
                            {l-1,r-1,c+1},
                            {l-2,r-2,c+2}
                        };
                }
            }
        }
        return boardTie(board3D);
    }

    public int[][] boardTie (int[][][] board3D)
    {
        // loops throught all position to see if a tie is achived. (all spaces full but no win)
        for (int l = 0; l <= 2; l++)
        {
            for (int r = 0; r <= 2; r++)
            {
                for (int c = 0; c <= 2; c++)
                {
                    if (board3D[l][r][c] == -1)
                        return new int[][] {
                            {-1}, // no tie
                            {Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE},
                            {Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE},
                            {Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE}
                        };
                }
            }
        }
        return new int[][] {
            {-5}, // means a tie has happend
            {Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE},
            {Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE},
            {Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE}
        };
    }

    public void mouseClicked (MouseEvent e) // checks if button is pressed
    {

    }

    public void mousePressed (MouseEvent e)
    {
        // cant move on AI turn
        if( aiStarted == false){
            int x = e.getX();
            int y = e.getY();
            if (screen == 1)// title screen
            {
                //start button
                if (x >= 0 && x <= 900 && y >= 100 && y <= 800)
                {
                    screen = 2; 
                    if (hole == true)
                        board3D[1][1][1] = -5000;
                }
                //settings button
                if (x >= 780 && x <= 880 && y >= 730 && y <= 830)
                {
                    screen = 3;
                }
            }

            else if (screen == 2)// board screen
            {
                //Loop throught all button positions to determin which one was clicked on screen two. Then adjusts the boared position
                int xCenter;
                int yCenter;
                int pesudoTurn; // resets the turn back to 1 if it is above so the algorithem can determine what pice to color in
                int[] rowJump = {1,4,0,5};
                for (int l = 0; l <= 2; l++)//layers of boards
                {
                    for (int r = 0; r <= 2; r++)//rows
                    {
                        //determines how to find the center of the button based on which collum it is located on.
                        if (r == 1){ 
                            rowJump[0] = 8;
                            rowJump[1] = 5;
                            rowJump[2] = 4;
                            rowJump[3] = 9;
                        }
                        else if (r == 2){
                            rowJump[0] = 12;
                            rowJump[1] = 9;
                            rowJump[2] = 8;
                            rowJump[3] = 13;
                        }
                        for (int c = 0; c <= 2; c++)//collums
                        {
                            xCenter = (gridP[c+rowJump[0]][0] + gridP[c+rowJump[1]][0]) / 2;
                            yCenter = (gridP[c+rowJump[2]][1] + gridP[c+rowJump[3]][1]) / 2 + (gap * l);
                            if (hole == true && l == 1 && r == 1 && c == 1) // makes center square unplayable if their are only 2 players
                                board3D[1][1][1] = Integer.MIN_VALUE;
                            else if (y - yCenter <= (((15/37.0) * x)+30) - ((15/37.0) * xCenter) && y - yCenter <= (((-15/37.0) * x) + 30) + ((15/37.0) * xCenter) && y - yCenter >= (((15/37.0) * x) - 30) - ((15/37.0) * xCenter) && y - yCenter >= (((-15/37.0) * x) - 30) + ((15/37.0) * xCenter) && board3D[l][r][c] == -1) 
                            {
                                board3D[l][r][c] = turn % players;
                                if (turn > 3)
                                    pesudoTurn = 1;
                                else{
                                    pesudoTurn = turn % 3;
                                    if (pesudoTurn == 0)
                                        pesudoTurn++;
                                }
                                if (players == 3 && pesudoTurn % 2 == 0)
                                    board3D[l][r][c] = 0;
                                else if (players == 3 && pesudoTurn % 3 == 0)
                                    board3D[l][r][c] = 2;
                                turn++;
                            }
                        }
                    }
                    rowJump[0] = 1;
                    rowJump[1] = 4;
                    rowJump[2] = 0;
                    rowJump[3] = 5;
                }

                if (x >= 10 && x <= 110 && y >= 700 && y <= 800) //reset button that bring back to home and resets all action done
                {
                    board3D = new int[][][] {

                        {
                            {-1,-1,-1},
                            {-1,-1,-1},
                            {-1,-1,-1}
                        },

                        {
                            {-1,-1,-1},
                            {-1,-1,-1},
                            {-1,-1,-1}
                        },

                        {
                            {-1,-1,-1},
                            {-1,-1,-1},
                            {-1,-1,-1}
                        }   
                    };
                    screen = 1;  
                    turn = 6; 
                    boardfirst = true; 
                    winState = -1;

                }
            }// contains buttons for settings that adjust game values
            else if (screen == 3){
                if (x >= 10 && x <= 110 && y >= 700 && y <= 800 && ((Ply1Color >= 0 && Ply2Color >= 0 && Ply3Color >= 0) ||(Ply1Color >= 0 && Ply2Color >= 0 && !ply3)) && ((Ply1Color != Ply2Color && Ply1Color != Ply3Color && Ply2Color != Ply3Color && ply1 && ply2 && ply3) || (Ply1Color != Ply2Color && ply2 && ply1  && !ply3))){
                    screen = 1;
                }

                // 3rd player enable buttons
                if (x >= 615 && x <= 855 && y >= 50 && y<= 100){
                    if (ply3 == false){
                        ply3 = true;
                        players++;
                    }
                    else{
                        ply3 = false;
                        players--;
                    }
                }

                int xshift;
                // change color of the diffrent players and modifies buttons to repersent that
                for (int j = 1; j <= 3; j++){
                    if (j == 1)
                        xshift = 814;
                    else if (j == 2)
                        xshift = 549;
                    else
                        xshift = 263;
                    for (int yshift = 0; yshift <= 2; yshift++){
                        if (Math.pow((x-xshift),2)+Math.pow(y-(182 +(yshift*50)),2) <= 100)
                        {
                            if (j == 3 && ply1 == true)
                            {
                                if (yshift == 0){
                                    Ply1Color = 1;
                                    if (Ply3Color == 1 && ply3)
                                        Ply3Color = -1;
                                    if (Ply2Color == 1)
                                        Ply2Color = -1;
                                    colorPressed[0][0] = true;
                                    colorPressed[1][0] = false;
                                    colorPressed[2][0] = false;
                                    colorPressed[0][1] = false;
                                    colorPressed[0][2] = false;
                                }
                                else if (yshift == 1){
                                    Ply1Color = 0;
                                    if (Ply3Color == 0 && ply3)
                                        Ply3Color = -1;
                                    if (Ply2Color == 0)
                                        Ply2Color = -1;
                                    colorPressed[0][1] = true;
                                    colorPressed[1][1] = false;
                                    colorPressed[2][1] = false;
                                    colorPressed[0][0] = false;
                                    colorPressed[0][2] = false;
                                }
                                else if (yshift == 2){
                                    Ply1Color = 2;
                                    if (Ply3Color == 2 && ply3)
                                        Ply3Color = -1;
                                    if (Ply2Color == 2)
                                        Ply2Color = -1;
                                    colorPressed[0][2] = true;
                                    colorPressed[1][2] = false;
                                    colorPressed[2][2] = false;
                                    colorPressed[0][0] = false;
                                    colorPressed[0][1] = false;
                                }
                            }

                            if (j == 2  && ply2 == true)
                            {
                                if (yshift == 0){
                                    Ply2Color = 1;
                                    if (Ply3Color == 1 && ply3)
                                        Ply3Color = -1;
                                    if (Ply1Color == 1)
                                        Ply1Color = -1;
                                    colorPressed[1][0] = true;
                                    colorPressed[0][0] = false;
                                    colorPressed[2][0] = false;
                                    colorPressed[1][1] = false;
                                    colorPressed[1][2] = false;
                                }
                                else if (yshift == 1){
                                    Ply2Color = 0;
                                    if (Ply3Color == 0 && ply3)
                                        Ply3Color = -1;
                                    if (Ply1Color == 0)
                                        Ply1Color = -1;
                                    colorPressed[1][1] = true;
                                    colorPressed[0][1] = false;
                                    colorPressed[2][1] = false;
                                    colorPressed[1][0] = false;
                                    colorPressed[1][2] = false;
                                }
                                else if (yshift == 2){
                                    Ply2Color = 2;
                                    if (Ply3Color == 2 && ply3)
                                        Ply3Color = -1;
                                    if (Ply1Color == 2)
                                        Ply1Color = -1;
                                    colorPressed[1][2] = true;
                                    colorPressed[0][2] = false;
                                    colorPressed[2][2] = false;
                                    colorPressed[1][1] = false;
                                    colorPressed[1][0] = false;
                                }
                            }

                            if (j == 1  && ply3 == true)
                            {
                                if (yshift == 0){
                                    Ply3Color = 1;
                                    if (Ply2Color == 1)
                                        Ply2Color = -1;
                                    if (Ply1Color == 1)
                                        Ply1Color = -1;
                                    colorPressed[2][0] = true;
                                    colorPressed[0][0] = false;
                                    colorPressed[1][0] = false;
                                    colorPressed[2][1] = false;
                                    colorPressed[2][2] = false;
                                }
                                else if (yshift == 1){
                                    Ply3Color = 0;
                                    if (Ply2Color == 0)
                                        Ply2Color = -1;
                                    if (Ply1Color == 0)
                                        Ply1Color = -1;
                                    colorPressed[2][1] = true;
                                    colorPressed[1][1] = false;
                                    colorPressed[0][1] = false;
                                    colorPressed[2][0] = false;
                                    colorPressed[2][2] = false;
                                }
                                else if (yshift == 2){
                                    Ply3Color = 2;
                                    if (Ply2Color == 2)
                                        Ply2Color = -1;
                                    if (Ply1Color == 2)
                                        Ply1Color = -1;
                                    colorPressed[2][2] = true;
                                    colorPressed[0][2] = false;
                                    colorPressed[1][2] = false;
                                    colorPressed[2][0] = false;
                                    colorPressed[2][1] = false;
                                }
                            }
                        }
                    }
                }

                if (x >= 115 && x <= 150 && y >= 385 && y<= 420){
                    if (ply1AI == false){
                        ply1AI = true;
                        botPlayers[0] = 0;
                    }
                    else{
                        ply1AI = false;
                        botPlayers[0] = -1;
                    }
                }

                if (x >= 115 && x <= 150 && y >= 415 && y<= 440){
                    if (ply2AI == false){
                        ply2AI = true;
                        botPlayers[1] = 1;
                    }
                    else{
                        ply2AI = false;
                        botPlayers[1] = -1;
                    }
                }

                if (x >= 115 && x <= 150 && y >= 445 && y<= 480){
                    if (ply3AI == false){
                        ply3AI = true;
                        botPlayers[2] = 2;
                    }
                    else{
                        ply3AI = false;
                        botPlayers[2] = -1;
                    }
                }

            }
            // adjusts difficulty slider when pressed
            if (x >= 440 && x <= 530 && y >= 430 && y<= 460){
                sliderPosition = 1;
            }

            if (x >= 531 && x <= 610 && y >= 430 && y<= 460){
                sliderPosition = 2;
            }

            if (x >= 611 && x <= 690 && y >= 430 && y<= 460){
                sliderPosition = 3;
            }

            if (x >= 691 && x <= 770 && y >= 430 && y<= 460){
                sliderPosition = 4;
            }

            if (x >= 771 && x <= 860 && y >= 430 && y<= 460){
                sliderPosition = 5;
            }
            // toogle hole button
            if (x >= 670 && x <= 710 && y >= 560 && y<= 610){
                if(hole)
                hole = false;
                else
                hole = true;
            }
            //calls paint method to display new information
            repaint();
        }
    }

    public void mouseReleased (MouseEvent e)
    {

    }

    public void mouseEntered (MouseEvent e)
    {

    }

    public void mouseExited (MouseEvent e)
    {

    }
}
