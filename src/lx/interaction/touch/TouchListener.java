package lx.interaction.touch;

public interface TouchListener
{
	public void pointerPressed(int x, int y);
	
	public void pointerReleased(int x, int y);

	public void pointerDragged(int x, int y);	

}