package cl.genesys.appchofer.bbdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cl.genesys.appchofer.entities.OrdenesTransporte;
import cl.genesys.appchofer.utilities.Constantes;

import static android.content.Context.MODE_PRIVATE;
import static cl.genesys.appchofer.utilities.ServicioConectado.sharedPref;

public class OrdenesTransporteDao {

    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;
    JSONArray jsonArray = null;

    public OrdenesTransporteDao(Context context){
        dbHelper = new MySQLiteOpenHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertOrdenTransporte(ArrayList<OrdenesTransporte> vdata, String patente) throws Exception {
        try {

            //dbHelper = new MySQLiteOpenHelper(context);
            if(vdata!=null ){ //&& vdata.size()>0

                for (int i = 0; i < vdata.size(); i++) {
                    OrdenesTransporte cf = new OrdenesTransporte();
                    cf = vdata.get(i);

                    // db = dbHelper.getWritableDatabase();
                    db.execSQL("INSERT  INTO DUT_ORDENES_TRANSPORTE ( " +
                                    "NUMERO_OT " +
                                    ",NOMBRE_CHOFER" +
                                    ",RUT_CHOFER" +
                                    ",PRODUCTO_CODIGO" +
                                    ",PRODUCTO_CODIGO_DESC" +
                                    ",GRUA" +
                                    ",PATENTE_CAMION" +
                                    ",PATENTE_CARRO" +
                                    ",HORA_INICIO_VIAJE" +
                                    ",ORIGEN_DSC" +
                                    ",ORIGEN_CODIGO" +
                                    ",ORIGEN_FORMIN" +
                                    ",PLANTA_ORIGEN" +
                                    ",CLAVE_ASICAM" +
                                    ",HORA_LLEGADA_ORIGEN" +
                                    ",HORA_SERVICIO_ORIGEN" +
                                    ",DESTINO_DSC" +
                                    ",DESTINO_CODIGO" +
                                    ",DESTINO_FORMIN" +
                                    ",PLANTA_DESTINO " +
                                    ",PROV_RUT_FOLIO " +
                                    ",PROV_RAZON_SOCIAL " +
                                    ",CAAC_GEO_SUP_IZQ_X" +
                                    ",CAAC_GEO_SUP_IZQ_Y" +
                                    ",CAAC_GEO_INF_DER_X" +
                                    ",CAAC_GEO_INF_DER_Y" +
                                    ",CUM_ID_ASICAM" +
                                    ",PERIODO_ASICAM" +
                                    ",HORA_LLEGADA_DESTINO" +
                                    ") " +
                                    "VALUES ( " +
                                    "?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22, ?23, ?24, ?25, ?26, ?27, ?28, ?29" +
                                    "); ",
                            new Object[]{
                                    cf.getNumero_ot(),
                                    cf.getNombre_chofer(),
                                    cf.getRut_chofer(),
                                    cf.getProducto_codigo(),
                                    cf.getProducto_codigo_desc(),
                                    cf.getGrua(),
                                    patente,
                                    cf.getPatente_carro(),
                                    cf.getHora_inicio_viaje(),
                                    cf.getOrigen_desc(),
                                    cf.getOrigen_codigo(),
                                    cf.getOrigen_formin(),
                                    cf.getPlanta_origen(),
                                    cf.getClave_asicam(),
                                    cf.getHora_llegada_origen(),
                                    cf.getHora_servicio_origen(),
                                    cf.getDestino_dsc(),
                                    cf.getDestino_codigo(),
                                    cf.getDestino_formin(),
                                    cf.getPlanta_destino(),
                                    cf.getProv_rut_folio(),
                                    cf.getProv_razon_social(),
                                    /*cf.getPrma_esma_codigo_especie(),
                                    cf.getPrma_codigo_presentacion(),
                                    cf.getPrma_nombre(),*/

                                    cf.getCaac_geo_sup_izq_x(),
                                    cf.getCaac_geo_sup_izq_y(),
                                    cf.getCaac_geo_inf_der_x(),
                                    cf.getCaac_geo_inf_der_y(),


                                    cf.getCum_id_asicam(),
                                    cf.getPeriodo_asicam(),
                                    cf.getHora_llegada_destino()
                            });


                    //dbHelper.close();
                }
            }

            return true;

        } catch (Exception ex) {
            //dbHelper.close();
            ex.printStackTrace();

            return false;
        }
    }

    public int insertOrdenTransporteGDE(ArrayList<OrdenesTransporte> vdata, String patente) throws Exception {
        try {

            //dbHelper = new MySQLiteOpenHelper(context);
            if(vdata!=null ){ //&& vdata.size()>0

                for (int i = 0; i < vdata.size(); i++) {
                    OrdenesTransporte cf = new OrdenesTransporte();
                    cf = vdata.get(i);

                    // db = dbHelper.getWritableDatabase();
                    db.execSQL("INSERT  INTO DUT_ORDENES_TRANSPORTE ( " +
                                    "NUMERO_OT " +
                                    ",NOMBRE_CHOFER" +
                                    ",RUT_CHOFER" +
                                    ",PRODUCTO_CODIGO" +
                                    ",PRODUCTO_CODIGO_DESC" +
                                    ",GRUA" +
                                    ",PATENTE_CAMION" +
                                    ",PATENTE_CARRO" +
                                    ",HORA_INICIO_VIAJE" +
                                    ",ORIGEN_DSC" +
                                    ",ORIGEN_CODIGO" +
                                    ",ORIGEN_FORMIN" +
                                    ",PLANTA_ORIGEN" +
                                    ",CLAVE_ASICAM" +
                                    ",HORA_LLEGADA_ORIGEN" +
                                    ",HORA_SERVICIO_ORIGEN" +
                                    ",DESTINO_DSC" +
                                    ",DESTINO_CODIGO" +
                                    ",DESTINO_FORMIN" +
                                    ",HORA_LLEGADA_DESTINO" +
                                    ",PLANTA_DESTINO" +
                                    ",PROV_RUT_FOLIO" +
                                    ",PROV_RAZON_SOCIAL" +
                                   /* ",PRMA_ESMA_CODIGO_ESPECIE" +
                                    ",PRMA_CODIGO_PRESENTACION" +
                                    ",PRMA_NOMBRE" +*/

                                    ",CAAC_GEO_SUP_IZQ_X" +
                                    ",CAAC_GEO_SUP_IZQ_Y" +
                                    ",CAAC_GEO_INF_DER_X" +
                                    ",CAAC_GEO_INF_DER_Y" +


                                    ",CUM_ID_ASICAM" +
                                    ",PERIODO_ASICAM" +

                                    ",FOLIO_DTE" +
                                    ") " +
                                    "VALUES ( " +
                                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
                                    "); ",
                            new Object[]{
                                    cf.getNumero_ot(),
                                    cf.getNombre_chofer(),
                                    cf.getRut_chofer(),
                                    cf.getProducto_codigo(),
                                    cf.getProducto_codigo_desc(),
                                    cf.getGrua(),
                                    patente,
                                    cf.getPatente_carro(),
                                    cf.getHora_inicio_viaje(),
                                    cf.getOrigen_desc(),
                                    cf.getOrigen_codigo(),
                                    cf.getOrigen_formin(),
                                    cf.getPlanta_origen(),
                                    cf.getClave_asicam(),
                                    cf.getHora_llegada_origen(),
                                    cf.getHora_servicio_origen(),
                                    cf.getDestino_dsc(),
                                    cf.getDestino_codigo(),
                                    cf.getDestino_formin(),
                                    cf.getHora_llegada_destino(),
                                    cf.getPlanta_destino(),
                                    cf.getProv_rut_folio(),
                                    cf.getProv_razon_social(),
                                    /*cf.getPrma_esma_codigo_especie(),
                                    cf.getPrma_codigo_presentacion(),
                                    cf.getPrma_nombre(),*/

                                    cf.getCaac_geo_sup_izq_x(),
                                    cf.getCaac_geo_sup_izq_y(),
                                    cf.getCaac_geo_inf_der_x(),
                                    cf.getCaac_geo_inf_der_y(),


                                    cf.getCum_id_asicam(),
                                    cf.getPeriodo_asicam(),

                                    cf.getFolio_dte()
                            });


                    //dbHelper.close();
                }
            }

        } catch (Exception ex) {
            //dbHelper.close();
            throw new Exception(ex.getMessage());
        }

        return vdata.size();
    }

    public void deleteOrdenTransporte(String OT) throws Exception {

        try {
            open();
            db.execSQL("DELETE FROM DUT_ORDENES_TRANSPORTE where NUMERO_OT = " + OT);
            close();
        }catch (Exception ex){
            close();
            throw new Exception(ex.getMessage());
        }
    }

    public String getOrdenTransporteInsertScriptById(Context context, String OT, String patente) throws Exception {

        String res = "";
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();

            String sql = "SELECT " +
                    "'INSERT OR REPLACE INTO DUT_ORDENES_TRANSPORTE " +
                    "(NUMERO_OT  " +
                    ",HORA_INICIO_VIAJE " +
                    ",PLANTA_ORIGEN " +
                    ",ORIGEN_CODIGO " +
                    ",ORIGEN_DSC " +
                    ",GRUA " +
                    ",PRODUCTO_CODIGO_EXIST " +
                    ",PRODUCTO_CODIGO_VENTA " +
                    ",HORA_LLEGADA_ORIGEN " +
                    ",HORA_SALIDA_ORIGEN " +
                    ",PLANTA_DESTINO " +
                    ",DESTINO_CODIGO " +
                    ",DESTINO_DSC  " +
                    ",EQUIPO_DESCARGA_DESTINO  " +
                    ",HORA_LLEGADA_DESTINO  " +
                    ",PATENTE_CAMION  " +
                    ",PATENTE_CARRO  " +
                    ",RUT_EMPRESA  " +
                    ",EMP_NOMBRE  " +
                    ",PROV_NOMBRE_CORTO " +
                    ",PEDIDO " +
                    ",POSICION " +
                    ",ESTADO " +
                    ",AS4REP_ID  " +
                    ",RUT_CHOFER  " +
                    ",NOMBRE_CHOFER  " +
                    ",FOLIO_DTE  " +
                    ",OT_ORIGEN  " +
                    ",ERROR_GDE  " +
                    ",CONFIRMA_OT  " +
                    ",NRO_CONTRATO  " +
                    ",PRECIO_TRANSPORTE  " +
                    ",ENVIADO  " +
                    ",OT_REDESTINO  " +
                    ",CONTINGENCIA  " +
                    ",MOTIVO  " +
                    ",HORA_SERVICIO_ORIGEN  " +
                    ",VELOCIDAD  " +
                    ",DISTANCIA  " +
                    ",LATITUD  " +
                    ",LONGITUD  " +
                    ",HORA_DESPACHO_REAL  " +
                    ",TIEMPO_ARRIBO  " +
                    ",HORA_ARRIBO  " +
                    ",HORA_ULTIMO_CALCULO  " +
                    ",HORA_SERVICIO_DESTINO  " +
                    ",HORA_SALIDA_DESTINO  " +
                    ",CAUSANTE_ANULACION  " +
                    ",NOMBRE_GRUA  " +
                    ",TIM_ID  " +
                    ",ESTADO_FOLIO_DTE  " +
                    ",RODAL  " +
                    ",DESP_FECHA_BD  " +
                    ",PROV_RUT_FOLIO  " +
                    ",PROV_RAZON_SOCIAL  " +
                    ",FECHA_RECEP  " +
                    ",DIFF_HORA_RECEP  " +
                    ",RECEP_ID  " +
                    ",CAAC_GEO_SUP_IZQ_X  " +
                    ",CAAC_GEO_SUP_IZQ_Y  " +
                    ",CAAC_GEO_INF_DER_X  " +
                    ",CAAC_GEO_INF_DER_Y  " +
                    ",DESP_ANEXO  " +
                    ",CLAVE_ASICAM  " +
                    ",CUM_ID_ASICAM  " +
                    ",PERIODO_ASICAM" +
                    ") VALUES('  " +
                    "|| IFNULL(NUMERO_OT,'0')   " +
                    "|| ',''' || IFNULL(HORA_INICIO_VIAJE,'')  || '''' " +
                    "|| ',' ||  IFNULL(PLANTA_ORIGEN  ,'NULL')  " +
                    "|| ',' ||  IFNULL(ORIGEN_CODIGO  ,'NULL')  " +
                    "|| ',''' || IFNULL(ORIGEN_DSC,'')  || '''' " +
                    "|| ',''' || IFNULL(GRUA,'')  || '''' " +
                    "|| ',''' || IFNULL(PRODUCTO_CODIGO,'')  || '''' " +
                    "|| ',''' || IFNULL(PRODUCTO_CODIGO,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_LLEGADA_ORIGEN,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_SALIDA_ORIGEN,'')  || '''' " +
                    "|| ',' ||  IFNULL(PLANTA_DESTINO  ,'NULL')  " +
                    "|| ',' ||  IFNULL(DESTINO_CODIGO  ,'NULL')  " +
                    "|| ',''' || IFNULL(DESTINO_DSC,'')  || '''' " +
                    "|| ',''' || IFNULL(EQUIPO_DESCARGA_DESTINO,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_LLEGADA_DESTINO,'')  || '''' " +
                    "|| ',''' || IFNULL(PATENTE_CAMION,'')  || '''' " +
                    "|| ',''' || IFNULL(PATENTE_CARRO,'')  || '''' " +
                    "|| ',''' || IFNULL(RUT_EMPRESA,'')  || '''' " +
                    "|| ',''' || IFNULL(EMP_NOMBRE,'')  || '''' " +
                    "|| ',''' || IFNULL(PROV_NOMBRE_CORTO,'')  || '''' " +
                    "|| ',' ||  IFNULL(PEDIDO  ,'NULL')  " +
                    "|| ',' ||  IFNULL(POSICION  ,'NULL')  " +
                    "|| ',' ||  IFNULL(ESTADO  ,'NULL')  " +
                    "|| ',' ||  IFNULL(AS4REP_ID  ,'NULL')  " +
                    "|| ',''' || IFNULL(RUT_CHOFER,'')  || '''' " +
                    "|| ',''' || IFNULL(NOMBRE_CHOFER,'')  || '''' " +
                    "|| ',' ||  IFNULL(FOLIO_DTE  ,'NULL')  " +
                    "|| ',' ||  IFNULL(OT_ORIGEN  ,'NULL')  " +
                    "|| ',''' || IFNULL(ERROR_GDE,'')  || '''' " +
                    "|| ',''' || IFNULL(CONFIRMA_OT,'')  || '''' " +
                    "|| ',' ||  IFNULL(NRO_CONTRATO  ,'NULL')  " +
                    "|| ',' ||  IFNULL(PRECIO_TRANSPORTE  ,'NULL')  " +
                    "|| ',''' || IFNULL(ENVIADO,'')  || '''' " +
                    "|| ',''' || IFNULL(OT_REDESTINO,'')  || '''' " +
                    "|| ',''' || IFNULL(CONTINGENCIA,'')  || '''' " +
                    "|| ',''' || IFNULL(MOTIVO,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_SERVICIO_ORIGEN,'')  || '''' " +
                    "|| ',''' || IFNULL(VELOCIDAD,'')  || '''' " +
                    "|| ',''' || IFNULL(DISTANCIA,'')  || '''' " +
                    "|| ',''' || IFNULL(LATITUD,'')  || '''' " +
                    "|| ',''' || IFNULL(LONGITUD,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_DESPACHO_REAL,'')  || '''' " +
                    "|| ',''' || IFNULL(TIEMPO_ARRIBO,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_ARRIBO,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_ULTIMO_CALCULO,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_SERVICIO_DESTINO,'')  || '''' " +
                    "|| ',''' || IFNULL(HORA_SALIDA_DESTINO,'')  || '''' " +
                    "|| ',''' || IFNULL(CAUSANTE_ANULACION,'')  || '''' " +
                    "|| ',''' || IFNULL(NOMBRE_GRUA,'')  || '''' " +
                    "|| ',' ||  IFNULL(TIM_ID  ,'NULL')  " +
                    "|| ',''' || IFNULL(ESTADO_FOLIO_DTE,'')  || '''' " +
                    "|| ',' ||  IFNULL(RODAL  ,'NULL')  " +
                    "|| ',''' || IFNULL(DESP_FECHA_BD,'')  || '''' " +
                    "|| ',''' || IFNULL(PROV_RUT_FOLIO,'')  || '''' " +
                    "|| ',''' || IFNULL(PROV_RAZON_SOCIAL,'')  || '''' " +
                    "|| ',''' || IFNULL(FECHA_RECEP,'')  || '''' " +
                    "|| ',' ||  IFNULL(DIFF_HORA_RECEP  ,'NULL')  " +
                    "|| ',' ||  IFNULL(RECEP_ID  ,'NULL')  " +
                    "|| ',''' || IFNULL(CAAC_GEO_SUP_IZQ_X,'')  || '''' " +
                    "|| ',''' || IFNULL(CAAC_GEO_SUP_IZQ_Y,'')  || '''' " +
                    "|| ',''' || IFNULL(CAAC_GEO_INF_DER_X,'')  || '''' " +
                    "|| ',''' || IFNULL(CAAC_GEO_INF_DER_Y,'')  || '''' " +
                    "|| ',' ||  IFNULL(DESP_ANEXO  ,'NULL')  " +
                    "|| ','''||  IFNULL(CLAVE_ASICAM  ,'') || '''' " +
                    "|| ','''||  IFNULL(CUM_ID_ASICAM  ,'') || '''' " +
                    "|| ','''||  IFNULL(PERIODO_ASICAM  ,'') || '''' " +
                    "||  ')' TEXTO " +
                    " FROM DUT_ORDENES_TRANSPORTE " +
                    " WHERE NUMERO_OT = " + OT;

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    res = cursor.getString(cursor.isNull(cursor.getColumnIndexOrThrow("TEXTO")) ? null : cursor.getColumnIndexOrThrow("TEXTO"));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            dbHelper.close();
        }
        return res;
    }


