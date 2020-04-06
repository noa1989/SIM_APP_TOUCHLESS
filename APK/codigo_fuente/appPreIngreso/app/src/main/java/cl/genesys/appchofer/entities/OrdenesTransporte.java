package cl.genesys.appchofer.entities;

import android.content.Intent;

import java.util.Date;

public class OrdenesTransporte {

    private Number numero_ot;
    private Number folio_dte;
    private String rut_chofer;
    private String nombre_chofer;
    private String producto_codigo;
    private String producto_codigo_desc;

    private String grua;
    private String patente_carro;
    private String hora_inicio_viaje;
    private String origen_desc;
    private String origen_codigo;
    private String origen_formin;
    private String hora_llegada_origen;

    private String destino_dsc;
    private String hora_llegada_destino;
    private String prov_nombre_corto;
    private String hora_servicio_origen;

    private String planta_destino;
    private String planta_origen;
    private String prov_rut_folio;
    private String prov_razon_social;

    private String fechaRecep;
    private Integer recepId;
    private String diferenciaRecep;

    private String destino_codigo;
    private String destino_formin;
    private String patente_camion;


    private Integer cum_id_asicam;
    private String clave_asicam;
    private String periodo_asicam;


    private String caac_geo_sup_izq_x;
    private String caac_geo_sup_izq_y;
    private String caac_geo_inf_der_x;
    private String caac_geo_inf_der_y;

    private String hora_llegada_origen_wifi;
    private String hora_fecha_transmision;
    private String hora_salida_origen_wifi;

    private Date inicio_carga;
    private Date fin_carga;

    private String gde_ruma;
    private String rodal;
    private String gde_anho_plantacion;

    public OrdenesTransporte() {

    }

    public String getGde_ruma() {
        return gde_ruma;
    }

    public void setGde_ruma(String gde_ruma) {
        this.gde_ruma = gde_ruma;
    }

    public String getGde_anho_plantacion() {
        return gde_anho_plantacion;
    }

    public void setGde_anho_plantacion(String gde_anho_plantacion) {
        this.gde_anho_plantacion = gde_anho_plantacion;
    }

    public String getOrigen_codigo() {
        return origen_codigo;
    }

    public void setOrigen_codigo(String origen_codigo) {
        this.origen_codigo = origen_codigo;
    }

    public String getDestino_codigo() {
        return destino_codigo;
    }

    public void setDestino_codigo(String destino_codigo) {
        this.destino_codigo = destino_codigo;
    }

    public String getPatente_camion() {
        return patente_camion;
    }

    public void setPatente_camion(String patente_camion) {
        this.patente_camion = patente_camion;
    }

    public Date getInicio_carga() {
        return inicio_carga;
    }

    public void setInicio_carga(Date inicio_carga) {
        this.inicio_carga = inicio_carga;
    }

    public Date getFin_carga() {
        return fin_carga;
    }

    public void setFin_carga(Date fin_carga) {
        this.fin_carga = fin_carga;
    }

    public String getFechaRecep() {
        return fechaRecep;
    }

    public void setFechaRecep(String fechaRecep) {
        this.fechaRecep = fechaRecep;
    }

    public Integer getRecepId() {
        return recepId;
    }

    public void setRecepId(Integer recepId) {
        this.recepId = recepId;
    }

    public String getDiferenciaRecep() {
        return diferenciaRecep;
    }

    public void setDiferenciaRecep(String  diferenciaRecep) {
        this.diferenciaRecep = diferenciaRecep;
    }

    public String getCaac_geo_sup_izq_x() {
        return caac_geo_sup_izq_x;
    }

    public void setCaac_geo_sup_izq_x(String caac_geo_sup_izq_x) {
        this.caac_geo_sup_izq_x = caac_geo_sup_izq_x;
    }

    public String getCaac_geo_sup_izq_y() {
        return caac_geo_sup_izq_y;
    }

    public void setCaac_geo_sup_izq_y(String caac_geo_sup_izq_y) {
        this.caac_geo_sup_izq_y = caac_geo_sup_izq_y;
    }

    public String getCaac_geo_inf_der_x() {
        return caac_geo_inf_der_x;
    }

    public void setCaac_geo_inf_der_x(String caac_geo_inf_der_x) {
        this.caac_geo_inf_der_x = caac_geo_inf_der_x;
    }

    public String getCaac_geo_inf_der_y() {
        return caac_geo_inf_der_y;
    }

    public void setCaac_geo_inf_der_y(String caac_geo_inf_der_y) {
        this.caac_geo_inf_der_y = caac_geo_inf_der_y;
    }

    public String getProv_razon_social() {
        return prov_razon_social;
    }

    public void setProv_razon_social(String prov_razon_social) {
        this.prov_razon_social = prov_razon_social;
    }


    public String getProv_rut_folio() {
        return prov_rut_folio;
    }

    public void setProv_rut_folio(String prov_rut_folio) {
        this.prov_rut_folio = prov_rut_folio;
    }

    public String getPlanta_destino() {
        return planta_destino;
    }

