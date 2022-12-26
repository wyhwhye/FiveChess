import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;


public class FiveChessFrame extends JFrame implements MouseListener {
    private JPanel pan = new JPanel();  // 面板
    private JButton undo = new JButton("悔棋");  // 悔棋
    private JButton concede = new JButton("认输");  // 认输
    private JButton reopen = new JButton("重新开始");  // 重新开始
    private JButton quit = new JButton("退出游戏");  // 退出游戏
    BufferedImage bgimg = null;  // 背景图片
    BufferedImage bgimgWhite = null;  // 白背景

    int x = -1;  // x、y坐标
    int y = -1;
    Stack <Integer> allx = new Stack<Integer>(); // 所有棋子的x、y坐标
    Stack <Integer> ally = new Stack<Integer>();
    int chessCount = 0;  // 棋子数量
    // 19*19棋盘，多两行作为判断， 故后续代码中所有allChess都+1
    int[][] allChess = new int [21][21];  // 所以棋子，0无棋，1黑棋，2白棋
    boolean isBlack = true;  // 判断现在是黑棋还是白棋下
    boolean finish = false;  // 判断游戏是否结束
    String win = "";  // 哪方胜利


    public FiveChessFrame() throws IOException {
        pan.setLayout(null);  // 自由布局
        // 按钮字体
        Font buttonFont = new Font("黑体",Font.PLAIN,20);
        undo.setFont(buttonFont);
        concede.setFont(buttonFont);
        reopen.setFont(buttonFont);
        quit.setFont(buttonFont);
        // 设置各个按钮位置与大小
        undo.setBounds(888,351,140,70);
        concede.setBounds(888,451,140,70);
        reopen.setBounds(888,550,140,70);
        quit.setBounds(888,651,140,70);
        // 设置按钮事件
        undo.addActionListener(new ButtonPress());
        concede.addActionListener(new ButtonPress());
        reopen.addActionListener(new ButtonPress());
        quit.addActionListener(new ButtonPress());
        // 按钮加入panel
        pan.add(undo);
        pan.add(concede);
        pan.add(reopen);
        pan.add(quit);
        pan.setVisible(true);

        this.add(pan);
        this.setSize(1200, 820);  // 窗口大小
        this.setTitle("五子棋");  // 标题
        this.setLocationRelativeTo(null);  // 屏幕居中显示
        this.setResizable(false);  // 不可改变窗口大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置关闭功能
        this.setVisible(true);  // 窗口可见
        this.addMouseListener(this);  // 设置鼠标监听

        // 读取背景图片
        try {
            bgimg = ImageIO.read(new File("img/bg2.png"));
            bgimgWhite = ImageIO.read(new File("img/bgWhite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.repaint();
    }


    // 画
    public void paint(Graphics g2){
        // 双缓冲，防止屏幕闪烁
        BufferedImage bi = new BufferedImage(1200,820,BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();

        // 画背景图片
        g.drawImage(bgimgWhite,0,20,this);
        g.drawImage(bgimg,0,20,this);

        // 使按钮显示
//        undo.requestFocus();
//        concede.requestFocus();
//        reopen.requestFocus();
//        quit.requestFocus();
        undo.repaint();
        concede.repaint();
        reopen.repaint();
        quit.repaint();

        // 打印提示信息
        g.setColor(Color.black);
        g.setFont(new Font("宋体",Font.PLAIN,50));
        if (finish == true){
            g.drawString(win+" 胜！",820,300);
        }else if (isBlack == true){
            g.drawString("轮 到 黑 方",850,300);
        } else {
            g.drawString("轮 到 白 方",850,300);
        }

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
                if (allChess[i+1][j+1]==1){
                    // 画黑子
                    int tmpx = 40+i*40;
                    int tmpy = 60+j*40;
                    g.setColor(Color.black);
                    g.fillOval(tmpx-15,tmpy-15,30,30);

                } else if (allChess[i+1][j+1]==2){
                    // 画白子
                    int tmpx = 40+i*40;
                    int tmpy = 60+j*40;
                    g.setColor(Color.white);
                    g.fillOval(tmpx-15,tmpy-15,30,30);
                }
            }
        }

        // 绘制现在下的棋子的提示
        if (!allx.isEmpty()){  // 如果棋盘上有棋子的话
            int i = allx.peek();  // 取出最新下的棋的坐标
            int j = ally.peek();
            if (allChess[i+1][j+1]==1){
                // 是黑子，画白点
                int tmpx = 40+i*40;
                int tmpy = 60+j*40;
                g.setColor(Color.white);
                g.fillOval(tmpx-5,tmpy-5,10,10);

            } else if (allChess[i+1][j+1]==2){
                // 是白子，画黑点
                int tmpx = 40+i*40;
                int tmpy = 60+j*40;
                g.setColor(Color.black);
                g.fillOval(tmpx-5,tmpy-5,10,10);
            }
        }

        g2.drawImage(bi,0,0,this);
    }


    // 按钮事件
    public class ButtonPress implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object obj = e.getSource();  // 按的哪个按钮
            if(obj == undo) {
                Undo();
            } else if(obj == concede) {
                Concede();
            } else if(obj == reopen) {
                Reopen();
            } else if (obj == quit){
                System.exit(0);
            }
        }
    }

