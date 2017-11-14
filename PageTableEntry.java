public class PageTableEntry {
	private boolean referenced;	// Denotes whether a page has been referenced recently
	private boolean dirty;		// Denotes whether a page has been modified
	private boolean valid;		// Denotes whether a page currently lives in RAM
	private int frameNumber;	

	public PageTableEntry(boolean r, boolean d, boolean v, int f) {
		referenced = r;
		dirty = d;
		valid = v;
		frameNumber = f;
	}

	public boolean isRef(){
		return referenced;
	}

	public boolean isDirty(){
		return dirty;
	}

	public boolean isValid(){
		return valid;
	}

	public int getFrameNum() {
		return frameNumber;
	}

	public void setRef(boolean r) {
		referenced = r;
	}

	public void setDirty(boolean d) {
		dirty = d;
	}

	public void setValid(boolean v) {
		valid = v;
	}

	public void setFrameNum(int f) {
		frameNumber = f;
	}
}