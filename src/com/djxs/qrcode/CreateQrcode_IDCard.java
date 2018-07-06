package com.djxs.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.swetake.util.Qrcode;

public class CreateQrcode_IDCard {
	public static void main(String[] args) throws IOException {
		//int ver=5;//假设版本号为5
		
		Qrcode qrcode=new Qrcode();
		//设置二维码内容，N：数字、A:字母、B：任意
		qrcode.setQrcodeEncodeMode('B');
		//设置二维码的排错率，可选L（7%）、M（15%默认）、Q（25%）、H（30%），排错率越高，可存储的信息越少，但对二维码清晰度的要求越小
		qrcode.setQrcodeErrorCorrect('Q');
		//qrcode.setQrcodeVersion(ver);//设置版本号
	    
		String str="BEGIN:VCARD\n"+
                   "PHOTO;VALUE=uir:http://game.gtimg.cn/upload/adw/image/20180703/c61b6c5bb59d60dc196f757b08c60c98.jpg\n" +
                   "FN:姓名:吴姗姗\n"+
                   "TITLE:河北科技师范学院\n"+
                   "ADR;WORK:;;秦皇岛海港区河北大街西段360号\n"+
                   "TEL;CELL,VOICE:12345678\n"
                   "TEL;WORK,VOICE:123456789\r\n"+
                   "URL;WORK:http://www.4399.com\n"+
                   "EMAIL;INTERNET,HOME:11111111@qq.com\n"+
                   "END:VCARD";//二维码扫出来的内容
        //得到一个二维数组
        boolean[][] calQrcode=qrcode.calQrcode(str.getBytes("UTF-8"));
        /*二维码大小，国家标准：
		 * ver = 1 , imagesize=21
		 * ver = 2 , imagesize=25
		 * ver = 3 , imagesize=29
		 * ver = n , imagesize=21+(ver-1)*4
		 * 当每一位用m位像素点表示时（默认一位）：
		 * imagesize=(21+(ver-1)*4)*m
		 * 当四边加x个像素的白边时，（默认不加）：
		 *imagesize=((21+x*2)+(ver-1)*4)*m
		 */
		int x=2;//假设加两个像素的白边
		int imagesize=67+12*(qrcode.getQrcodeVersion()-1);//二维码大小
		
		BufferedImage bufferedimage=new BufferedImage(imagesize,imagesize,BufferedImage.TYPE_INT_RGB);//设置二维码大小
		Graphics2D gs1=bufferedimage.createGraphics();
		gs1.setBackground(Color.WHITE);
		gs1.setColor(Color.BLACK);
		gs1.clearRect(0,0,imagesize,imagesize);//与上面的bufferedimage的大小一样，不然会有黑边
		
        int startR=21;
        int startG=151;
        int startB=197;
        
        int endR=124;
        int endG=80;
        int endB=138;
        
        for (int i=0;i<calQrcode.length;i++){
        	for (int j=0;j<calQrcode.length;j++){
        		if(calQrcode[i][j]){
        		   /*x=开始值+（结束值-开始值）*（）/长度
        			*                   j+1       （上下       渐变）
        			*                   i+1       （左右       渐变）
        			*                  (i+j)/2（正对角线渐变）
        			*/ 
        		
        		   int num1=startR+(endR-startR)*((i+j)/2)/calQrcode.length;
        		   int num2=startG+(endG-startG)*((i+j)/2)/calQrcode.length;
        		   int num3=startB+(endB-startB)*((i+j)/2)/calQrcode.length;
        		
        		   Color color1= new Color(num1,num2,num3);
        		   gs1.setColor(color1);
        		   gs1.fillRect(i*3+x,j*3+x,3,3);//(i*m+x,j*m+x,m,m)
        		  
        		}
        	}
        }
        Image logo=ImageIO.read(new File("D:/djxsQrcode/idcard.jpg"));
        int logosize=50;
        int logox=(imagesize-logosize)/2;
        gs1.drawImage(logo,logox, logox, logosize, logosize, null);
        gs1.dispose();
    	bufferedimage.flush();
    	ImageIO.write(bufferedimage,"png",new File("D:/djxsQrcode/idqrcode.jpg"));
	}
}
