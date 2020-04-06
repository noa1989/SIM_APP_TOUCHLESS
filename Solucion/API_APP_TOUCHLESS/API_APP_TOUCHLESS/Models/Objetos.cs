using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using GenesysJWT;

namespace API_APP_TOUCHLESS.Models
{

    public class Recepcion
    {

        public string PROVEEDOR_RUT = string.Empty;
        public string PROVEEDOR_DV = string.Empty;
        public string PROVEEDOR_DSC = string.Empty;

        public string CHOFER_RUT = string.Empty;
        public string CHOFER_DV = string.Empty;
        public string CHOFER_DSC = string.Empty;

        public string TRANS_RUT = string.Empty;
        public string TRANS_DV = string.Empty;
        public string TRANS_DSC = string.Empty;

        public string PLANTA_ID = string.Empty;
        public string PLANTA_DSC = string.Empty;

        public string PLANTA_ORIGEN = string.Empty;
        public string CANCHA_ORIGEN = string.Empty;
        public string PLANTA_DESTINO = string.Empty;
        public string CANCHA_DESTINO = string.Empty;

        public string GUIA = string.Empty;
        public string RECEP_ID = string.Empty;
        public string FEC_RECEP_ID = string.Empty;

        public string ESMA_COD = string.Empty;
        public string ESMA_DSC = string.Empty;

        public string PRMA_COD = string.Empty;
        public string PRMA_DSC = string.Empty;

        public string EEPP = string.Empty;

        public string CAMION = string.Empty;
        public string CARRO = string.Empty;
        public string CELULAR = string.Empty;

        public string PEDIDO = string.Empty;
        public string COMUNA_ID = string.Empty;
        public string COMUNA_DSC = string.Empty;
        public string PREDIO_ID = string.Empty;
        public string PREDIO_DSC = string.Empty;
        public string ROL = string.Empty;

        public string PDF417 = string.Empty;
        public string BASE64 = string.Empty;

        public string TICKET_VALDOR = string.Empty;
        public string OT = string.Empty;



    }
    public class VM_Recep
    {

        public Recepcion RECEP = new Recepcion();
        public Usuario USER = new Usuario();

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class Chofer
    {


        public string CHOFER_RUT = string.Empty;
        public string CHOFER_DV = string.Empty;
        public string CHOFER_DSC = string.Empty;

        public string CELULAR = string.Empty;

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;


    }
    public class VM_Chofer
    {

        public Chofer CHOFER = new Chofer();
        public Usuario USER = new Usuario();
        public String RUT = String.Empty;
        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class Proveedor
    {


        public string PROVEEDOR_RUT = string.Empty;
        public string PROVEEDOR_DV = string.Empty;
        public string PROVEEDOR_DSC = string.Empty;


        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;


    }
    public class VM_Proveedor
    {

        public Proveedor PROVEEDOR = new Proveedor();
        public Usuario USER = new Usuario();
        public String RUT = String.Empty;
        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class Destinos
    {

        public string PLANTA = string.Empty;
        public string CANCHA = string.Empty;
        public string CANCHA_EERR = string.Empty;
        public string NOMBRE = string.Empty;
        public string RUT = string.Empty;
        public string GEO_SUP_IZQ_X = string.Empty;
        public string GEO_SUP_IZQ_Y = string.Empty;
        public string GEO_INF_DER_X = string.Empty;
        public string GEO_INF_DER_Y = string.Empty;
        public string TIPO_LUGAR = string.Empty;
        public string UTILIZA_CTAC = string.Empty;


        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class VM_Listas_Destinos
    {

        public Usuario USER = new Usuario();
        public List<Destinos> LISTA = new List<Destinos>();

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class EEPP
    {

        public string PLTA_CODIGO = string.Empty;
        public string ESMA_CODIGO = string.Empty;
        public string ESMA_NOMBRE = string.Empty;
        public string PRMA_CODIGO = string.Empty;
        public string PRMA_NOMBRE = string.Empty;
        public string TIPO_CLASIF = string.Empty;
        public string TIPO_MADERA = string.Empty;
        public string TIPO_ROLLIZO = string.Empty;


        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class VM_Listas_EEPP
    {

        public Usuario USER = new Usuario();
        public List<EEPP> LISTA = new List<EEPP>();

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class CanchaProcedencia
    {

        public string RUT_PROV = string.Empty;
        public string CANCHA = string.Empty;
        public string NOMBRE_CANCHA = string.Empty;
        public string COMU_CODIGO = string.Empty;
        public string COMU_NOMBRE = string.Empty;



        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class VM_Listas_Canchas_Proc
    {

        public Usuario USER = new Usuario();
        public String RUT = String.Empty;
        public String CODIGO = String.Empty;
        public List<CanchaProcedencia> LISTA = new List<CanchaProcedencia>();

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class Pedido
    {

        public string RPCM_PRED_CODIGO = string.Empty;
        public string PRED_DESCRIPCION = string.Empty;
        public string RPCM_ROPR_ROL = string.Empty;



        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class VM_Listas_Pedido
    {

        public Usuario USER = new Usuario();
        public String NRO_CTTM = String.Empty;
        public List<Pedido> LISTA = new List<Pedido>();

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class DatosTerceros
    {

        public string RPCM_PRED_CODIGO = string.Empty;
        public string PRED_DESCRIPCION = string.Empty;
        public string RPCM_ROPR_ROL = string.Empty;



        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class VM_DatosTerceros
    {

        public Usuario USER = new Usuario();
        public String RUT = String.Empty;
        public String GUIA = String.Empty;
        public List<DatosTerceros> LISTA = new List<DatosTerceros>();

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }

    public class Comunas
    {

        public string COMU_CODIGO = string.Empty;
        public string COMU_NOMBRE = string.Empty;

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }
    public class VM_Listas_Comunas
    {

        public Usuario USER = new Usuario();
        public List<Comunas> LISTA = new List<Comunas>();

        public int ERROR_COD = 0;
        public string ERROR_DSC = string.Empty;

    }


}