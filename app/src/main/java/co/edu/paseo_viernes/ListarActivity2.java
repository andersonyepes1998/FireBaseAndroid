package co.edu.paseo_viernes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListarActivity2 extends AppCompatActivity {

    RecyclerView recyclerpaseo;
    ArrayList<ClsPaseo> listapaseo;
    FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar2);
        recyclerpaseo = findViewById(R.id.rvlistarpaseo);
        listapaseo = new ArrayList<ClsPaseo>();
        recyclerpaseo.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerpaseo.setHasFixedSize(true);
        cargarpaseo();
    }

    private void cargarpaseo(){
            db.collection("factura")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    ClsPaseo objpaseo=new ClsPaseo();
                                    objpaseo.setCodigo(document.getString("Codigo"));
                                    objpaseo.setNombre(document.getString("Nombre"));
                                    objpaseo.setCiudad(document.getString("Ciudad"));
                                    objpaseo.setCantidad(document.getString("Cantidad"));
                                    listapaseo.add(objpaseo);
                                }
                                PaseoAdapte adpaseo=new PaseoAdapte(listapaseo);
                                recyclerpaseo.setAdapter(adpaseo);
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }