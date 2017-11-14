import java.io.File;
import java.lang.Math;

public class vmsim {
	
	// System Fields (32-bit)
	private static final int PAGE_SIZE 	  = (int) Math.pow(2,12);		// 2^12 (4KB)
	private static final int ADDRESS_SIZE = (int) Math.pow(2,32);		// 2^32 (all enumerable addresses for a 32-bit system)
	private static final int NUM_PAGES 	  = ADDRESS_SIZE / PAGE_SIZE;	// 2^32 / 2^12 = 2^20 (total # of address divided by page size)
	
	private static void report(String algorithm, int numFrames, int numAccesses, int numFaults, int numWrites){}

	// Page Replacement Algorithms
	private static void opt(int numFrames, File instructions){}
	private static void clock(int numFrames, File instructions){}
	private static void nru(int numFrames, int refresh, File instructions){}
	private static void random(int numFrames, File instructions){}

	private static void simulate(String algorithm, int numFrames, int refresh, File instructions) {

		PageTableEntry[]  pageTable = new PageTableEntry[NUM_PAGES];
		for(int i = 0; i < NUM_PAGES; i++)
			pageTable[i] = new PageTableEntry(false,false,false,-1);

		PageTableEntry[] frameTable = new PageTableEntry[numFrames];
	}

	public static void main(String args[]) {
		
		String algorithm;
		int numFrames, refresh = 0;
		File f;

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
				else f = new File("killme.txt");
				simulate(algorithm,numFrames,refresh,f);
			}
		}
	}
}