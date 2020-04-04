using System;
using System.Collections.Generic;
using System.Data;
using System.Data.OracleClient;
using System.Linq;
using System.Web;

namespace API_APP_TOUCHLESS.Models
{
    public class Metodos
    {

        public static void SP_CHOFER(VM_Chofer DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_CHOFER";
                CMD.CommandType = CommandType.StoredProcedure;


                CMD.Parameters.Add("PN_RUT", OracleType.Number).Value = DATA.RUT;

                CMD.Parameters.Add("PV_DV", OracleType.VarChar, 100).Direction = ParameterDirection.Output;
                CMD.Parameters.Add("PV_NOMBRE", OracleType.VarChar, 100).Direction = ParameterDirection.Output;
                CMD.Parameters.Add("PV_CELULAR", OracleType.VarChar, 100).Direction = ParameterDirection.Output;


                OracleDataAdapter DA = new OracleDataAdapter(CMD);

                CON.Open();

                CMD.ExecuteNonQuery();

                DATA.CHOFER.CHOFER_RUT = DATA.RUT;
                DATA.CHOFER.CHOFER_DV = Parametros.VALIDA_DBNULL(CMD.Parameters["PV_DV"].Value.ToString());
                DATA.CHOFER.CHOFER_DSC = Parametros.VALIDA_DBNULL(CMD.Parameters["PV_NOMBRE"].Value.ToString());
                DATA.CHOFER.CELULAR = Parametros.VALIDA_DBNULL(CMD.Parameters["PV_CELULAR"].Value.ToString());


                CON.Close();


            }
            catch (Exception EX)
            {


            }


        }