    public OrdenesTransporte getOrdenTransporteById(Context context, String OT) throws Exception {

        OrdenesTransporte ordenesTransporte = null;
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();

            String sql = "SELECT " +
                    "NUMERO_OT " +
                    ",NOMBRE_CHOFER" +
                    ",RUT_CHOFER" +
                    ",PRODUCTO_CODIGO" +
                    ",PRODUCTO_CODIGO_DESC" +
                    ",GRUA" +
                    ",PATENTE_CARRO" +
                    ",HORA_INICIO_VIAJE" +
                    ",ORIGEN_DSC" +
                    ",ORIGEN_CODIGO" +
                    ",ORIGEN_FORMIN" +
                    ",PLANTA_ORIGEN" +
                    ",HORA_LLEGADA_ORIGEN" +
                    ",HORA_SERVICIO_ORIGEN" +
                    ",DESTINO_DSC" +
                    ",DESTINO_CODIGO" +
                    ",DESTINO_FORMIN" +
                    ",CLAVE_ASICAM" +
                    ",HORA_LLEGADA_DESTINO " +
                    ",PLANTA_DESTINO " +
                    ",PROV_RUT_FOLIO " +
                    ",PROV_RAZON_SOCIAL " +
                    ",FECHA_RECEP " +
                    ",DIFF_HORA_RECEP " +
                    ",RECEP_ID " +
                    ",CAAC_GEO_SUP_IZQ_X" +
                    ",CAAC_GEO_SUP_IZQ_Y" +
                    ",CAAC_GEO_INF_DER_X" +
                    ",CAAC_GEO_INF_DER_Y" +
                    ",CUM_ID_ASICAM" +
                    ",FOLIO_DTE " +
                    ",GDE_RODAL " +
                    ",HORA_LLEGADA_ORIGEN_WIFI " +
                    ",HORA_SALIDA_ORIGEN_WIFI " +
                    ",HORA_FECHA_TRANSMISION " +
                    " FROM DUT_ORDENES_TRANSPORTE " +
                    " WHERE NUMERO_OT = " + OT;


            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    ordenesTransporte = new OrdenesTransporte();
                    ordenesTransporte.setNumero_ot(cursor.getInt(cursor.isNull(cursor.getColumnIndexOrThrow("NUMERO_OT")) ? null : cursor.getColumnIndexOrThrow("NUMERO_OT")));
                    ordenesTransporte.setRodal(cursor.isNull(cursor.getColumnIndexOrThrow("GDE_RODAL")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("GDE_RODAL")));
                    ordenesTransporte.setHora_salida_origen_wifi(cursor.isNull(cursor.getColumnIndexOrThrow("HORA_SALIDA_ORIGEN_WIFI")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("HORA_SALIDA_ORIGEN_WIFI")));
                    ordenesTransporte.setHora_llegada_origen_wifi(cursor.isNull(cursor.getColumnIndexOrThrow("HORA_LLEGADA_ORIGEN_WIFI")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("HORA_LLEGADA_ORIGEN_WIFI")));
                    ordenesTransporte.setNombre_chofer(cursor.isNull(cursor.getColumnIndexOrThrow("NOMBRE_CHOFER")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_CHOFER")));
                    ordenesTransporte.setRut_chofer(cursor.isNull(cursor.getColumnIndexOrThrow("RUT_CHOFER")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("RUT_CHOFER")));
                    ordenesTransporte.setProducto_codigo(cursor.isNull(cursor.getColumnIndexOrThrow("PRODUCTO_CODIGO")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PRODUCTO_CODIGO")));
                    ordenesTransporte.setProducto_codigo_desc(cursor.isNull(cursor.getColumnIndexOrThrow("PRODUCTO_CODIGO_DESC")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PRODUCTO_CODIGO_DESC")));
                    ordenesTransporte.setGrua(cursor.isNull(cursor.getColumnIndexOrThrow("GRUA")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("GRUA")));
                    ordenesTransporte.setPatente_carro(cursor.isNull(cursor.getColumnIndexOrThrow("PATENTE_CARRO")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PATENTE_CARRO")));
                    ordenesTransporte.setHora_inicio_viaje(cursor.isNull(cursor.getColumnIndexOrThrow("HORA_INICIO_VIAJE")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("HORA_INICIO_VIAJE")));
                    ordenesTransporte.setOrigen_desc(cursor.isNull(cursor.getColumnIndexOrThrow("ORIGEN_DSC")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("ORIGEN_DSC")));
                    ordenesTransporte.setHora_llegada_origen(cursor.isNull(cursor.getColumnIndexOrThrow("HORA_LLEGADA_ORIGEN")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("HORA_LLEGADA_ORIGEN")));
                    ordenesTransporte.setPlanta_origen(cursor.isNull(cursor.getColumnIndexOrThrow("PLANTA_ORIGEN")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PLANTA_ORIGEN")));
                    ordenesTransporte.setHora_servicio_origen(cursor.isNull(cursor.getColumnIndexOrThrow("HORA_SERVICIO_ORIGEN")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("HORA_SERVICIO_ORIGEN")));
                    ordenesTransporte.setHora_fecha_transmision(cursor.isNull(cursor.getColumnIndexOrThrow("HORA_FECHA_TRANSMISION")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("HORA_FECHA_TRANSMISION")));
                    ordenesTransporte.setDestino_dsc(cursor.isNull(cursor.getColumnIndexOrThrow("DESTINO_DSC")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("DESTINO_DSC")));
                    ordenesTransporte.setDestino_codigo(cursor.isNull(cursor.getColumnIndexOrThrow("DESTINO_CODIGO")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("DESTINO_CODIGO")));
                    ordenesTransporte.setDestino_formin(cursor.isNull(cursor.getColumnIndexOrThrow("DESTINO_FORMIN")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("DESTINO_FORMIN")));
                    ordenesTransporte.setHora_llegada_destino(cursor.isNull(cursor.getColumnIndexOrThrow("HORA_LLEGADA_DESTINO")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("HORA_LLEGADA_DESTINO")));
                    ordenesTransporte.setPlanta_destino(cursor.isNull(cursor.getColumnIndexOrThrow("PLANTA_DESTINO")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PLANTA_DESTINO")));
                    //ordenesTransporte.setPlanta_dsc(cursor.isNull(cursor.getColumnIndexOrThrow("PLANTA_DSC")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PLANTA_DSC")));
                    ordenesTransporte.setProv_rut_folio(cursor.isNull(cursor.getColumnIndexOrThrow("PROV_RUT_FOLIO")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PROV_RUT_FOLIO")));
                    ordenesTransporte.setProv_razon_social(cursor.isNull(cursor.getColumnIndexOrThrow("PROV_RAZON_SOCIAL")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PROV_RAZON_SOCIAL")));
                    /*ordenesTransporte.setPrma_esma_codigo_especie(cursor.isNull(cursor.getColumnIndexOrThrow("PRMA_ESMA_CODIGO_ESPECIE")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PRMA_ESMA_CODIGO_ESPECIE")));
                    ordenesTransporte.setPrma_codigo_presentacion(cursor.isNull(cursor.getColumnIndexOrThrow("PRMA_CODIGO_PRESENTACION")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PRMA_CODIGO_PRESENTACION")));
                    ordenesTransporte.setPrma_nombre(cursor.isNull(cursor.getColumnIndexOrThrow("PRMA_NOMBRE")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("PRMA_NOMBRE")));*/


                    ordenesTransporte.setFechaRecep(cursor.isNull(cursor.getColumnIndexOrThrow("FECHA_RECEP")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("FECHA_RECEP")));
                    ordenesTransporte.setClave_asicam(cursor.isNull(cursor.getColumnIndexOrThrow("CLAVE_ASICAM")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("CLAVE_ASICAM")));
                    ordenesTransporte.setDiferenciaRecep(cursor.isNull(cursor.getColumnIndexOrThrow("DIFF_HORA_RECEP")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("DIFF_HORA_RECEP")));
                    ordenesTransporte.setRecepId(cursor.isNull(cursor.getColumnIndexOrThrow("RECEP_ID")) ? null : cursor.getInt(cursor.getColumnIndexOrThrow("RECEP_ID")));


                    ordenesTransporte.setCaac_geo_sup_izq_x(cursor.isNull(cursor.getColumnIndexOrThrow("CAAC_GEO_SUP_IZQ_X")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("CAAC_GEO_SUP_IZQ_X")));
                    ordenesTransporte.setCaac_geo_sup_izq_y(cursor.isNull(cursor.getColumnIndexOrThrow("CAAC_GEO_SUP_IZQ_Y")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("CAAC_GEO_SUP_IZQ_Y")));
                    ordenesTransporte.setCaac_geo_inf_der_x(cursor.isNull(cursor.getColumnIndexOrThrow("CAAC_GEO_INF_DER_X")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("CAAC_GEO_INF_DER_X")));
                    ordenesTransporte.setCaac_geo_inf_der_y(cursor.isNull(cursor.getColumnIndexOrThrow("CAAC_GEO_INF_DER_Y")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("CAAC_GEO_INF_DER_Y")));
                    ordenesTransporte.setOrigen_codigo(cursor.isNull(cursor.getColumnIndexOrThrow("ORIGEN_CODIGO")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("ORIGEN_CODIGO")));
                    ordenesTransporte.setOrigen_formin(cursor.isNull(cursor.getColumnIndexOrThrow("ORIGEN_FORMIN")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("ORIGEN_FORMIN")));
                    //ordenesTransporte.setCaac_geo_inf_der_y(cursor.isNull(cursor.getColumnIndexOrThrow("CUM_ID_ASICAM")) ? null : cursor.getString(cursor.getColumnIndexOrThrow("CUM_ID_ASICAM")));

                    try  {
                        ordenesTransporte.setFolio_dte(cursor.getInt(cursor.isNull(cursor.getColumnIndexOrThrow("FOLIO_DTE")) ? null : cursor.getColumnIndexOrThrow("FOLIO_DTE")));
                    } catch (Exception e) {

                    }

                    try  {
                        ordenesTransporte.setCum_id_asicam(cursor.getInt(cursor.isNull(cursor.getColumnIndexOrThrow("CUM_ID_ASICAM")) ? null : cursor.getColumnIndexOrThrow("CUM_ID_ASICAM")));
                    } catch (Exception e) {

                    }

                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            dbHelper.close();
        }
        return ordenesTransporte;
    }


    public boolean UpdateChofer(String Rut, String Nombre, String numero_ot, Context context) throws Exception  {


        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("UPDATE  DUT_ORDENES_TRANSPORTE SET " +
                            " RUT_CHOFER = ?, " +
                            " NOMBRE_CHOFER = ? " +
                            " WHERE NUMERO_OT = ?;",
                    new Object[]{
                            Rut , Nombre, numero_ot
                    });
            dbHelper.close();
        }catch (Exception ex){
            dbHelper.close();
            //throw new Exception(ex.getMessage());
            return false;
        }
        return true;
    }

    public void updatePresentacion(Context context, String ot, String fechaRecep, String diferenciaRecep, Integer recepId) {
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("UPDATE  DUT_ORDENES_TRANSPORTE SET " +
                            " FECHA_RECEP = ?, " +
                            " DIFF_HORA_RECEP = ?, " +
                            " RECEP_ID = ? " +
                            " WHERE NUMERO_OT = ?;",
                    new Object[]{
                            fechaRecep, diferenciaRecep, recepId, ot
                    });
            dbHelper.close();
        }catch (Exception ex){
            dbHelper.close();
            ex.printStackTrace();
        }
    }

    public void modificaHoraOrigenWifi(Context context) {
        Date fecha = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        sharedPref =context.getSharedPreferences(Constantes.keyPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fechaEnvioWifi", hourdateFormat.format(fecha));
        editor.commit();

        open();
        try {
            String query = "UPDATE DUT_ORDENES_TRANSPORTE SET HORA_LLEGADA_ORIGEN_WIFI = '" + hourdateFormat.format(fecha) + "'";
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL(query);
            dbHelper.close();
        }catch (Exception ex){
            dbHelper.close();
            ex.printStackTrace();
        }
        close();
    }

    public void coordenadas(Context context) {
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("update  dut_ordenes_transporte set CAAC_GEO_SUP_IZQ_X = '-35.709132',CAAC_GEO_SUP_IZQ_Y = '-73.466703',CAAC_GEO_INF_DER_X = '-40.230267',CAAC_GEO_INF_DER_Y = '-71.068623'");
            dbHelper.close();
        }catch (Exception ex)  {
            dbHelper.close();
            try {
                throw new Exception(ex.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean validaConfirmaOt() throws Exception {

        boolean existeCarroRut = false;
        try {
            String sql = "SELECT count(1) as c FROM DUT_ORDENES_TRANSPORTE " +
                    "WHERE "+
                    "RUT_CHOFER IS NOT NULL "+
                    "AND PATENTE_CARRO IS NOT NULL ";
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst())  {
                while (!cursor.isAfterLast()) {

                    if (cursor.getInt(cursor.getColumnIndexOrThrow("c")) > 0){
                        existeCarroRut = true;
                    }
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            dbHelper.close();
        }
        return existeCarroRut;
    }


    public boolean UpdatePatenteCarro(String numero_ot, String carro, Context context) {
        try {
            dbHelper = new MySQLiteOpenHelper(context);
            db = dbHelper.getWritableDatabase();
            db.execSQL("UPDATE  DUT_ORDENES_TRANSPORTE SET " +
                            " PATENTE_CARRO = ? " +
                            " WHERE NUMERO_OT = ?;",
                    new Object[]{
                            carro , numero_ot
                    });
            dbHelper.close();
        }catch (Exception ex){
            dbHelper.close();
            //throw new Exception(ex.getMessage());
            return false;
        }
        return true;
    }

    public void coordenadas(String caac_geo_sup_izq_x, String caac_geo_sup_izq_y, String caac_geo_inf_der_x, String caac_geo_inf_der_y)
    {
        try {
            db.execSQL("update dut_ordenes_transporte set CAAC_GEO_SUP_IZQ_X = " + caac_geo_sup_izq_x + ", CAAC_GEO_SUP_IZQ_Y =  " + caac_geo_sup_izq_y + ", CAAC_GEO_INF_DER_X = " + caac_geo_inf_der_x + ", CAAC_GEO_INF_DER_Y = " + caac_geo_inf_der_y + "");
        }catch (Exception ex)  {
            try {
                throw new Exception(ex.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
