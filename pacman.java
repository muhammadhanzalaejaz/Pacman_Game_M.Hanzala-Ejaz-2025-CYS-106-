import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.HashSet;




public class pacman extends JPanel implements ActionListener , KeyListener{              // al for game loop              implements take on the properties of some class

    @Override
    public void actionPerformed(ActionEvent e) {                            // search this on net
        move();                              // Thus first it moves and the is repainted
        repaint();
        if (game_over) {
            gameloop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {                            // triggers a function when the key is released after pressing
        if (game_over){
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
            load_map();      //adds food back in the map
            reset_positions();
            lives = 3;
            score = 0;
            game_over = false;
            gameloop.start();}
        }


        if (e.getKeyCode() == KeyEvent.VK_UP){
            pacMan.update_direction('U');
        }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            pacMan.update_direction('D');
        }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            pacMan.update_direction('L');
        }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacMan.update_direction('R');

        }
        if(pacMan.direction == 'U'){
            pacMan.image = pacman_up_image;
        }else if(pacMan.direction == 'D'){
            pacMan.image = pacman_down_image;
        }else if(pacMan.direction == 'L'){
            pacMan.image = pacman_left_image;
        }else if(pacMan.direction == 'R'){
            pacMan.image = pacman_right_image;
        }
    }

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int start_x;
        int start_y;
        char direction ='U';             // U,D,L,R <==> up, down, left and right
        int velocityx = 0;
        int velocityy = 0;              // 0,0  for th walls


        Block(Image i,int x,int y,int w,int h){
            this.image = i;
            this.x =x;
            this.y = y;
            this.width =w;
            this.height =h;

            this.start_x = x;
            this.start_y = y;

        }

        void update_direction(char d){
            char prev_direction =this.direction;
            this.direction = d;
            update_velocity();
            this.x +=this.velocityx;
            this.y +=this.velocityy;
            for (Block wall:walls){
                if(collision(this,wall)){                             //this to allow use of it for the ghosts to
                    this.x-=this.velocityx;
                    this.y-=this.velocityy;
                    this.direction =prev_direction;
                    update_velocity();
                }
            }
        }                                                                             // direction parameters
                                                                                      //           -y
        void update_velocity(){                                                       //   -x --------- +x
            if (this.direction == 'U'){                                               //           +y
                this.velocityx = 0;
                this.velocityy = -tile_size/4;             // 8 pixels so every frame we are gonna up 8 pixels
            }else if (this.direction == 'D'){
                this.velocityx = 0;
                this.velocityy = +(tile_size/4);
            }else if (this.direction == 'L'){
                this.velocityx = -(tile_size/4);
                this.velocityy = 0;
            }else if (this.direction == 'R'){
                this.velocityx = +tile_size/4;
                this.velocityy = 0;
            }
        }
        void reset(){
            this.x=this.start_x;
            this.y=this.start_y;

        }
    }
    private int row_count = 21;
    private int column_count =20;
    private int tile_size = 32;
    private int board_width = column_count*tile_size;
    private int board_height = row_count *tile_size;

    private HashSet<Block> walls;
    private HashSet<Block> foods;
    private HashSet<Block> ghosts;
    private HashSet<Block> cherrys;
    private Block pacMan;

    Timer gameloop ;
    char [] directions ={'U','D','L','R'};
    Random random = new Random();

    int score = 0;
    int lives= 3;
    boolean game_over = false ;
    boolean win = false;

    // __________MAP____________
    // 'X' = wall---- ' ' = food ------'O' = nothing
    //  'C' = Cherry ---- 'P' = pacman
    // ghosts : r,p,o,b (color wise)
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

    private Image wall_image;
    private Image red_ghost_image;
    private Image blue_ghost_image;
    private Image pink_ghost_image;
    private Image orange_ghost_image;
    private Image pacman_up_image;
    private Image pacman_down_image;
    private Image pacman_right_image;
    private Image pacman_left_image;
    private Image cherry_image;



    private Image load_image(String path){
        return new ImageIcon(getClass().getResource(path)).getImage();
    }
    //  -----------------------------------constructor------------------------------------

    pacman(){
        setPreferredSize(new Dimension(board_width,board_height));
        setBackground(Color.BLACK);

        addKeyListener(this);                      // to allow key press
        // to ensure that only j panel is listening for the key
        setFocusable(true);
        //----------------------Now loading the images-----------------------

        wall_image = load_image("./wall.png");
        red_ghost_image = load_image("./redGhost.png");
        blue_ghost_image =load_image("./blueGhost.png");
        pink_ghost_image = load_image("./pinkGhost.png");
        orange_ghost_image = load_image("./orangeGhost.png");
        pacman_up_image = load_image("./PacmanUp.png");
        pacman_down_image = load_image("./pacmanDown.png");
        pacman_left_image = load_image("./pacmanLeft.png");
        pacman_right_image = load_image("./pacmanRight.png");
        cherry_image =load_image("./cherry.png");

        load_map();
        for (Block ghost :ghosts){
            char new_direction =directions[random.nextInt(4)];    // first create the random object and then rd.nextInt(4) 0-4 where 4 is excluded
            ghost.update_direction(new_direction);
        }

        // how long it takes to start the timer , milliseconds gone before frames
        //20 fps (1000/50)
        gameloop = new Timer(50,this);           //this is the pacman object j panel        timer search?
        gameloop.start();          //to start game
    }
    public void load_map(){
        walls = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        foods = new HashSet<Block>();
        cherrys = new HashSet<Block>();

        for (int r =0;r< row_count;r++){                       // r = row
            for(int c = 0; c<column_count; c++){               // c = column
                String row = tileMap[r];
                char tileMapchar = row.charAt(c);
                int x =c * tile_size;                    // how many columns from the left
                int y = r *tile_size;               // how many rows from the top == y

                if (tileMapchar == 'X'){             // X = block(wall)
                    Block wall = new Block(wall_image,x,y,tile_size,tile_size);
                    walls.add(wall);
                } else if(tileMapchar == 'b'){
                    Block ghost = new Block(blue_ghost_image,x,y,tile_size,tile_size);
                    ghosts.add(ghost);
                }else if(tileMapchar == 'r'){
                    Block ghost = new Block(red_ghost_image,x,y,tile_size,tile_size);
                    ghosts.add(ghost);
                }else if(tileMapchar == 'o'){
                    Block ghost = new Block(orange_ghost_image,x,y,tile_size,tile_size);
                    ghosts.add(ghost);
                }else if(tileMapchar == 'p'){
                    Block ghost = new Block(pink_ghost_image,x,y,tile_size,tile_size);
                    ghosts.add(ghost);
                }else if(tileMapchar == 'P'){
                    pacMan = new Block(pacman_right_image,x,y,tile_size,tile_size);

                }else if(tileMapchar == ' '){
                    Block food = new Block(null,x +14,y+14,4,4);           // 4/4 px rectangle at the centre
                    foods.add(food);                 // (32-4)/2
                }                     // skipping the O bcz we have to leave ot so conserve the time
                else if(tileMapchar == 'C'){
                    Block cherry = new Block(cherry_image,x,y,tile_size,tile_size);
                    cherrys.add(cherry);
                }
            }

        }
    }

    public void paintComponent(Graphics g){                             //super invokes the function of same name from the Jpanel(parent class)
        super.paintComponent(g);                               //study it
        draw(g);
    }                               // study g
    public void draw(Graphics g){
        g.drawImage(pacMan.image,pacMan.x,pacMan.y,pacMan.width,pacMan.height,null);   //to draw the image we are using PaintComponent of out Jpanel Class
        for (Block ghost:ghosts){
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);   //g.drawImage to draw

        }
        for(Block wall:walls){
            g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);
        }
        for(Block cherry:cherrys){
            g.drawImage(cherry.image,cherry.x,cherry.y,cherry.width,cherry.height,null);
        }
        g.setColor(Color.white);                                                   //For color object always use Color.name
        for(Block food:foods){
            g.fillRect(food.x,food.y,food.width,food.height);
        }
        g.setFont(new Font("Times New Roman",Font.PLAIN,18));  // search font obj creation and .PLAIN
        if(game_over){if(!win){
            g.drawString("Game Over: "+String.valueOf(score)+"            Press Space Bar to Again Start",tile_size/2,tile_size/2);}
            else{
            g.drawString("You Won with : "+String.valueOf(score)+" Scores            Press Space Bar to Again Start",tile_size/2,tile_size/2);}

        }
        else{
            g.drawString("No. of Lifes Left: "+String.valueOf(lives)+ " Scores: "+String.valueOf(score),tile_size/2,tile_size/2);
    }}
    public void move(){
        pacMan.x +=pacMan.velocityx;
        pacMan.y+=pacMan.velocityy;

        // check pacman -- wall collision
        for(Block wall:walls){
            if(collision(pacMan,wall)){
                pacMan.x-=pacMan.velocityx;
                pacMan.y-=pacMan.velocityy;
                break;
            }
        }

        //move ghost + check( pacman -- ghost) collision
        for (Block ghost:ghosts){
                if (collision(ghost,pacMan)){
                    lives-=1;
                    if (lives == 0){
                        game_over = true;
                        return;       //quickely gets out of the move function
                    }
                    reset_positions();
                }
                ghost.x += ghost.velocityx;
                ghost.y += ghost.velocityy;
                for(Block wall:walls){
                    if (collision(ghost,wall)){
                        ghost.x -= ghost.velocityx;
                        ghost.y -= ghost.velocityy;
                        char new_direction = directions[random.nextInt(4)];
                        ghost.update_direction(new_direction);
                    }
                }
                if (foods.isEmpty() && cherrys.isEmpty()){
                    game_over = true;
                    return;
                }
        }
        //check food collision
        Block food_eaten =null;
        for(Block food: foods){
            if (collision(pacMan,food)){
                food_eaten = food;
                score +=10;
                break;
            }
        }foods.remove(food_eaten);
        Block cherry_eaten = null;
        for(Block cherry: cherrys){
            if(collision(pacMan,cherry)){
                cherry_eaten = cherry;
                score +=50;
                break;
            }
        }cherrys.remove(cherry_eaten);



    }
    public boolean collision(Block a, Block b){                //pacman-food,pacman-wall,pacman-ghosts one combined function
        // Will use a specific formula for detecting th rectangle intersection
        return  a.x <b.x +b.width &&
                a.x +a.width > b.x &&                                // very complex found using regressive use of AI
                a.y<b.y +b.height &&
                a.y +a.height >b.y;
    }
    public void reset_positions(){
        pacMan.reset();
        pacMan.velocityx =0;
        pacMan.velocityy =0;
        for(Block ghost:ghosts){
            ghost.reset();
            char new_direction = directions[random.nextInt(4)];
            ghost.update_direction(new_direction);
        }

    }
}
//       Game loop to instantly draw the updated mao after each move