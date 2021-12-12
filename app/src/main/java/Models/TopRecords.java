package Models;

import java.util.ArrayList;

public class TopRecords {

    public final static int MAX_REC_IN_LIST = 5;
    private ArrayList<Record> records = new ArrayList<>();

    public TopRecords() { }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public TopRecords setRecords(ArrayList<Record> records) {
        this.records = records;
        return this;
    }

    public void addRecord(Record record){
        this.records.add(record);
        sortByScore();
        if (this.records.size() > MAX_REC_IN_LIST)
            this.records.remove(MAX_REC_IN_LIST);
    }

    private void sortByScore() {
        records.sort((score1, score2) -> score2.getScore()-score1.getScore());
    }
}
