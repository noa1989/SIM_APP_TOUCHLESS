using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Web;
using API_APP_TOUCHLESS.Models;

namespace GenesysJWT
{


    public class Usuario
    {

        //public String USER = string.Empty;
        //public String PASS = string.Empty;
        //public String ROL = string.Empty;
        //public String ROL_COD = string.Empty;


        public String TOKEN = string.Empty;
        public String CAMION = string.Empty;
        public String CARRO = string.Empty;
        public String NOMBRE = string.Empty;
        public String RUT = string.Empty;


        public int ERROR_ID = 0;
        public string ERROR_DSC = string.Empty;


    }


    public class JWT
    {

        private static string Secret = "QVBQX1RPVUNITEVTUw==";
        public static ClaimsPrincipal GetPrincipal(string token)
        {
            try
            {
                JwtSecurityTokenHandler tokenHandler = new JwtSecurityTokenHandler();
                JwtSecurityToken jwtToken = (JwtSecurityToken)tokenHandler.ReadToken(token);
                if (jwtToken == null)
                    return null;
                byte[] key = Convert.FromBase64String(Secret);
                TokenValidationParameters parameters = new TokenValidationParameters()
                {
                    RequireExpirationTime = true,
                    ValidateIssuer = false,
                    ValidateAudience = false,
                    IssuerSigningKey = new SymmetricSecurityKey(key)
                };
                SecurityToken securityToken;
                ClaimsPrincipal principal = tokenHandler.ValidateToken(token,
                      parameters, out securityToken);
                return principal;
            }
            catch
            {
                return null;
            }
        }


        public string GeneraToken(Usuario USER)
        {

            DateTime expiracion = DateTime.UtcNow.AddMinutes(Parametros._TIEMPO_EXPIRA_SESION);

            byte[] key = Convert.FromBase64String(Secret);
            SymmetricSecurityKey securityKey = new SymmetricSecurityKey(key);
            SecurityTokenDescriptor descriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[] {
                      new Claim(ClaimTypes.Name, USER.CAMION),new Claim(ClaimTypes.Surname, USER.CARRO),new Claim(ClaimTypes.SerialNumber, USER.RUT), new Claim(ClaimTypes.Expiration, expiracion.ToString())}),
                Expires = expiracion,
                SigningCredentials = new SigningCredentials(securityKey,
                SecurityAlgorithms.HmacSha256Signature)
            };

            JwtSecurityTokenHandler handler = new JwtSecurityTokenHandler();
            JwtSecurityToken token = handler.CreateJwtSecurityToken(descriptor);
            return handler.WriteToken(token);
        }

        public string GeneraToken(Usuario USER, int minutos)
        {

            DateTime expiracion = DateTime.UtcNow.AddMinutes(minutos);

            byte[] key = Convert.FromBase64String(Secret);
            SymmetricSecurityKey securityKey = new SymmetricSecurityKey(key);
            SecurityTokenDescriptor descriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[] {
                      new Claim(ClaimTypes.Name, USER.CAMION),new Claim(ClaimTypes.Surname, USER.CARRO),new Claim(ClaimTypes.SerialNumber, USER.RUT), new Claim(ClaimTypes.Expiration, expiracion.ToString())}),
                Expires = expiracion,
                SigningCredentials = new SigningCredentials(securityKey,
                SecurityAlgorithms.HmacSha256Signature)
            };

            JwtSecurityTokenHandler handler = new JwtSecurityTokenHandler();
            JwtSecurityToken token = handler.CreateJwtSecurityToken(descriptor);
            return handler.WriteToken(token);
        }


        public bool ValidaToken(Usuario USER, int minutos)
        {

            if (USER.TOKEN is null || USER.TOKEN == string.Empty) {

                return false;

            }

            string Camion = null;
            string Carro = null;
            string Rut = null;
            ClaimsPrincipal principal = GetPrincipal(USER.TOKEN);
            if (principal == null)
                return false;
            ClaimsIdentity identity = null;
            try
            {
                identity = (ClaimsIdentity)principal.Identity;
            }
            catch (NullReferenceException)
            {
                return false;
            }
            Claim CamionClaim = identity.FindFirst(ClaimTypes.Name);
            Camion = CamionClaim.Value;
            Claim CarroClaim = identity.FindFirst(ClaimTypes.Surname);
            Carro = CarroClaim.Value;
            Claim RutClaim = identity.FindFirst(ClaimTypes.SerialNumber);
            Rut = RutClaim.Value;

            Claim expiraClaim = identity.FindFirst(ClaimTypes.Expiration);
            string expira = null;
            expira = expiraClaim.Value;
            DateTime EXPIRACION = DateTime.Parse(expira);
            //DateTime AHORA = DateTime.UtcNow.AddMinutes(minutos);
            DateTime AHORA = DateTime.UtcNow;



            if (EXPIRACION > AHORA && Camion == USER.CAMION && Carro == USER.CARRO && Rut == USER.RUT)
            {

                return true;

            }
            else
            {

                return false;


            }
        }



    }


}
