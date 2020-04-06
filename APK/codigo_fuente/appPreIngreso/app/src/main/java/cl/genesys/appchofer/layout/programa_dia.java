package cl.genesys.appchofer.layout;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cl.genesys.appchofer.R;
import cl.genesys.appchofer.bbdd.ProgramaAsicamDao;
import cl.genesys.appchofer.entities.ProgramaAsicam;
import cl.genesys.appchofer.utilities.Constantes;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class programa_dia extends Fragment {
    LayoutInflater vinflater;
    public static RecyclerView rv;
    LinearLayoutManager llm;
    public static SharedPreferences sharedPref;

    public programa_dia() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_programa_dia, container, false);
        vinflater = inflater;
        sharedPref = this.getActivity().getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        String patente = sharedPref.getString("patente", "");
        rv = (RecyclerView)v.findViewById(R.id.rv_OT);
        llm = new LinearLayoutManager(MainActivity.context);
        rv.setLayoutManager(llm);
        final ProgramaAsicamDao _dao = new ProgramaAsicamDao(getContext());

        _dao.open();
        List<ProgramaAsicam> _OTs = null;
        try {
            _OTs = _dao.getProductoProgramaAsicam(MainActivity.context, patente);
            if (_OTs.size() > 0) {
                programa_dia.RVAdapter adapter = new programa_dia.RVAdapter(_OTs);
                rv.setAdapter(adapter);
            }else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //_ot = _dao.getOrdenTransporteByFolioDte(getContext(), folio);

        //adapter.llenaInfo(_ot);
        _dao.close();


        return v;
    }

    public static class RVAdapter extends RecyclerView.Adapter<programa_dia.RVAdapter.OrdenesTransporteViewHolder> {
        //public ListaValoresDao _Dao = new ListaValoresDao(MainActivity.context);
        public String ot;
        LinearLayout llOT_CV;
        public class OrdenesTransporteViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView OT_tvOT, OT_empresa, OT_Asicam, OT_tvGDE, OT_tvPatente, OT_tvHrAtencionIni, OT_tvDestino, OT_tvProducto;

            OrdenesTransporteViewHolder(View vi) {
                super(vi);
                cv = (CardView)itemView.findViewById(R.id.cvOT);
                llOT_CV = itemView.findViewById(R.id.llOT_CV);
                OT_tvOT = vi.findViewById(R.id.OT_tvOT);
                OT_empresa = vi.findViewById(R.id.OT_empresa);
                OT_Asicam = vi.findViewById(R.id.OT_Asicam);
                OT_tvGDE = vi.findViewById(R.id.OT_tvGDE);
                OT_tvPatente = vi.findViewById(R.id.OT_tvPatente);
                OT_tvHrAtencionIni = vi.findViewById(R.id.OT_tvHrAtencionIni);
                OT_tvDestino = vi.findViewById(R.id.OT_tvDestino);
                OT_tvProducto = vi.findViewById(R.id.OT_tvProducto);

            }
        }

        List<ProgramaAsicam> _OT;

        RVAdapter(List<ProgramaAsicam> orden){
            this._OT = orden;
        }

        @Override
        public int getItemCount() {
            return _OT.size();
        }

        @Override
        public programa_dia.RVAdapter.OrdenesTransporteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_ot_row, viewGroup, false);
            programa_dia.RVAdapter.OrdenesTransporteViewHolder pvh = new programa_dia.RVAdapter.OrdenesTransporteViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(final programa_dia.RVAdapter.OrdenesTransporteViewHolder ordenesTransporteViewHolder, int i) {


            if (_OT.get(i).getGde() == 0) {
                llOT_CV.setBackgroundResource(R.drawable.gde_row_view);
            }else {
                llOT_CV.setBackgroundResource(R.drawable.gde_row_view_t);
            }

            TextView  OT_tvOT, OT_empresa, OT_Asicam, OT_tvGDE, OT_tvPatente, OT_tvHrAtencionIni, OT_tvDestino, OT_tvProducto;


            ordenesTransporteViewHolder.OT_tvOT.setText(_OT.get(i).getOT().toString());


            ordenesTransporteViewHolder.OT_empresa.setText(_OT.get(i).getTransportista());

            ordenesTransporteViewHolder.OT_Asicam.setText(_OT.get(i).getAsicam().toString());
            if (_OT.get(i).getGde() != 0) {
                ordenesTransporteViewHolder.OT_tvGDE.setText(_OT.get(i).getGde().toString());
            }

            try {
                ordenesTransporteViewHolder.OT_tvHrAtencionIni.setText(_OT.get(i).getHora_atencion().split(" ")[1]);
            } catch (Exception e) {
                ordenesTransporteViewHolder.OT_tvHrAtencionIni.setText("-");
            }
            ordenesTransporteViewHolder.OT_tvPatente.setText(_OT.get(i).getPatente());
            ordenesTransporteViewHolder.OT_tvDestino.setText(_OT.get(i).getDestino());
            try {
                ordenesTransporteViewHolder.OT_tvProducto.setText(_OT.get(i).getProducto());
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

}
