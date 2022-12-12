package adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.famileat.ChatActivity;
import com.example.famileat.EditDinnerActivity;
import com.example.famileat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import classes.Dinner;
import classes.Request;

public class HostsAdapter extends RecyclerView.Adapter<HostsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Dinner> list;

    int proImage;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public static String currUid;
    private AlertDialog.Builder dinnerOptions;
    //    private AlertDialog options;
    Bitmap bitmap;






    public HostsAdapter(Context context, ArrayList<Dinner> list, int proImage, AlertDialog.Builder dinnerOptions) {
        this.context = context;
        this.list = list;
        this.proImage = proImage;
        this.currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.dinnerOptions = dinnerOptions;
        //this.options = options;

    }

    @NonNull
    @Override
    public HostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dinner_view_host, parent, false);
        return new HostsAdapter.MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HostsAdapter.MyViewHolder holder, int position) {
        Dinner dinner = list.get(position);
        holder.title.setText(dinner.getTitle());
        holder.address.setText(dinner.getAddress());
        holder.date.setText(dinner.getDate());
        holder.time.setText(dinner.getTime());
        int av = Dinner.numOfAvailables(dinner);
        if (av == 0)
            holder.availables.setText("FULL");
        else
            holder.availables.setText(Integer.toString(av));
        holder.kosher.setText(dinner.getKosher());
        holder.dinnerImage.setImageBitmap(bitmap);

        //Set picture
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

        final String[] Rid = new String[1];
        //Set meal button
        holder.mealbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currUid=dinner.getID();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contactPopuoView = inflater.inflate(R.layout.host_dinner_options, null);
                AlertDialog options;
                TextView title, address, date, time, availables, kosher, details, amount;
                ImageView dinnerImage;
                Button editBtn,deleteBtn, chatbtn;

                title = contactPopuoView.findViewById(R.id.tvTitle);
                address = contactPopuoView.findViewById(R.id.tvAddress);
                date = contactPopuoView.findViewById(R.id.tvDate);
                time = contactPopuoView.findViewById(R.id.tvTime);
                availables = contactPopuoView.findViewById(R.id.tvAvailables);
                amount = contactPopuoView.findViewById(R.id.tvAmount);
                kosher = contactPopuoView.findViewById(R.id.tvKosher);
                dinnerImage = contactPopuoView.findViewById(R.id.dinnerImage);
                details = contactPopuoView.findViewById(R.id.tvDetails);
                deleteBtn = contactPopuoView.findViewById(R.id.deleteDinner);
                editBtn = contactPopuoView.findViewById(R.id.editDinner);
                chatbtn = contactPopuoView.findViewById(R.id.chatbtn);

                title.setText(dinner.getTitle());
                address.setText(dinner.getAddress());
                date.setText(dinner.getDate());
                time.setText(dinner.getTime());
                int av = Dinner.numOfAvailables(dinner);
                if (av == 0)
                    availables.setText("FULL");
                else
                    availables.setText(Integer.toString(av));
                amount.setText(Integer.toString(dinner.getAmount()));
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
                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            dinnerImage.setImageBitmap(bitmap);
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

                dinnerOptions.setView(contactPopuoView);
                options = dinnerOptions.create();
                options.show();

                //Set edit button
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editIntent = new Intent(context.getApplicationContext(), EditDinnerActivity.class);
                        context.startActivity(editIntent);
                        options.cancel();
                    }
                });

                //Set delete button
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dinner.deleteDinnerById(dinner.getID());
                        Request.deleteRequstsByDinnerId(dinner.getID());
                        options.cancel();
                    }
                });

                //Set chat button
                chatbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent chatIntent = new Intent(context.getApplicationContext(), ChatActivity.class);
                        chatIntent.putExtra("Did",dinner.getID());
                        context.startActivity(chatIntent);
                        options.cancel();
                    }
                });
                //show image
/*                dinnerImage.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View v = LayoutInflater.from(context).inflate(R.layout.dinner_view_host, parent, false);
                    }
                }));*/

            }
        });





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

            mealbtn = itemView.findViewById(R.id.reqbtn);
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
