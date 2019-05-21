package gui;


import data_analysis.Student_information;
import javafx.concurrent.Task;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mlx
 */
public class WorldCloud extends Task<Void> {

    @Override
    protected Void call() throws Exception {
        wordcloud();
        return null;
    }

    double getProgress1() {
        return progress1;
    }

    private ArrayList<Student_information> student_information;
    private BufferedImage image;
    private double progress1 = 0;
    private static int width = 800;
    private static int height = 500;
    private static int r = 250;
    private static int maxsize = 82;
    private static int minsize = 12;
    boolean finished = false;

    boolean isFinished() {
        return finished;
    }

    BufferedImage getImage() {
        return image;
    }


    WorldCloud(ArrayList<Student_information> student_information) {
        this.student_information = student_information;
    }

    private static int getWidth() {
        return width;
    }

    private static int getHeight() {
        return height;
    }

    /**
     *
     * @param x 起始点的坐标
     * @param y  起始点的坐标
     * @param width  单个词的宽度
     * @param height  单个词的宽度
     * @return
     */
    static private boolean inbounds(int x, int y, int width, int height) {
        int a = x;
        int b = y;
        int centerx = getWidth() / 2;
        int centery = getHeight() / 2;
        x = x - centerx;
        y = y - centery;
        return in_circle(x, y) && in_circle(x + width, y + height) && in_rectangle(a, b, width, height);
    }

    private static boolean in_circle(int x, int y) {
        return Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2);
    }

    private static boolean in_rectangle(int x, int y, int width, int height) {
        return x > 2 && y > 2 && x + width < getWidth() - 2 && y + height < getHeight() - 2;
    }

    private void wordcloud() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Student_information p : this.student_information) {
            for (String s : p.getWordcloud()
            ) {
                if (!(s.equals("未知") || s.equals("其他") || s.equals("")))
                    map.put(s, map.getOrDefault(s, 0) + 1);
            }
        }
        map = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        int max = map.values().iterator().next();
        //System.out.println(map);
        Random random = new Random();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(-1));
        g.fillRect(0, 0, width, height);
        int x;
        int y;
        int time = 0;
        for (Map.Entry<String, Integer> w1 : map.entrySet()
        ) {
            System.out.println(time++ + " " + map.size());
            this.progress1 = (double) time / 150.0;
            if (time > 150) break;
            int t = 0;
            int w = 25;
            int size = (int) (((double) w1.getValue() / max) * (double) (maxsize - minsize) + (double) minsize);
            long currentTimeMillis = System.currentTimeMillis();
            int sub_picture[][] = getRGB(w1.getKey(), size, getcolor());
            while (System.currentTimeMillis() - currentTimeMillis < 10000) {
                t++;
                if (random.nextBoolean()) x = random.nextInt(w) + image.getWidth() / 2 - sub_picture.length / 2;
                else x = -random.nextInt(w) + image.getWidth() / 2 - sub_picture.length / 2;
                if (random.nextBoolean()) y = random.nextInt(w) + image.getHeight() / 2 - sub_picture[0].length / 2;
                else y = -random.nextInt(w) + image.getHeight() / 2 - sub_picture[0].length / 2;
                if (t > 200) {
                    w += 20;
                    t = 0;
                }
                if (w > height / 2)
                    w = 100;
                if (inbounds(x, y, sub_picture.length, sub_picture[0].length)) {
                    boolean candraw = true;
                    A:
                    for (int i = x; i < x + sub_picture.length; i++) {
                        for (int j = y; j < y + sub_picture[0].length; j++) {
                            int now = image.getRGB(i, j);
                            if (sub_picture[i - x][j - y] != -1 && (now != -1 || image.getRGB(i - 1, j - 1) != -1 || image.getRGB(i - 1, j + 1) != -1
                                    || image.getRGB(i, j - 1) != -1 || image.getRGB(i, j + 1) != -1 || image.getRGB(i - 1, j) != -1
                                    || image.getRGB(i + 1, j - 1) != -1 || image.getRGB(i + 1, j - 1) != -1 || image.getRGB(i + 1, j - 1) != -1)
                                    || image.getRGB(i + 2, j) != -1 || image.getRGB(i - 2, j) != -1 || image.getRGB(i, j + 2) != -1
                                    || image.getRGB(i, j - 2) != -1) {
                                candraw = false;
                                break A;
                            }
                        }
                    }
                    if (candraw) {
                        for (int i = x; i < x + sub_picture.length; i++) {
                            for (int j = y; j < y + sub_picture[0].length; j++) {
                                if (sub_picture[i - x][j - y] != -1) {
                                    image.setRGB(i, j, sub_picture[i - x][j - y]);
                                }
                            }
                        }
                        break;
                    }
                }
            }

        }
        finished = true;
        this.image = image;
    }

    /**
     *
     * @return 随机颜色
     */
    private static Color getcolor() {
        int c[] = {0xfda67e, 0x81cacc, 0xcca8ba, 0x88cc81, 0x82a0c5, 0xfddb7e, 0x735ba1, 0xbda29a, 0x6e7074, 0x546570, 0xc4ccd3};
        Random random = new Random();
        int co = random.nextInt(11);
        return new Color(c[co]);
    }

    /**
     *
     * @param word 单个词
     * @param font_size 字体大小
     * @param color 颜色
     * @return rgb信息
     */
    private static int[][] getRGB(String word, int font_size, Color color) {
        Font font = new Font("文泉驿", Font.PLAIN, font_size);
        int hight = (int) (font_size * 1.1) + 1;
        int width = (int) (font_size * word.length()) + 1;
        int rgb[][] = new int[width][hight];
        BufferedImage image = new BufferedImage(width, hight,
                BufferedImage.TYPE_INT_BGR);
        Graphics2D g = image.createGraphics();
        g.setClip(0, 0, width, hight);
        g.setColor(new Color(-1));
        g.fillRect(0, 0, width, hight);
        g.setColor(color);
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//抗锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);//抗锯齿
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.drawString(word, 0, y);
        g.dispose();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < hight; j++) {
                rgb[i][j] = image.getRGB(i, j);
            }
        }
        return rgb;
    }


}