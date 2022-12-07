package classes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famileat.HostMainActivity;
import com.example.famileat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class hostAdapter extends RecyclerView.Adapter<hostAdapter.MyViewHolder> {

    Context context;

    ArrayList<Dinner> list;

    int proImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String currUid;
    private AlertDialog.Builder dinnerOptions;
//    private AlertDialog options;






    public hostAdapter(Context context, ArrayList<Dinner> list, int proImage, AlertDialog.Builder dinnerOptions) {
        this.context = context;
        this.list = list;
        this.proImage = proImage;
        this.currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.dinnerOptions = dinnerOptions;
        //this.options = options;

    }

    @NonNull
    @Override
    public hostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dinner_view_host, parent, false);
        return new hostAdapter.MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull hostAdapter.MyViewHolder holder, int position) {
        Dinner dinner = list.get(position);
        holder.title.setText(dinner.getTitle());
        holder.address.setText(dinner.getAddress());
        holder.date.setText(dinner.getDate());
        holder.time.setText(dinner.getTime());
        holder.availables.setText(Integer.toString(Dinner.numOfAvailables(dinner)));
        holder.kosher.setText(dinner.getKosher());

        final String[] Rid = new String[1];
        //Set meal button
        holder.mealbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contactPopuoView = inflater.inflate(R.layout.host_dinner_options, null);
                AlertDialog options;
                TextView title, address, date, time, availables, kosher, details, photoname;
                ImageView dinnerImage;
                title = contactPopuoView.findViewById(R.id.tvTitle);
                address = contactPopuoView.findViewById(R.id.tvAddress);
                date = contactPopuoView.findViewById(R.id.tvDate);
                time = contactPopuoView.findViewById(R.id.tvTime);
                availables = contactPopuoView.findViewById(R.id.tvAvailables);
                kosher = contactPopuoView.findViewById(R.id.tvKosher);
                dinnerImage = contactPopuoView.findViewById(R.id.dinnerImage);
                details = contactPopuoView.findViewById(R.id.tvDetails);

                title.setText(dinner.getTitle());
                address.setText(dinner.getAddress());
                date.setText(dinner.getDate());
                time.setText(dinner.getTime());
                availables.setText(Integer.toString(Dinner.numOfAvailables(dinner)));
                kosher.setText(dinner.getKosher());
                details.setText(dinner.getDetails());
                //set image
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference("images/" + dinner.getPicture());
                try {
                    File file = File.createTempFile("temp", ".png");
                    storageReference.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            dinnerImage.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            System.out.println("Failed");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                dinnerOptions.setView(contactPopuoView);
                options = dinnerOptions.create();
                options.show();

            }
        });


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/" + dinner.getPicture());
        try {
            File file = File.createTempFile("temp", ".png");
            storageReference.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    holder.dinnerImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    System.out.println("Failed");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void createDialog() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView mealbtn;
        TextView title, address, date, time, availables, kosher, details, photoname;
        ImageView dinnerImage;
        AlertDialog.Builder dinnerOptions;
        AlertDialog options;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mealbtn = itemView.findViewById(R.id.mealbtn);
            title = itemView.findViewById(R.id.tvTitle);
            address = itemView.findViewById(R.id.tvAddress);
            date = itemView.findViewById(R.id.tvDate);
            time = itemView.findViewById(R.id.tvTime);
            availables = itemView.findViewById(R.id.tvAvailables);
            kosher = itemView.findViewById(R.id.tvKosher);
            dinnerImage = itemView.findViewById(R.id.dinnerImage);


        }
    }
}