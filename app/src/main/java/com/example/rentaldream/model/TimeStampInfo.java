package com.example.rentaldream.model;

public class TimeStampInfo {

        private String contract;
        private String time;
        private String message;
        private long timestamp;


        public TimeStampInfo(){

        }

        public TimeStampInfo(String contract, String time, String message, long timestamp) {
            this.contract = contract;
            this.time = time;
            this.message = message;
            this. timestamp  = timestamp;
        }

        public TimeStampInfo(TimeStampInfo object) {
            this.contract = object.contract;
            this.time = object.time;
            this.message = object.message;
            this. timestamp  = object.timestamp;
        }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