        public static void SP_PROVEEDOR(VM_Proveedor DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_PROVEEDOR";
                CMD.CommandType = CommandType.StoredProcedure;


                CMD.Parameters.Add("PN_RUT", OracleType.Number).Value = DATA.RUT;

                CMD.Parameters.Add("PV_DV", OracleType.VarChar, 100).Direction = ParameterDirection.Output;
                CMD.Parameters.Add("PV_NOMBRE", OracleType.VarChar, 100).Direction = ParameterDirection.Output;


                OracleDataAdapter DA = new OracleDataAdapter(CMD);

                CON.Open();

                CMD.ExecuteNonQuery();

                DATA.PROVEEDOR.PROVEEDOR_RUT = DATA.RUT;
                DATA.PROVEEDOR.PROVEEDOR_DV = Parametros.VALIDA_DBNULL(CMD.Parameters["PV_DV"].Value.ToString());
                DATA.PROVEEDOR.PROVEEDOR_DSC = Parametros.VALIDA_DBNULL(CMD.Parameters["PV_NOMBRE"].Value.ToString());
               

                CON.Close();


            }
            catch (Exception EX)
            {


            }


        }

        public static void SP_DESTINO(VM_Listas_Destinos DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_DESTINO";
                CMD.CommandType = CommandType.StoredProcedure;


                CMD.Parameters.Add("P_CURSOR", OracleType.Cursor).Direction = ParameterDirection.Output;

                OracleDataAdapter DA = new OracleDataAdapter(CMD);



                DA.Fill(DT);

                if (DT.Rows.Count > 0)
                {
                    foreach (DataRow ITEM in DT.Rows)
                    {
                        Destinos ROW = new Destinos();


                        ROW.PLANTA = ITEM["PLANTA"].ToString();
                        ROW.CANCHA = ITEM["CANCHA"].ToString();
                        ROW.CANCHA_EERR = ITEM["CANCHA_EERR"].ToString();
                        ROW.NOMBRE = ITEM["NOMBRE"].ToString();
                        ROW.RUT = ITEM["RUT"].ToString();
                        ROW.GEO_SUP_IZQ_X = ITEM["GEO_SUP_IZQ_X"].ToString();
                        ROW.GEO_SUP_IZQ_Y = ITEM["GEO_SUP_IZQ_Y"].ToString();
                        ROW.GEO_INF_DER_X = ITEM["GEO_INF_DER_X"].ToString();
                        ROW.GEO_INF_DER_Y = ITEM["GEO_INF_DER_Y"].ToString();
                        ROW.TIPO_LUGAR = ITEM["TIPO_LUGAR"].ToString();
                        ROW.UTILIZA_CTAC = ITEM["UTILIZA_CTAC"].ToString();

                        DATA.LISTA.Add(ROW);

                    }

                }

            }
            catch (Exception EX)
            {


            }
        }

        public static void SP_EEPP(VM_Listas_EEPP DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_EEPP";
                CMD.CommandType = CommandType.StoredProcedure;


                CMD.Parameters.Add("P_CURSOR", OracleType.Cursor).Direction = ParameterDirection.Output;

                OracleDataAdapter DA = new OracleDataAdapter(CMD);



                DA.Fill(DT);

                if (DT.Rows.Count > 0)
                {
                    foreach (DataRow ITEM in DT.Rows)
                    {
                        EEPP ROW = new EEPP();


                        ROW.PLTA_CODIGO = ITEM["PLTA_CODIGO"].ToString();
                        ROW.ESMA_CODIGO = ITEM["ESMA_CODIGO"].ToString();
                        ROW.ESMA_NOMBRE = ITEM["ESMA_NOMBRE"].ToString();
                        ROW.PRMA_CODIGO = ITEM["PRMA_CODIGO"].ToString();
                        ROW.PRMA_NOMBRE = ITEM["PRMA_NOMBRE"].ToString();
                        ROW.TIPO_CLASIF = ITEM["TIPO_CLASIF"].ToString();
                        ROW.TIPO_MADERA = ITEM["TIPO_MADERA"].ToString();
                        ROW.TIPO_ROLLIZO = ITEM["TIPO_ROLLIZO"].ToString();

                        DATA.LISTA.Add(ROW);

                    }

                }

            }
            catch (Exception EX)
            {


            }
        }

        public static void SP_CANCHA_PROCEDENCIA(VM_Listas_Canchas_Proc DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_CANCHA_PROCEDENCIA";
                CMD.CommandType = CommandType.StoredProcedure;

                CMD.Parameters.Add("PN_RUT", OracleType.Number).Value = DATA.RUT;
                CMD.Parameters.Add("PN_CODIGO", OracleType.Number).Value = DATA.CODIGO;


                CMD.Parameters.Add("P_CURSOR", OracleType.Cursor).Direction = ParameterDirection.Output;

                OracleDataAdapter DA = new OracleDataAdapter(CMD);



                DA.Fill(DT);

                if (DT.Rows.Count > 0)
                {
                    foreach (DataRow ITEM in DT.Rows)
                    {
                        CanchaProcedencia ROW = new CanchaProcedencia();


                        ROW.RUT_PROV = ITEM["RUT_PROV"].ToString();
                        ROW.CANCHA = ITEM["CANCHA"].ToString();
                        ROW.NOMBRE_CANCHA = ITEM["NOMBRE_CANCHA"].ToString();
                        ROW.COMU_CODIGO = ITEM["COMU_CODIGO"].ToString();
                        ROW.COMU_NOMBRE = ITEM["COMU_NOMBRE"].ToString();


                        DATA.LISTA.Add(ROW);

                    }

                }

            }
            catch (Exception EX)
            {


            }
        }

        public static void SP_PEDIDO_COMPRA(VM_Listas_Pedido DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_PEDIDO_COMPRA";
                CMD.CommandType = CommandType.StoredProcedure;

                CMD.Parameters.Add("PN_NRO_CTTM", OracleType.Number).Value = DATA.NRO_CTTM;


                CMD.Parameters.Add("P_CURSOR", OracleType.Cursor).Direction = ParameterDirection.Output;

                OracleDataAdapter DA = new OracleDataAdapter(CMD);



                DA.Fill(DT);

                if (DT.Rows.Count > 0)
                {
                    foreach (DataRow ITEM in DT.Rows)
                    {
                        Pedido ROW = new Pedido();


                        ROW.RPCM_PRED_CODIGO = ITEM["RPCM_PRED_CODIGO"].ToString();
                        ROW.PRED_DESCRIPCION = ITEM["PRED_DESCRIPCION"].ToString();
                        ROW.RPCM_ROPR_ROL = ITEM["RPCM_ROPR_ROL"].ToString();
   

                        DATA.LISTA.Add(ROW);

                    }

                }

            }
            catch (Exception EX)
            {


            }
        }

        public static void SP_COMUNAS(VM_Listas_Comunas DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_COMUNAS";
                CMD.CommandType = CommandType.StoredProcedure;


                CMD.Parameters.Add("P_CURSOR", OracleType.Cursor).Direction = ParameterDirection.Output;

                OracleDataAdapter DA = new OracleDataAdapter(CMD);



                DA.Fill(DT);

                if (DT.Rows.Count > 0)
                {
                    foreach (DataRow ITEM in DT.Rows)
                    {
                        Comunas ROW = new Comunas();


                        ROW.COMU_CODIGO = ITEM["COMU_CODIGO"].ToString();
                        ROW.COMU_NOMBRE = ITEM["COMU_NOMBRE"].ToString();

                        DATA.LISTA.Add(ROW);

                    }

                }

            }
            catch (Exception EX)
            {


            }
        }


        public static void SP_DATOS_TERCEROS(VM_DatosTerceros DATA)
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_DATOS_TERCEROS";
                CMD.CommandType = CommandType.StoredProcedure;

                CMD.Parameters.Add("PN_RUT", OracleType.Number).Value = DATA.RUT;
                CMD.Parameters.Add("PN_GUIA", OracleType.Number).Value = DATA.GUIA;


                CMD.Parameters.Add("P_CURSOR", OracleType.Cursor).Direction = ParameterDirection.Output;

                OracleDataAdapter DA = new OracleDataAdapter(CMD);



                DA.Fill(DT);

                if (DT.Rows.Count > 0)
                {
                    foreach (DataRow ITEM in DT.Rows)
                    {
                        DatosTerceros ROW = new DatosTerceros();


                        ROW.RPCM_PRED_CODIGO = ITEM["RPCM_PRED_CODIGO"].ToString();
                        ROW.PRED_DESCRIPCION = ITEM["PRED_DESCRIPCION"].ToString();
                        ROW.RPCM_ROPR_ROL = ITEM["RPCM_ROPR_ROL"].ToString();


                        DATA.LISTA.Add(ROW);

                    }

                }

            }
            catch (Exception EX)
            {


            }
        }


    }
}