// Christian Jarani
// CS 1550: Intro to Operating Systems
// Project 3: VM Simulator

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;
import java.lang.Math;

public class vmsim {



	 ////////////////////////////
	// System Fields (32-bit) //
   ////////////////////////////	

	private static final int PAGE_SIZE 	  = (int) Math.pow(2,12);		// 2^12 (4KB)
	private static final int ADDRESS_SIZE = (int) Math.pow(2,32);		// 2^32 (all enumerable addresses for a 32-bit system)
	private static final int NUM_PAGES 	  = (int) Math.pow(2,20);		// 2^32 / 2^12 = 2^20 (total # of address divided by page size)

 

	 /////////////////////////////////
	// Page Replacement Algorithms //
   /////////////////////////////////

	// OPT will evict a page based on if/when it's referenced in the future (i.e. from an off-line setting like a tracefile)
	private static int opt(int[] frameTable, RefEntry[] refTable, int numFrames, int numRefs) {
		int evictFrame = -1, evictFutureRef = -1;
		for(int i = 0; i < numFrames; i++) {

		}
		return 0;
	}

	// CLOCK will evict the oldest unreferenced page, using its position in a circular queue as a measure of history
	private static int clock(int[] frameTable, int numFrames) {
		return 0;
	}

	// RANDOM will select a random page to evict
	private static int random(int numFrames) {
		Random rand = new Random();
		return rand.nextInt(numFrames);			// Return a random frame between 1st and last addresses in frame table
	}

	// NRU will continuously check if a page has been referenced/modified during page management, even when evicition is unnecessary.
	// 	 Similar to CLOCK, but age is not represented both implicitly AND explicitly by the position in a queue supplemented with a timestamp.
	//	 Rather, it is only represented explicitly thru use of the dirty bit and referenced bit (which is reset after an amt of time called the refresh period)
	private static int nru(int[] frameTable, int numFrames, int refresh) {
		return 0;
	}



	 //////////////////////
	// Simulation Logic //
   //////////////////////