    public void Undo() {
        // 如果没有棋子 或 游戏已经结束
        if (chessCount==0 || finish == true){
            return;
        }
        // 悔棋
        int tmpx = allx.pop();
        int tmpy = ally.pop();
        chessCount--;
        allChess[tmpx+1][tmpy+1]=0;
        repaint();
        System.out.println("悔棋");
    }

    public void Concede() {
        if (finish == true){
            return;
        }
        // 确认提示
        int res = JOptionPane.showConfirmDialog(this,"确定认输吗？");
        if (res==0){
            finish = true;
            win = (isBlack==true ? "白 白 白" : "黑 黑 黑");
            this.repaint();
            System.out.println("认输");
            JOptionPane.showMessageDialog(this,"认输，" + win + "方获胜");

        }
    }

    public void Reopen() {
        // 确认提示
        int res = JOptionPane.showConfirmDialog(this,"确定重新开始吗？");
        if (res == 0) {
            // 初始化allChess
            for (int i = 0; i < 21; i++) {
                for (int j = 0; j < 21; j++) {
                    allChess[i][j] = 0;
                }
            }
            // 清空allx、ally
            allx.clear();
            ally.clear();
            // 初始化chessCount、x、y坐标、判断
            chessCount = 0;
            x = -1;
            y = -1;
            isBlack = true;
            finish = false;
            repaint();
            System.out.println("重新开始");
        }
    }


    // 判断是否有五子连珠
    private boolean checkWin(){
        boolean flag = false;
        // 横向
        int cnt1 = 1;  // 计数连子个数
        int i1 = 1;
        // 下的子 右边
        while (allChess[x+1][y+1] == allChess[x+i1+1][y+1]){
            i1++;
            cnt1++;
        }
        i1=1;
        // 下的子 左边
        while (allChess[x+1][y+1] == allChess[x-i1+1][y+1]){
            i1++;
            cnt1++;
        }

        // 纵向
        int cnt2 = 1;
        int i2 = 1;
        // 下的子 下边
        while (allChess[x+1][y+1] == allChess[x+1][y+i2+1]){
            i2++;
            cnt2++;
        }
        i2=1;
        // 下的子 上边
        while (allChess[x+1][y+1] == allChess[x+1][y-i2+1]){
            i2++;
            cnt2++;
        }

        // 一三斜向
        int cnt3 = 1;
        int i3 = 1;
        // 下的子 右上边
        while (allChess[x+1][y+1] == allChess[x-i3+1][y+i3+1]){
            i3++;
            cnt3++;
        }
        i3=1;
        // 下的子 左下边
        while (allChess[x+1][y+1] == allChess[x+i3+1][y-i3+1]){
            i3++;
            cnt3++;
        }

        //二四斜向
        int cnt4 = 1;
        int i4 = 1;
        // 下的子 右下边
        while (allChess[x+1][y+1] == allChess[x+i4+1][y+i4+1]){
            i4++;
            cnt4++;
        }
        i4=1;
        // 下的子 左上边
        while (allChess[x+1][y+1] == allChess[x-i4+1][y-i4+1]){
            i4++;
            cnt4++;
        }

        if (cnt1>=5 || cnt2>=5 || cnt3>=5 || cnt4>=5){
            flag = true;
        }
        return flag;
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
//        System.out.println("X->"+e.getX());
//        System.out.println("Y->"+e.getY());
//        System.out.println();
        // 游戏未结束
        if (finish == false){
            // 获取点击位置坐标
            x = e.getX();
            y = e.getY();
            float fx = x;
            float fy = y;
            if (x>=30 && x<= 770 && y>=50 && y<=790){
                // 算出离他最近的交叉点
                x = Math.round((fx-40)/40);
                y = Math.round((fy-60)/40);
                if (allChess[x+1][y+1]==0){  // 可以下子
                    allx.push(x);  // 棋子坐标入栈
                    ally.push(y);
                    chessCount++;  // 棋子总数+1
                    if (isBlack == true ) {
                        allChess[x+1][y+1] = 1;  // 下黑子
                        isBlack = false;
                    } else if (isBlack == false ){
                        allChess[x+1][y+1] = 2;  // 下白子
                        isBlack = true;
                    }
                }
                if (chessCount == 361){
                    finish = true;
                    JOptionPane.showMessageDialog(this,"棋盘已下满，游戏结束！");
                }
                this.repaint();  // 画

                // 判断是否胜利
                boolean isWin = checkWin();
                if (isWin == true){
                    finish = true;
                    win = (allChess[x+1][y+1]==1 ? "黑 黑 黑" : "白 白 白");
                    JOptionPane.showMessageDialog(this,"游戏结束，" + win + "方获胜");
                }
            }
        }

    }

}