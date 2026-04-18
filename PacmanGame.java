package Pacman;

import java.awt.*;                     // awt       =         "Abstract Window Kit"            Creating the User Interface Java Original Library
import java.awt.event.*;               // This handles user actions. Without this, your program wouldn't know if you clicked a mouse, pressed a key, or moved a window.
import java.util.HashSet;              //As we discussed, a HashSet is a way to store a group of items where no duplicates are allowed.
import java.util.Random;
import javax.swing.*;                 //If AWT is the engine, Swing is the sleek car body. Swing is built on top of AWT and provides much mo
// re sophisticated components like buttons (JButton), panels (JPanel), and the main window frame (JFrame).

public class PacmanGame extends JPanel implements ActionListener, KeyListener{
    class Block{
        int x,y, width, height, startX, startY;
        Image image;
        char direction = 'U';
        int velocityX = 0, velocityY = 0;

        Block(Image image, int x, int y, int width, int height){
        this.image = image;
        this.x = x;
        this.y =y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
        }

        void updateDirection(char direction){
            char preDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;
            for(Block wall: walls){
                if(collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = preDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity(){
            if(this.direction == 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize/4;
            }
            else if(this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }
            else if(this.direction == 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }
            else if(this.direction == 'R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }

        void reset(){
            this.x = this.startX;
            this.y = this.startY;
        }
    }


    private int rowCount = 21;
    private int columnCount = 20;
    private int tileSize = 32;
    private int boardWidth = columnCount*tileSize;
    private int boardHeight = rowCount*tileSize;
    private Image wallImage, cherryImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage, pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;
    private String[] tileMap ={
            "XXXXXXXXXXXXXXXXXXXX",
            "XC        X       CX",
            "X  XX XXX X XXX XX X",
            "X                  X",
            "XX XX X XXXXX X XX X",
            "X     X   C   X    X",
            "X        rpo       X",
            "X X X  X   X  X  X X",
            "X X X XOX XOX XX X X",
            "X XXX XXX XXX X XX X",
            "X X X X X X X X  X X",
            "X                  X",
            "X XXX X XXXXX X XX X",
            "X         X        X",
            "X  XX XXX X XXX XX X",
            "X   X     P     X  X",
            "XX  X X XXXXX X X XX",
            "X     X   X   X    X",
            "X  XXXXXX X XXXXXX X",
            "XC        b       CX",
            "XXXXXXXXXXXXXXXXXXXX"
    };

    Timer gameLoop;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    HashSet<Block> cherrys;
    Block pacman;

    char[] directions = {'U','L','D','R'};
    Random random = new Random();
    int score = 0, life = 3;
    boolean gameOver = false;




    PacmanGame(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);

        addKeyListener(this);
        setFocusable(true);

        wallImage = new ImageIcon(getClass().getResource("./source/wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./source/blueGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./source/pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./source/redGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./source/orangeGhost.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("./source/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./source/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./source/pacmanleft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./source/pacmanRight.png")).getImage();
        cherryImage = new ImageIcon(getClass().getResource("./source/cherry.png")).getImage();

        loadMap();
        for(Block ghost: ghosts){
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        cherrys = new HashSet<Block>();

        for(int r=0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if(tileMapChar == 'X'){
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if(tileMapChar == 'b'){
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'r'){
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'o'){
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'p'){
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'P'){
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if(tileMapChar == ' '){
                    Block food = new Block(null, x+14, y+14, 4, 4);
                    foods.add(food);
                }
                else if(tileMapChar == 'C'){
                    Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
                    cherrys.add(cherry);
                }

            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for(Block ghost: ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height,null);
        }

        for (Block wall: walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height,null);
        }

        for(Block cherry: cherrys){
            g.drawImage(cherryImage, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }

        g.setColor(Color.white);
        for(Block food: foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        if(gameOver){
            g.drawString("Game Over: "+ String.valueOf(score),tileSize/2,tileSize/2);
        }
        else{
            g.drawString("x"+String.valueOf(life)+" Score: "+String.valueOf(score),tileSize/2,tileSize/2);
        }
    }

    public void move(){
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        for (Block wall: walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        for(Block ghost: ghosts){
            if(collision(ghost,pacman)){
                life -= 1;
                if(life == 0){
                    gameOver = true;
                }
                resetPositions();
                pacman.image = pacmanRightImage;
            }
            if(ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D')
                ghost.updateDirection('U');

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;


            for (Block wall: walls){
                if(collision(ghost, wall) || ghost.x<=0 || ghost.x+ghost.width >= boardWidth) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }

        Block foodEaten = null;
        for(Block food: foods){
            if(collision(pacman, food)){
                foodEaten = food;
                score += 10;
            }
        }
        foods.remove(foodEaten);

        Block cherryEaten = null;
        for(Block cherry: cherrys){
            if(collision(pacman, cherry)){
                cherryEaten = cherry;
                score += 50;
            }
        }
        cherrys.remove(cherryEaten);

        if(foods.isEmpty() && cherrys.isEmpty()){
            loadMap();
            resetPositions();
        }

    }

    public boolean collision(Block a, Block b){
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y+b.height &&
                a.y+a.height > b.y;
    }

    public void resetPositions(){
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for(Block ghost : ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if(gameOver) gameLoop.stop();
    }

    @Override
    public void keyTyped(KeyEvent e){/*Not needed*/}
    public void keyPressed(KeyEvent e){/*Not needed*/}
    public void keyReleased(KeyEvent e){

        if(gameOver){
            loadMap();
            resetPositions();
            life = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }


        if(e.getKeyCode() == KeyEvent.VK_UP) pacman.updateDirection('U');
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) pacman.updateDirection('D');
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) pacman.updateDirection('L');
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT) pacman.updateDirection('R');

        if(pacman.direction == 'U') pacman.image = pacmanUpImage;
        else if(pacman.direction == 'D') pacman.image = pacmanDownImage;
        else if(pacman.direction == 'L') pacman.image = pacmanLeftImage;
        else if(pacman.direction == 'R') pacman.image = pacmanRightImage;

    }
}
