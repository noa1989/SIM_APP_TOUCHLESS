using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;

namespace API_APP_TOUCHLESS.Models
{
    public class Parametros
    {

        public const string _STR_CON = "Data Source=IBMUDESA;User Id=sam;Password=sam;Pooling=no;Pooling=false;Connection Lifetime=60;";
        public const int _TIEMPO_EXPIRA_SESION = 120;
        public const string _PACKAGE = "SAM.SIM_APP_TOUCHLESS.";

        public static object VALIDA_NULO(object ENTRADA)
        {


            if (ENTRADA == null || ENTRADA.ToString() == "null" || ENTRADA.ToString() == "0" || ENTRADA.ToString() == string.Empty)
            {

                return DBNull.Value;

            }
            else
            {

                return ENTRADA.ToString();

            }



        }

        public static object VALIDA_DBNULL(object ENTRADA)
        {


            if (ENTRADA == DBNull.Value )
            {

                return string.Empty ;

            }
            else
            {

                return ENTRADA.ToString();

            }



        }
        public static string Base64Encode(string plainText)
        {
            var plainTextBytes = System.Text.Encoding.UTF8.GetBytes(plainText);
            return System.Convert.ToBase64String(plainTextBytes);
        }

        public static string Base64Decode(string base64EncodedData)
        {
            var base64EncodedBytes = System.Convert.FromBase64String(base64EncodedData);
            return System.Text.Encoding.UTF8.GetString(base64EncodedBytes);
        }
    }
}





