package at.mse.walchhofer.utilities.timing;

public class StopWatch {

	private long start = Long.MIN_VALUE;
	
	private long stop = Long.MIN_VALUE;
	
	private boolean started = false;
	private boolean stopped = false;
	
	public static StopWatch getInstance() {
		return new StopWatch();
	}
	
	private StopWatch() {
	}
	
	public void start() {
		if(started) {
			throw new RuntimeException("Timer already started! Stop first!");
		}
		this.started = true;
		this.stopped = false;
		start = System.nanoTime();
	}
	
	public void stop() {
		this.stop = System.nanoTime();
		this.stopped = true;
	}
	
	public long getElapsedTime() {
		if(stopped)
			return stop-start;
		else {
			long now = System.nanoTime();
			return now-start;
		} 
	}
}