	private static void simulate(String algorithm, int numFrames, int refresh, File tracefile) {

		// Algorithm Choice (quicker than using .compareTo every interation of our simulation)
		int algChoice = 4;
			 if(algorithm.compareTo("opt") 	  == 0) algChoice = 0;
		else if(algorithm.compareTo("clock")  == 0) algChoice = 1;
		else if(algorithm.compareTo("random") == 0) algChoice = 2;
		else if(algorithm.compareTo("nru")	  == 0) algChoice = 3;

		// Algorithm Statistics
		int usedFrames = 0, numRefs = 0, numFaults = 0, numWrites = 0; 

		Scanner reader = null;
		try {   reader = new Scanner(tracefile);  }
		catch(FileNotFoundException e) { 
			System.out.println("\n!!! ERROR - File not found !!!\n"); return;
		}

		PageTableEntry[] pageTable = new PageTableEntry[NUM_PAGES];		// Create page table
		for(int i = 0; i < NUM_PAGES; i++)								// Initialize all PTE's
			pageTable[i] = new PageTableEntry(false,false,false,-1);

		int[] frameTable = new int[numFrames];	// Create frame table (remains empty until we load a page)
		for(int i = 0; i < numFrames; i++) {	// Initialize all frames to 'empty' status
			frameTable[i] = -1;
		}

		RefEntry[] refTable = new RefEntry[NUM_PAGES];		// Create lookup table for memory references from file (offline setting)
		String[]   tokens 	= null;										// Will hold the separated address & mode (read or write)
		String 	   line;						// Read first line of file
		while(reader.hasNextLine()) {									// And keep reading until there is nothing left to read (EOF)
			line = reader.nextLine();										// Read next line
			tokens = line.split("\\s");										// Split line into address and mode
			refTable[numRefs] = new RefEntry(tokens[0],tokens[1]);			// Make new RefEntry from contents of tokens
			numRefs++;														// Increment total number of memory references made
		}
		
		
		if(algChoice == 0) {
			
		}

		for(int i = 0; i < numRefs; i++) {
			long temp = Long.parseLong(refTable[i].getAddress(),32); 	// Get full virtual address
			int  pAddress = (int) temp >>> 12;							// Logical shift 12 bits to isolate our page address
			char mode = refTable[i].getMode();							// Find if we are reading or writing

			//	   if(algChoice == 0)	set up shit OPT needs
 			//else if(algChoice == 3)	set up shit NRU needs

			if(!pageTable[pAddress].isValid()) {		// If page address is invalid (page fault)

				numFaults++;								// Increment running count of page faults
				if(usedFrames < numFrames) {				// Compulsary Miss: we still have open frames (no eviction necessary)
					for(int j = 0; j < numFrames; j++) {		// Find an open frame
						if(frameTable[j] == -1) {					// If this frame is open
							frameTable[j] = pAddress;					// Set frame to hold requested page
							pageTable[pAddress].setFrameNum(i);			// Associate page with frame it's being loaded into
							usedFrames++;								// Increment number of used frames in frame table
						}
					}
				} else {									// Capacity Miss: we need to evict something
					
					int evictionIndex = 0;
						 if(algChoice == 0) evictionIndex = opt(frameTable,refTable,numFrames,numRefs);
					else if(algChoice == 1) evictionIndex = clock(frameTable,numFrames);
					else if(algChoice == 2) evictionIndex = random(numFrames);
					else if(algChoice == 3) evictionIndex = nru(frameTable,numFrames,refresh);

					int pageToEvict = frameTable[evictionIndex];	// Get address of page we want to evict
					pageTable[pageToEvict].setFrameNum(-1);			// Disassociate frame from page
					pageTable[pageToEvict].setValid(false);			// Set valid bit to 0 (signal page no longer is loaded into a frame)
					pageTable[pageToEvict].setRef(false);			// Set referenced bit to 0 (signal page has not been referenced)
					
					if(pageTable[pageToEvict].isDirty()) {			// If page has been modified (is dirty) since being loaded into a frame
						pageTable[pageToEvict].setDirty(false);			// Set dirty bit to 0 (signal page has not been modified)
						numWrites++;									// Write page back to disk (increment running count of disk writes)
					}
					
					frameTable[evictionIndex] = pAddress;			// 
					pageTable[pAddress].setFrameNum(i);
				}
				pageTable[pAddress].setValid(true);				// Set valid it to 1 (signal page is loaded into a frame)																						
			}
			pageTable[pAddress].setRef(true);				// Set referenced bit to 1 (signal page has been referenced)
			if(mode == 'W')									// If page is written to
				pageTable[pAddress].setDirty(true);				// Set dirty bit to 1 (signal page has been modified)
		} 
		report(algorithm,numFrames,numRefs,numFaults,numWrites);
	}

	private static void report(String algorithm, int numFrames, int numRefs, int numFaults, int numWrites){
		System.out.println("Algorithm:\t" + algorithm);
		System.out.println("Number of frames:\t" + numFrames);
		System.out.println("Total memory accesses:\t" + numRefs);
		System.out.println("Total page faults:\t" + numFaults);
		System.out.println("Total writes to disk:\t" + numWrites);
	}




	 /////////////////////////////////////
	// Command Handling & Dependencies //
   /////////////////////////////////////

	public static void main(String args[]) {
		
		String algorithm;
		int numFrames, refresh = 0;
		File f = null;

		// Parse command line flags
		if(args[0].compareTo("-n") == 0) {
			numFrames = Integer.parseInt(args[1]);
			if(args[2].compareTo("-a") == 0) {
				algorithm = args[3];
				if(algorithm.compareTo("nru") == 0 && args[4].compareTo("-r") == 0) {
					refresh = Integer.parseInt(args[5]);
					if(args[6].compareTo("-f") == 0) {
						f = new File(args[7]);
						System.out.println("Reading from " + args[7] + "...");
					}
				}
				else if(args[4].compareTo("-f") == 0){
					f = new File(args[5]);
					System.out.println("Reading from " + args[5] + "...");
				}
				simulate(algorithm,numFrames,refresh,f);
			}
		}
	}
}

class PageTableEntry {
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

class RefEntry {
	private String address;
	private char   mode;

	public RefEntry(String a, String m) {
		address = a;
		mode = m.charAt(0);
	}

	public String getAddress() {
		return address;
	}

	public char getMode() {
		return mode;
	}
}