package at.mount.matize.jsf;

import javax.faces.component.UIComponent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.NamedEvent;

@NamedEvent(shortName="postInvokeAction")
public class PostInvokeActionEvent extends ComponentSystemEvent {

	private static final long serialVersionUID = 326218508537691006L;

	public PostInvokeActionEvent(UIComponent component) {
		super(component);
	}

}
