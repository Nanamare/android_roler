package com.vocketlist.android.network.executor;

/**
 * 우선 순위
 * 
 * @author Lim SeungTaek 2014. 6. 12
 *
 */
public enum Priority {
	HIGHEST(0),
	HIGH(1),
	MEDIUM(2),
	LOW(3),
	LOWEST(4);

	int value;

	Priority(int val) {
		this.value = val;
	}

	public int getValue(){
		return value;
	}
}