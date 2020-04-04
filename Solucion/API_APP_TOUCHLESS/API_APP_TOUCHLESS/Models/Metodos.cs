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

        public void RetornaIDOferta()
        {

            try
            {

                DataTable DT = new DataTable();

                OracleConnection CON = new OracleConnection(Parametros._STR_CON);
                OracleCommand CMD = new OracleCommand();
                CMD.Connection = CON;
                CMD.CommandText = Parametros._PACKAGE + "SP_RETORNA_ID_OFERTA";
                CMD.CommandType = CommandType.StoredProcedure;


                CMD.Parameters.Add("PN_SIM_SEQ_OFPR_ID", OracleType.Number).Direction = ParameterDirection.Output;

                OracleDataAdapter DA = new OracleDataAdapter(CMD);

                CON.Open();

                CMD.ExecuteNonQuery();

               
                CON.Close();


            }
            catch (Exception EX)
            {


            }


        }


    }
}