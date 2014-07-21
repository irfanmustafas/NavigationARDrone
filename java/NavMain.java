import com.leapmotion.leap.*;
import de.yadrone.base.ARDrone;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.command.LEDAnimation;

import javax.swing.JFrame;

public class NavMain {

	public static void main(String[] args)
	{
	    IARDrone drone = null;
	    JFrame window = new JFrame("Navigation");
	    window.setBounds(50, 100, 300, 300);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    

	    try
	    {
	        drone = new ARDrone();
	        drone.start();
	        boolean isRunning = true;
	        int speed = 20;
	        Controller controller = new Controller();
	        
	        CommandManager cmd = drone.getCommandManager();
	        cmd.setLedsAnimation(LEDAnimation.BLINK_ORANGE, 3, 10);
	        cmd.takeOff();
	        cmd.waitFor(5000);
	        
	        while(isRunning)
	        {
	        	Frame frame = controller.frame();
	        	
	        	if (!frame.hands().isEmpty()) 
	        	{
	        	    Hand hand = frame.hands().get(0);
	        	    
	        	    Vector normal = hand.palmNormal();
	        	    Vector direction = hand.direction();
	        	    
	        	    System.out.println("roll: " + Math.toDegrees(normal.roll()) + " degrees, "
	                        + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees\n");
	        	    
	        	    if (hand.grabStrength() >= 0.5)
	        	    {
	        	    	cmd.landing();
	        	    	System.out.println("Landing");
	        	    	isRunning = false;
	        	    }
	        	    
	        	    
	        	    else if(Math.toDegrees(direction.yaw()) > 10)
	        	    {
	        	    	cmd.spinRight(speed).doFor(1000);
	        	    	System.out.println("Turn right");
	        	    }
	        	    
	        	    else if(Math.toDegrees(direction.yaw())< -10)
	        	    {
	        	    	cmd.spinLeft(speed).doFor(1000);
	        	    	System.out.println("Turn left");
	        	    }
	        	    
	        	    else if(Math.toDegrees(normal.roll()) > 10)
	        	    {
	        	    	cmd.goRight(speed).doFor(1000);
	        	    	System.out.println("Move right");
	        	    }
	        	    
	        	    else if(Math.toDegrees(normal.roll()) < -10)
	        	    {
	        	    	cmd.goLeft(speed).doFor(1000);
	        	    	System.out.println("Move left");
	        	    }
	        	    
	        	    else
	        	    {
	        	    	cmd.hover();
	        	    }
	        	    
	        	    
	        	}
	        	
	        	
	        	
	        	
	        
	    }
	        System.out.println("Shutting down");
	    }
	    
	    catch (Exception exc)
		{
			exc.printStackTrace();
		}
		finally
		{
			if (drone != null)
				drone.stop();
			System.exit(0);
		}
	}
}
