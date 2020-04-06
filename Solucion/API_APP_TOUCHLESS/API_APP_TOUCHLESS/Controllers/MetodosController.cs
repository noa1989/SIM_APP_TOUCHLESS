using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using API_APP_TOUCHLESS.Models;

namespace API_APP_TOUCHLESS.Controllers
{
    public class MetodosController : ApiController
    {
        [HttpPost]
        public HttpResponseMessage Inicio()
        {
            List<string> lista = new List<string>();

            lista.Add("1");
            lista.Add("2");
            lista.Add("3");
            lista.Add("4");

            return Request.CreateResponse(HttpStatusCode.OK, lista);
        }

        [HttpPost]
        public HttpResponseMessage SP_WS_CHOFER(VM_Chofer DATA)
        {

            if (DATA == null) {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }

            Metodos.SP_WS_CHOFER(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }


        [HttpPost]
        public HttpResponseMessage SP_WS_PROVEEDOR(VM_Proveedor DATA)
        {
            if (DATA == null)
            {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }
            Metodos.SP_WS_PROVEEDOR(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }



        [HttpPost]
        public HttpResponseMessage SP_WS_COMUNAS(VM_Listas_Comunas DATA)
        {
            if (DATA == null)
            {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }
            Metodos.SP_WS_COMUNAS(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }

        [HttpPost]
        public HttpResponseMessage SP_WS_DESTINO(VM_Listas_Destinos DATA)
        {
            if (DATA == null)
            {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }
            Metodos.SP_WS_DESTINO(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }

        [HttpPost]
        public HttpResponseMessage SP_WS_EEPP(VM_Listas_EEPP DATA)
        {
            if (DATA == null)
            {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }
            Metodos.SP_WS_EEPP(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }

        [HttpPost]
        public HttpResponseMessage SP_WS_CANCHA_PROCEDENCIA(VM_Listas_Canchas_Proc DATA)
        {
            if (DATA == null)
            {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }
            Metodos.SP_WS_CANCHA_PROCEDENCIA(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }

        [HttpPost]
        public HttpResponseMessage SP_WS_PEDIDO_COMPRA(VM_Listas_Pedido DATA)
        {

            if (DATA == null)
            {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }
            Metodos.SP_WS_PEDIDO_COMPRA(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }

        [HttpPost]
        public HttpResponseMessage SP_WS_DATOS_GDE(VM_Recep DATA)
        {
            if (DATA == null)
            {

                DATA.ERROR_COD = 1;
                DATA.ERROR_DSC = "Objeto en el body esta ingresando nulo";

                return Request.CreateResponse(HttpStatusCode.BadRequest, DATA);
            }

            Metodos.SP_WS_DATOS_GDE(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }

        [HttpPost]
        public HttpResponseMessage SP_REGISTRO_PRESENTACION(VM_Recep DATA)
        {
            Metodos.SP_REGISTRO_PRESENTACION(DATA);

            return Request.CreateResponse(HttpStatusCode.OK, DATA);
        }
    }
}
