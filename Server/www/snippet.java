text.addTraverseListener(new TraverseListener() {
	public void keyTraversed(TraverseEvent event) {
		if(event.detail == SWT.TRAVERSE_RETURN) {                     
			go(text.getText());
		}
	}
 });
