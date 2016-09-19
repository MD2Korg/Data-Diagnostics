package org.md2k.datadiagnostic.struct;


public class DataPoints  implements Comparable<DataPoints>{


    public long timestamp;
	public double value;
    public long endTimestamp;


    /**
     * DataPoint Constructor
     *
     * @param timestamp Time in milliseconds since Jan 1st, 1970
     * @param value a value corresponding to a timestamp
     */
    public DataPoints(long timestamp, double value) {
        this.value = value;
        this.timestamp = timestamp;
    }
    
    /**
     * DataPoint Constructor
     *
     * @param timestamp Start Time in milliseconds since Jan 1st, 1970
     * @param endTimestamp     End Time in milliseconds since Jan 1st, 1970
     */
    public DataPoints(long timestamp, long endTimestamp) {
        this.timestamp = timestamp;
        this.endTimestamp = endTimestamp;
    }


    /**
     * Copy Constructor
     *
     * @param other DataPointArray object
     */
    public DataPoints(DataPoints other) {
        this.value = other.value;
        this.timestamp = other.timestamp;
    }


    public long getTimestamp(){
		return timestamp;
	}
	
	public double getValue(){
		return value;
	}
	
	public long getEndTimestamp(){
		return endTimestamp;
	}
	
	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}
	
	public void setValue(double value){
		this.value = value;
	}
	
	public void setEndTimestamp(long endTimestamp){
		 this.endTimestamp = endTimestamp;
	}
	
    /**
     * @return String representation of a DataPoint object
     */
    @Override
    public String toString() {
        return "DP:(" + this.timestamp + "," + this.endTimestamp + ")";
    }



	@Override
	public int compareTo(DataPoints o) {
		// TODO Auto-generated method stub
		 if (this.value == o.value && this.timestamp == o.timestamp) {
	            return 0; //They are the same
	        } else if (this.timestamp < o.timestamp) {
	            return -1; //"this" is before "o"
	        } else {
	            return 1; //"this" is after "o"
	        }
	}
	
	@Override
	public boolean equals(Object other) {
	    if (!(other instanceof DataPoints)) {
	        return false;
	    }

	    DataPoints that = (DataPoints) other;

	    // Custom equality check here.
	    return this.timestamp==that.timestamp
	        && this.endTimestamp==that.endTimestamp;
	}
}