    public void setPlanta_destino(String planta_destino) {
        this.planta_destino = planta_destino;
    }

   /* public String getPlanta_dsc() {
        return planta_dsc;
    }

    public void setPlanta_dsc(String planta_dsc) {
        this.planta_dsc = planta_dsc;
    }
*/

    public Number getNumero_ot() {
        return numero_ot;
    }


    public void setNumero_ot(Number numero_ot) {
        this.numero_ot = numero_ot;
    }

    public Number getFolio_dte() {
        return folio_dte;
    }

    public void setFolio_dte(Number folio_dte) {
        this.folio_dte = folio_dte;
    }

    public String getRut_chofer() {
        return rut_chofer;
    }

    public void setRut_chofer(String rut_chofer) {
        this.rut_chofer = rut_chofer;
    }

    public String getNombre_chofer() {
        return nombre_chofer;
    }

    public void setNombre_chofer(String nombre_chofer) {
        this.nombre_chofer = nombre_chofer;
    }

    public String getProducto_codigo() {
        return producto_codigo;
    }

    public void setProducto_codigo(String producto_codigo) {
        this.producto_codigo = producto_codigo;
    }

    public String getGrua() {
        return grua;
    }

    public void setGrua(String grua) {
        this.grua = grua;
    }

    public String getPatente_carro() {
        return patente_carro;
    }

    public void setPatente_carro(String patente_carro) {
        this.patente_carro = patente_carro;
    }

    public String getHora_inicio_viaje() {
        return hora_inicio_viaje;
    }

    public void setHora_inicio_viaje(String hora_inicio_viaje) {
        this.hora_inicio_viaje = hora_inicio_viaje;
    }

    public String getOrigen_desc() {
        return origen_desc;
    }

    public void setOrigen_desc(String origen_desc) {
        this.origen_desc = origen_desc;
    }

    public String getHora_llegada_origen() {
        return hora_llegada_origen;
    }

    public void setHora_llegada_origen(String hora_llegada_origen) {
        this.hora_llegada_origen = hora_llegada_origen;
    }

    public String getDestino_dsc() {
        return destino_dsc;
    }

    public void setDestino_dsc(String destino_dsc) {
        this.destino_dsc = destino_dsc;
    }

    public String getHora_llegada_destino() {
        return hora_llegada_destino;
    }

    public void setHora_llegada_destino(String hora_llegada_destino) {
        this.hora_llegada_destino = hora_llegada_destino;
    }

    public String getProv_nombre_corto() {
        return prov_nombre_corto;
    }

    public void setProv_nombre_corto(String prov_nombre_corto) {
        this.prov_nombre_corto = prov_nombre_corto;
    }

    public String getHora_servicio_origen() {
        return hora_servicio_origen;
    }

    public void setHora_servicio_origen(String hora_servicio_origen) {
        this.hora_servicio_origen = hora_servicio_origen;
    }

    public Integer getCum_id_asicam() {
        return cum_id_asicam;
    }

    public void setCum_id_asicam(Integer cum_id_asicam) {
        this.cum_id_asicam = cum_id_asicam;
    }

    public String getPlanta_origen() {
        return planta_origen;
    }

    public void setPlanta_origen(String planta_origen) {
        this.planta_origen = planta_origen;
    }

    public String getClave_asicam() {
        return clave_asicam;
    }

    public void setClave_asicam(String clave_asicam) {
        this.clave_asicam = clave_asicam;
    }

    public String getHora_llegada_origen_wifi() {
        return hora_llegada_origen_wifi;
    }

    public void setHora_llegada_origen_wifi(String hora_llegada_origen_wifi) {
        this.hora_llegada_origen_wifi = hora_llegada_origen_wifi;
    }

    public String getHora_salida_origen_wifi() {
        return hora_salida_origen_wifi;
    }

    public void setHora_salida_origen_wifi(String hora_salida_origen_wifi) {
        this.hora_salida_origen_wifi = hora_salida_origen_wifi;
    }

    public String getRodal() {
        return rodal;
    }

    public void setRodal(String rodal) {
        this.rodal = rodal;
    }

    public String getPeriodo_asicam() {
        return periodo_asicam;
    }

    public void setPeriodo_asicam(String periodo_asicam) {
        this.periodo_asicam = periodo_asicam;
    }

    public String getProducto_codigo_desc() {
        return producto_codigo_desc;
    }

    public void setProducto_codigo_desc(String producto_codigo_desc) {
        this.producto_codigo_desc = producto_codigo_desc;
    }

    public String getOrigen_formin() {
        return origen_formin;
    }

    public void setOrigen_formin(String origen_formin) {
        this.origen_formin = origen_formin;
    }

    public String getDestino_formin() {
        return destino_formin;
    }

    public void setDestino_formin(String destino_formin) {
        this.destino_formin = destino_formin;
    }

    public String getHora_fecha_transmision() {
        return hora_fecha_transmision;
    }

    public void setHora_fecha_transmision(String hora_fecha_transmision) {
        this.hora_fecha_transmision = hora_fecha_transmision;
    }
}
