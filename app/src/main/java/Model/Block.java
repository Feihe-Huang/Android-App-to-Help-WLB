package Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.example.project.R;

public class Block extends View{
    private double x;
    private double y;
    private double velocityY;
    private int lineToDraw;
    private boolean scored;

    public Block(Context context, double x, double y, double velocityY, int lineToDraw) {
        super(context);
        this.x = x;
        this.y = y;
        this.velocityY = velocityY;
        // 设定在四个区域的哪个部分出现block
        this.lineToDraw = lineToDraw;
        // 设置屏幕大小
    }


    // 更新block的位置，让方块不断下落
    public void update(int ms) {
        y = y + velocityY * ms/1000.0;
    }

    // 绘制block
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // 设置block以及其被点击前后的颜色
        Paint keyButton = new Paint();
        int blueNotScored = getResources().getColor(R.color.white);
        int blueScored = getResources().getColor(R.color.black);
        // 判断block是否被点进与未被点击时就达到底部，并切换其颜色
        if (isScored()==true) {
            keyButton.setColor(blueNotScored);
        } else {
            keyButton.setColor(blueScored);
        }

        keyButton.setStyle(Paint.Style.FILL_AND_STROKE); // 描边以及填充
        keyButton.setAntiAlias(true); //  Anti-Aliasing
        int width = 300; // block的宽以及列宽 1200/4
        int height = 384; // block的高 1920/5
        // 绘制block
        canvas.drawRect((int)getX()+(width*lineToDraw), (int)getY(), width*lineToDraw+width+getX(), height+getY(), keyButton);
    }

    // 得到block与顶部之间的距离
    public float getY() {
        return (float) y;
    }

    // 得到block与最左侧之间的距离
    public float getX() {
        return (float) x;
    }

    // 判断是否被点击与未被点击时就达到底部
    public boolean isScored() {
        return scored;
    }

    // 判断block是否被点击与未被点击时就达到底部
    public void setScored(boolean scored) {
        this.scored = scored;
    }

    // 设定在四个区域的哪个部分出现block
    public int getLineToDraw() {
        return lineToDraw;
    }

}
