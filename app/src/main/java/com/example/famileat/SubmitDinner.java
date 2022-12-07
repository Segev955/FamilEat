package com.example.famileat;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.famileat.databinding.ActivityHostMainBinding;
import com.example.famileat.databinding.ActivitySubmitDinnerBinding;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import classes.Dinner;

public class SubmitDinner extends AppCompatActivity {

    private Button location,submit, btnTime, uploadImage;
    private EditText text_title,text_details,text_date;
    private TextView location_text;
    private RadioGroup radio_kosher;
    private RadioButton kosher_r;
    private NumberPicker amont_t;
    private ImageView imgGallery;
    int PLACE_PICKER_REQUEST = 1, SELECT_PICTURE=200;
    private final int GALLERY_REQ_CODE= 1000;
//    private  Uri selectedImageUri;

    // Upload Image: .................................
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private String fileName;
    //...............................................

    //    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_dinner);
        text_title = findViewById(R.id.title);
        text_date = findViewById(R.id.date);
        btnTime =findViewById(R.id.time);
        amont_t = findViewById(R.id.amount_p);
        amont_t.setMinValue(0);
        amont_t.setMaxValue(500);
        radio_kosher = findViewById(R.id.kosher);
        text_details = findViewById(R.id.details);
        submit = findViewById(R.id.submit);
        imgGallery = findViewById(R.id.imgGallery);
        auth = FirebaseAuth.getInstance();
        fileName = "dufult_dinner.jpg";

        //View the default image.
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/" + fileName);
        try {
            File file = File.createTempFile("temp", ".png");
            storageReference.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imgGallery.setImageBitmap(bitmap);
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

        //Pick Picture Button
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


//        //location picker
//        location = findViewById(R.id.location);
//        location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                try {
//                    startActivityForResult(builder.build(SubmitDinner.this),
//                            PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        //Date Button:
        text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);
                DatePickerDialog dateDialog = new DatePickerDialog(SubmitDinner.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                text_date.setText(date);
            }
        };

        //Time Button
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(SubmitDinner.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                btnTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });


        //Submit button: ..................................................
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = text_title.getText().toString();
                String date = text_date.getText().toString();
                String time = btnTime.getText().toString();
                String address = "maze pinat mapo";
                int amount = amont_t.getValue();
                int select_kosher = radio_kosher.getCheckedRadioButtonId();
                String details = text_details.getText().toString();

                //validation input checking...............................
                String valid_ans = "";
                if (!(valid_ans=Dinner.check_title(title)).equals("accept")) {
                    Toast.makeText(SubmitDinner.this, valid_ans, Toast.LENGTH_SHORT).show();
                    text_title.requestFocus();
                } else if (!(valid_ans= Dinner.check_date_time(date,time)).equals("accept")) {
                    Toast.makeText(SubmitDinner.this, valid_ans, Toast.LENGTH_SHORT).show();
                    text_date.requestFocus();
                } else {
                    String kosher ="Unspecified";
                    if(select_kosher!=-1) {
                        kosher_r = (RadioButton) findViewById(select_kosher);
                        kosher = kosher_r.getText().toString();
                    }
                    submitDinner(title, date, time, address, amount, kosher, details, fileName);
                }
            }
        });

    }
    //...................................................................

    private void submitDinner(String title, String date, String time, String address, int amount, String kosher, String details,String fileName) {
        String Uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Dinner dinner = new Dinner(Uid,title, date, time, address, amount, kosher,details, fileName);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Dinners").push();
        String Did=reference.getKey();
        reference.setValue(dinner).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SubmitDinner.this, title + " submitted successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SubmitDinner.this, HostMainActivity.class));
                }
                else
                    Toast.makeText(SubmitDinner.this, "Submission failed!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);


    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file....");
        progressDialog.show();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        fileName = format.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);


        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                imgGallery.setImageURI(imageUri);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SubmitDinner.this, "image upload failed!", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgGallery.setImageURI(imageUri);
            uploadImage();
        }

    }


}