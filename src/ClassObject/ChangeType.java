package ClassObject;

public enum ChangeType {
	TAKEOFF,
	RESUME,
	NULL;
	
	public static ChangeType gotChangeType(int changeType) {
		switch(changeType) {
		case 0:		return TAKEOFF;
		case 1:		return RESUME;
		default: 	return NULL;
		}
	}
	
	public static int gotTinyInt(ChangeType changeType) {
		switch(changeType) {
		case TAKEOFF:		return 0;
		case RESUME:		return 1;
		default: 			return -1;
		}
	}
}
