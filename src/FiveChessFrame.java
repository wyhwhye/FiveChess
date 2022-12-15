import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class FiveChessFrame extends JFrame implements MouseListener {

    BufferedImage bgimg = null;  // 背景图片
    int x = -1;  // x、y坐标
    int y = -1;
    int[][] allChess = new int [19][19];  // 所以棋子，0无棋，1黑棋，2白棋
    boolean isBlack = true;  // 判断现在是黑棋还是白棋下

    public FiveChessFrame() throws IOException {
        this.setSize(1200, 820);  // 窗口大小
        this.setTitle("五子棋");  // 标题
        this.setLocationRelativeTo(null);  // 屏幕居中显示
        this.setResizable(false);  // 不可改变窗口大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置关闭功能
        this.setVisible(true);  // 窗口可见

        this.addMouseListener(this);  // 设置鼠标监听

        // 读取背景图片
//        try {
//            bgimg = ImageIO.read(new File("img/bg2.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        ImageIcon img = new ImageIcon("img/bg2.png");
//        //要设置的背景图片
//        JLabel imgLabel = new JLabel(img);
//        //将背景图放在标签里。
//        this.getLayeredPane().add(imgLabel);
//        //将背景标签添加到jfram的LayeredPane面板里。
//        imgLabel.setBounds(0, 0,1200,820);
//        // 设置背景标签的位置


    }

    public void paint(Graphics g2){
        // 双缓冲，防止屏幕闪烁
        BufferedImage bi = new BufferedImage(1200,820,BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();

        // 背景图片
//        g.drawImage(bgimg,0,20,this);

        // 绘制棋盘
        for (int i = 0; i < 19; i++) {
            g.setColor(Color.black);
            g.drawLine(40,60+i*40,760,60+i*40);
            g.drawLine(40+i*40,60,40+i*40,780);
        }

        // 绘制小黑点
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g.setColor(Color.black);
                g.fillOval(160+i*240-3,180+j*240-3,6,6);
            }
        }

        // 绘制棋子
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (allChess[i][j]==1){
                    // 画黑子
                    int tmpx = 40+i*40;
                    int tmpy = 60+j*40;
                    g.setColor(Color.black);
                    g.fillOval(tmpx-15,tmpy-15,30,30);

                } else if (allChess[i][j]==2){
                    // 画白子
                    int tmpx = 40+i*40;
                    int tmpy = 60+j*40;
                    g.setColor(Color.white);
                    g.fillOval(tmpx-15,tmpy-15,30,30);

                }
            }
        }
        g2.drawImage(bi,0,0,this);
    }



    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override  // 鼠标点击操作
    public void mousePressed(MouseEvent e){
        // 打印坐标
        System.out.println("X->"+e.getX());
        System.out.println("Y->"+e.getY());
        System.out.println();
        x = e.getX();  // 获取点击位置坐标
        y = e.getY();
        float fx = x;
        float fy = y;
        if (x>=30 && x<= 770 && y>=50 && y<=790){
            x = Math.round((fx-40)/40);  // 算出离他最近的交叉点
            y = Math.round((fy-60)/40);
            if (isBlack == true && allChess[x][y]==0) {
                allChess[x][y] = 1;  // 下黑子
                isBlack = false;
            } else if (isBlack == false && allChess[x][y]==0){
                allChess[x][y] = 2;  // 下白子
                isBlack = true;
            }
            this.repaint();  // 画
        }
    }
}

