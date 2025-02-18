import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    //images
    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;
    Image crashBirdImage;
    //bird initial values
    static int birdX=360/8;//initial bird position
    static int birdY=640/2;
    static int birdWidth=34;
    static int birdHeight=24;
    //for the bird logic
    Bird bird;
    Timer gameLoop;
    int velocityY=0;//for the bird but the bird only moves upwards and downwards
    int gravity=1;//every frame the bird going to slow down with one pixel
    //pipe initial values
    static int pipeX=360;
    static int pipeY=0;
    static int pipeWidth=64;
    static int pipeHeight=512;
    int velocityX=-4;////for the pipes because when the pipes move to the left it simulates that the bird moves to the right
    boolean gameOver=false;
    double score=0;
    Timer placePipestimer;
    ArrayList<Pipe> pipes;
    FlappyBird(){
        setPreferredSize(new Dimension(360,640));
        setFocusable(true);//make sure our class which is the Panel takes our key events
        addKeyListener(this);//make sure to check the 3 functions when we have a ke pressed or key type or key released
        //Load the Image
        backgroundImage=new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybirdbg.png"))).getImage();//get class refers to this class and the resource is the image url
        birdImage=new ImageIcon(Objects.requireNonNull(getClass().getResource("./flappybird.png"))).getImage();
        topPipeImage=new ImageIcon(Objects.requireNonNull(getClass().getResource("./toppipe.png"))).getImage();
        bottomPipeImage=new ImageIcon(Objects.requireNonNull(getClass().getResource("./bottompipe.png"))).getImage();
        bird=new Bird(birdImage);
        pipes=new ArrayList<>();
      //lambda function  to handle the required action performed
      placePipestimer=new Timer(1500, e -> {
          placePipes();//the action
      });//every 1.5 seconds we call the performed action
      placePipestimer.start();
        gameLoop=new Timer(1000/60,this);//this refers to this class, and we want to draw 60 frame per second
        gameLoop.start();

}
    //to display the image onto the frame
    //when we draw we start from the position(0,0)
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g){
        //background
        g.drawImage(backgroundImage,0,0,360,640,null);
        //bird
        g.drawImage(bird.image,bird.x,bird.y,birdWidth,bird.height,null);
        //pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over :"+ (int) score,10,35);
        }else{
            g.drawString(String.valueOf((int) score),10,35);
        }
    }
    private void move(){
      velocityY+=gravity;
      bird.y+=velocityY;
      bird.y=Math.max(bird.y,0);//to prevent the bird to escape from the screen
        for (Pipe pipe : pipes) {
            pipe.x += velocityX;
            if(!pipe.passed&&bird.x>pipe.x+pipe.width){
                pipe.passed=true;
                score+=0.5;//this is why we make it double because we pass 2 pipes not one so the score will be incremented by total of one
            }
            if(crash(bird,pipe)){
                gameOver=true;
            }
        }//moving the pipes
        if(bird.y>640){
            gameOver=true;
        }
    }
    public void placePipes(){
      int RandomPipeY=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2));
      int openingSpace=640/4;
      Pipe topPipe=new Pipe(topPipeImage);
      topPipe.y=RandomPipeY;
      pipes.add(topPipe);
      Pipe bottomPipe=new Pipe(bottomPipeImage);
      bottomPipe.y=topPipe.y+pipeHeight+openingSpace;//making the space between the top and bottom pipe the opening space
      pipes.add(bottomPipe);

    }
    @Override
    //this action will be performed every second 60 times
    public void actionPerformed(ActionEvent e) {
      move();
      repaint();//this is the paint component
        if(gameOver){
            placePipestimer.stop();
            gameLoop.stop();
        }
    }
    public boolean crash(Bird bird,Pipe pipe){
      return bird.x<pipe.x+pipe.width&&//bird top left corner does not reach the pipe top right corner
              bird.x+bird.width>pipe.x&&//bird top right corner does not reach the pipe top left corner
              bird.y<pipe.y+pipe.height&&//bird bottom left corner does not reach the pipe bottom right corner
              bird.y+bird.height>pipe.y;//bird bottom right corner does not reach the pipe bottom left corner
    }
    @Override
    public void keyTyped(KeyEvent e) {
        //not be used
    }
    @Override
    //when we press space the bird will jump
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY =-9;
            //restart the game by resetting the values
            if(gameOver){
                bird.y=birdY;
                velocityY=0;
                pipes.clear();
                score=0;
                gameOver=false;
                gameLoop.start();
                placePipestimer.start();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
      //not be used
    }
}
