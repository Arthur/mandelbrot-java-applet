
import java.awt.*;
import java.applet.*;


public class Fractal extends Applet implements Runnable{
	
	Thread runner;
	FractalControls	controls;
	
	Panel paramP;
	Image offimg;
	Graphics offg;
	
	boolean julia;
	int Xscreen,Yscreen,xRange, yRange,r,g,b;
	double Left,Right,Bottom,Top;
	double x, y, deltaXPerPixel,deltaYPerPixel;
	double cReal,cImaginary;
	int maximalIteration = 200;
	//int Bound = 100;
		
	public void update(Graphics g){
		paint(g);
	}
	
	public void paint(Graphics g) {
		g.drawImage(offimg,150,0,this);		
	}

	
	
	public void start()
	{
	if (runner==null)
		{
		runner=new Thread(this);
		runner.setPriority(2);
		runner.start();
		}
	}//start

	public void stop(){
		if (runner!=null){
			runner.stop();
			runner=null;
		}
	}//stop

	public void restart(){
		stop();
		start();
	}//restart
			
			
	public void init(){
		
		Left = -2;
		Right=2;
		Bottom=-1;
		Top=1;
		cReal=0.7454054;
		cImaginary=0.1130063;
		
		setLayout(new BorderLayout());
		controls= new FractalControls(this);
		add("West",controls);
		
		julia=true;
		newColor();
		Xscreen = 300;
		Yscreen = 200;
		
	}
	
	
	
	public void run() {			
		
		showStatus("I am mapping...");
		colormapping(julia);
		repaint();
		showStatus("finished");
		
	}//run()
	
	public void newColor(){
		r=(int)(Math.random()*255);
		g=(int)(Math.random()*255);
		b=(int)(Math.random()*255);
	}
	
	public void colormapping(boolean frac){
		int depth;
		
		offimg= createImage(Xscreen,Yscreen);
		offg=offimg.getGraphics();
		offg.setColor(Color.white);
		offg.fillRect(0,0,Xscreen,Yscreen);
			
		deltaXPerPixel = (Right - Left) / Xscreen;
		deltaYPerPixel = (Bottom - Top) / Yscreen;
		
		y = Top;
		
		if(julia){
			for (yRange = 0; yRange <= Yscreen; yRange++) {
				x = Left;
				for (xRange = 0; xRange <= 	Xscreen; xRange++) {
					depth=JuliaComputeAndTest(x,y,200);
					offg.setColor(new Color((depth+r)%256,(depth+g)%256,(depth+b)%256));
					offg.drawLine(xRange,yRange,xRange,yRange);
					x = x + deltaXPerPixel;
				}
				repaint();
				y = y + deltaYPerPixel;
			}
		}else{
			for (yRange = 0; yRange <= Yscreen; yRange++) {
				x = Left;
				for (xRange = 0; xRange <= 	Xscreen; xRange++) {
					depth=MandelComputeAndTest(x,y,200);
					offg.setColor(new Color((depth+r)%256,(depth+g)%256,(depth+b)%256));
					offg.drawLine(xRange,yRange,xRange,yRange);
					x = x + deltaXPerPixel;
				}
				repaint();
				y = y + deltaYPerPixel;
			}
		}		
	}//colormapping()
	
	

			
	int JuliaComputeAndTest(double x, double y,int maximalIteration) {
		
				
		int iterationNo;
		double xSq, ySq, distanceSq;
		boolean finished;
		
		finished = false;
		iterationNo = 0;
		xSq = x * x;
		ySq = y * y;
		distanceSq = xSq + ySq;
		
		while (iterationNo != maximalIteration && !finished)  {
			iterationNo++;
			y = x * y;
			y = y + y - cImaginary;
			x = xSq - ySq - cReal;
			xSq = x * x;
			ySq = y * y;
			distanceSq = xSq + ySq;
			
			finished = (distanceSq > 100.0);
		}
		
		return iterationNo;
	}//JuliaComputeAndTest()
	
	int MandelComputeAndTest(double cReal, double cImaginary,int maximalIteration) {
				
		int iterationNo;
		double x,y;
		double xSq, ySq, distanceSq;
		boolean finished;
		
		finished = false;
		iterationNo = 0;
		x=0.0;
		y=0.0;
		xSq = x * x;
		ySq = y * y;
		distanceSq = xSq + ySq;
		
		while (iterationNo != maximalIteration && !finished)  {
			iterationNo++;
			y = x * y;
			y = y + y - cImaginary;
			x = xSq - ySq - cReal;
			xSq = x * x;
			ySq = y * y;
			distanceSq = xSq + ySq;
			
			finished = (distanceSq > 100.0);
		}
		return iterationNo;
	}//MandelComputeAndTest()
	
	public boolean mouseDown(Event mEvt, int i,int j){
		i-=150;
		controls.firstCorner(i*(Right-Left)/Xscreen+Left,j*(Bottom-Top)/Yscreen+Top);
		return true;
	}
	public boolean mouseDrag(Event e, int i, int j) {
		i-=150;
		controls.secondCorner(i*(Right-Left)/Xscreen+Left,j*(Bottom-Top)/Yscreen+Top);
		return true;
	}
	
}