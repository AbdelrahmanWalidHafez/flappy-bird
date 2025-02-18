import java.awt.*;
public class Pipe {
    int x=FlappyBird.pipeX;
    int y=FlappyBird.pipeY;
    int width=FlappyBird.pipeWidth;
    int height=FlappyBird.pipeHeight;
    boolean passed=false;
    Image image;
    Pipe(Image image){
        this.image=image;
    }
}
