package Models;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hw1.R;
import com.google.android.material.textview.MaterialTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment fragment;
    private ArrayList<Record> records = new ArrayList<>();
    private RecordItemClickedListener recordItemClickedListener;

    public ListRecordAdapter(Fragment fragment, ArrayList<Record> records){
        this.records = records;
        this.fragment = fragment;
    }

    public ListRecordAdapter setRecordItemClickedListener(RecordItemClickedListener recordItemClickedListener) {
        this.recordItemClickedListener = recordItemClickedListener;
        return this;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_scores , parent , false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecordViewHolder recordViewHolder = (RecordViewHolder) holder;
        Record record = getItem(position);

        recordViewHolder.record_LBL_score.setText("" +record.getScore());
        recordViewHolder.record_LBL_player.setText(record.getName());

    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private Record getItem(int position){
        return records.get(position);
    }

    public interface RecordItemClickedListener{
        void RecordItemClicked(Record record ,int position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder{

        private MaterialTextView record_LBL_player;
        private MaterialTextView record_LBL_score;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            record_LBL_player = itemView.findViewById(R.id.player_TBL_records);
            record_LBL_score = itemView.findViewById(R.id.score_TBL_records);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordItemClickedListener.RecordItemClicked(getItem(getAdapterPosition()) , getAdapterPosition());
                }
            });

        }
    }
}
