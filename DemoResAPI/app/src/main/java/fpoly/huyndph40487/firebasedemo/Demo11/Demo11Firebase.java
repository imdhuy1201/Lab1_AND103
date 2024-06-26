package fpoly.huyndph40487.firebasedemo.Demo11;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import fpoly.huyndph40487.firebasedemo.R;

public class Demo11Firebase extends AppCompatActivity {
    FirebaseFirestore database;
    Button btn_them, btn_sua, btn_xoa, btn_display;
    TextView tvResult;
    String id = "";
    ToDo toDo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance(); // khoi tao database
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_demo11_firebase);
        tvResult = findViewById(R.id.textView);
        btn_them = findViewById(R.id.btn_them);
        btn_them.setOnClickListener(view -> {
            insertFirebase(tvResult);
        });
        btn_sua = findViewById(R.id.btn_sua);
        btn_sua.setOnClickListener(view -> {
            upDateFireBase(tvResult);
        });
        btn_xoa = findViewById(R.id.btn_xoa);
        btn_xoa.setOnClickListener(view -> {
            DeleteFireBase(tvResult);
        });
        btn_display=findViewById(R.id.btn_display);
        btn_display.setOnClickListener(view -> {
            selectDataFromFirebase(tvResult);

        });

    }

    public void insertFirebase(TextView tvResult) {
        id = UUID.randomUUID().toString();
        //tajo doi tuong de insert
        toDo = new ToDo(id, "Iphone12", "20.000.000");
        // chuyen doi sang doi tuong co the thao tac voi firebase
        HashMap<String, Object> mapTodo = toDo.convertHashMap();
        //insert vao database
        database.collection("TODO").document(id)
                .set(mapTodo) // doi tuong can insert
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tvResult.setText("Them Thanh Cong");
                        Toast.makeText(getApplicationContext(), "ToDo added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });

    }

    public void upDateFireBase(TextView tvResult) {
        id = "23abf378-f1a4-4c46-971d-ca4378b9b258";
        toDo = new ToDo(id, "Iphone14", "30.000.000");
        database.collection("TODO").document(toDo.getId())
                .update(toDo.convertHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Demo11Firebase.this.tvResult.setText("sua thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Demo11Firebase.this.tvResult.setText(e.getMessage());
                    }
                });

    }
    public void DeleteFireBase(TextView tvResult) {
        id = "23abf378-f1a4-4c46-971d-ca4378b9b258";
        toDo = new ToDo(id, "Iphone12", "20.000.000");
        database.collection("TODO").document(toDo.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Demo11Firebase.this.tvResult.setText("xoa thanh cong");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Demo11Firebase.this.tvResult.setText(e.getMessage());
                    }
                });
    }
    String strResulte = "";
    public ArrayList<ToDo> selectDataFromFirebase(TextView tvResult) {
        ArrayList<ToDo> list = new ArrayList<>();
        database.collection("TODO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) { // sau khi lay du lieu thanh cong
                            strResulte = "";
                            //đọc theo từng dòng dữ liệu
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //chuyển dòng đọc được sang đối tượng
                                ToDo toDo1=document.toObject(ToDo.class);
                                //chuyển đối tượng thành chuỗi
                                strResulte+="Name: "+toDo1.getTitle()+"\n";
                                strResulte+="Price: "+toDo1.getContent()+"\n";
                                list.add(toDo1); // thêm vào list
                            }
                            //hiển thị keest quả
                            tvResult.setText(strResulte);
                        }
                        else {
                            tvResult.setText("dọc dữ liệu thất bại");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
        return list;
    }
}