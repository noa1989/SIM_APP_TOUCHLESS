using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

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
    }
}
