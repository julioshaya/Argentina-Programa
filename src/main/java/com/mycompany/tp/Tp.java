/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tp;

/**
 *
 * @author Julio
 * 
 */
    import java.io.IOException;
    import java.nio.file.*;
    import java.sql.*;
    import java.util.*;


public class Tp {

    public static void main(String[] args) throws SQLException {
        
       
             Collection<Partido> partidos = new ArrayList<Partido>();
             Collection<Ronda> rondas = new ArrayList<Ronda>();
             Collection<Fase> fases = new ArrayList<Fase>();
             
             Path pathConfiguracion = Paths.get(args[0]);
             List<String> lineasConfiguracion = null;
    
             try {
               lineasConfiguracion = Files.readAllLines(pathConfiguracion);
                      
             } catch (IOException e){
               System.out.println("No se Pudo Leer el Archivo de Configuraciones");
               System.out.println(e.getMessage());
               System.exit(1);
                 }
     
               String bd="";
               String url="";
               String user="";
               String password="";
               String driver="";
               int PuntosGanador=0;
               int PuntosExtraRonda=0;
               int PuntosExtraFase=0;
               
               //Lee el Archivo Configuracion
               for (String lineaconfiguracion:lineasConfiguracion){
                   if (lineaconfiguracion.contains("bd=")){
                       bd = lineaconfiguracion.substring(3);
                   }
                   
                   if (lineaconfiguracion.contains("url=")){
                       url = lineaconfiguracion.substring(4);
                   }
                   
                   if (lineaconfiguracion.contains("user=")){
                       user= lineaconfiguracion.substring(5);
                   }
                       
                   if (lineaconfiguracion.contains("password=")){
                       password= lineaconfiguracion.substring(9);
                   }
                   
                    if (lineaconfiguracion.contains("driver=")){
                       driver= lineaconfiguracion.substring(7);
                   }
                   
                   if (lineaconfiguracion.contains("PuntosGanador=")){
                       PuntosGanador= Integer.parseInt(lineaconfiguracion.substring(14));
                   }
               
                    if (lineaconfiguracion.contains("PuntosExtraRonda=")){
                       PuntosExtraRonda= Integer.parseInt(lineaconfiguracion.substring(17));
                   }
               
                    if (lineaconfiguracion.contains("PuntosExtraFase=")){
                       PuntosExtraFase= Integer.parseInt(lineaconfiguracion.substring(16));
                   }
               }
             
             
             ConexionBD cn = new ConexionBD(bd,url,user,password,driver);
             cn.conexion();
              
            Statement st = cn.conn.createStatement(); //pronosticos
            Statement rt = cn.conn.createStatement(); //participantes 
            
            //Crea las tablas del Sistema
            //Tabla pronosticos
           
            st.execute("drop table if exists pronosticos;"); 
            st.execute ("create table pronosticos (participante varchar(255)," +
                        "equipo1 varchar(255),gana1 char(1),empata char(1)," + 
                        "gana2 char(1),equipo2 varchar(255))");
             
            // Path pathResultados = Paths.get("src/test/java/resultados.csv");
             Path pathResultados = Paths.get(args[1]);
            
             List<String> lineasResultados = null;
    
             try {
               lineasResultados = Files.readAllLines(pathResultados);
                      
             } catch (IOException e){
               System.out.println("No se Pudo Leer el Archivo Resultados");
               System.out.println(e.getMessage());
               System.exit(1);
                     
                 }
               boolean primera = true;
               String nroronda="";
               String nrofase="";
               String ultronda="";
               String ultfase="";
               int cantpartidosrondas = 0;
               int cantpartidosfases = 0;
               
               for (String lineaResultado:lineasResultados ){
                  
                   if (primera) {
                       primera = false;
                   } else{
                       String[] campos = lineaResultado.split(",");
                       Equipo equipo1 = new Equipo(campos[1]);
                       Equipo equipo2 = new Equipo(campos[4]);
                       nroronda = campos[0];
                       nrofase = campos[5];
                       if (ultronda.isEmpty())  {
                          ultronda = nroronda; }
                       else {
                          if (!nroronda.equals(ultronda)) {
                               Ronda ronda = new Ronda(ultronda,cantpartidosrondas);
                               rondas.add(ronda);
                               cantpartidosrondas = 0;
                               ultronda = "";
                             }       
                         
                         }
                    
                       if (ultfase.isEmpty())  {
                          ultfase = nrofase; }
                       else {
                          if (!nrofase.equals(ultfase)) {
                               Fase fase = new Fase(ultfase,cantpartidosfases);
                               fases.add(fase);
                               cantpartidosfases = 0;
                               ultfase = "";
                              
                             }       
                         
                         }
                         
                            cantpartidosrondas += 1;
                            cantpartidosfases += 1;
                            Partido partido = new Partido(nroronda,equipo1,equipo2,nrofase);
                            partido.setGolesEquipo1(Integer.parseInt(campos[2]));
                            partido.setGolesEquipo2(Integer.parseInt(campos[3]));
                            partidos.add(partido); //Agrego el Partido a la colección
                          
                       }
                       
                     
                  }
                       Ronda ronda = new Ronda(ultronda,cantpartidosrondas);
                       rondas.add(ronda);
                       Fase fase = new Fase(ultfase,cantpartidosfases);
                       fases.add(fase);
                       
               
               
               
             //Inserta Archivo pronostico en tabla Pronosticos
               
             Path pathRegPronosticos = Paths.get(args[2]);
             List<String> regPronosticos = null;

             try {
               regPronosticos = Files.readAllLines(pathRegPronosticos);
                  
             } catch (IOException e){
               System.out.println("No se Pudo Leer el Archivo de Pronósticos");
               System.out.println(e.getMessage());
               System.exit(1);
                     
             }
               primera = true;
               for (String regPronostico:regPronosticos ){
                  
                   if (primera) {
                       primera = false;
                   } else{
                          String[] campos = regPronostico.split(",");
                          st.execute( "insert into pronosticos (participante,equipo1,gana1,empata,gana2,equipo2)" +
                                " values (" + "'" + campos[0] + "'" + "," + "'" + campos[1] + "'" + ","  +
                                "'" + campos[2] + "'" + "," + "'" + campos[3] + "'" + "," + 
                                "'" + campos[4] + "'" + "," + "'" + campos[5] + "'" + ")") ;
               
                   }
               }
               //Calculo de Puntos x Participante
               try {
                 int puntos=0;
                 int puntosRonda=0;
                 int puntosFase=0;
                 int partidosacertadosxronda=0;
                 int partidosacertadosxfase=0;
                 int partidosxronda=0;
                 int partidosxfase=0;
                 String rondaActual="";
                 String faseActual="";
                 String participante="";
                 rt.execute("drop table if exists participantes;"); 
                 rt.execute ("create table participantes (nombre varchar(255)," +
                            "aciertos int,puntosxronda int,puntosxfase int)");  
                
                 
                 ResultSet rs = st.executeQuery("select * from pronosticos order by participante");
                 
                while (rs.next()){
                      if (participante.equals(rs.getString(1)) || participante.isEmpty())  {
                          participante = rs.getString(1);
                      } else {
                                    
                                      Participante jugador = new Participante(participante);
                                      jugador.setAciertos(puntos);
                                      jugador.setPuntosxfase(puntosFase);
                                      jugador.setPuntosxronda(puntosRonda);
                                      rt.execute("insert into participantes (nombre,aciertos,puntosxronda,puntosxfase) values " +
                                                 "(" + "'" + jugador.getNombre() + "'" + "," + jugador.getAciertos() + "," + jugador.getPuntosxronda() + "," +
                                                 jugador.getPuntosxfase() + ")") ;
                                      
                                      
                                      participante = rs.getString(1);
                                      puntos = 0;
                                      puntosRonda = 0;
                                      puntosFase = 0;
                                      partidosacertadosxronda = 0;
                                      partidosacertadosxfase = 0;
                      
                      } 
                          Equipo equipo1 = new Equipo(rs.getString((2)));
                          Equipo equipo2 = new Equipo(rs.getString((6)));
                          Partido partido = null;
                          for (Partido partidoColeccion:partidos){
                             if (partidoColeccion.getEquipo1().getNombre().equals(equipo1.getNombre()) &&
                                  partidoColeccion.getEquipo2().getNombre().equals(equipo2.getNombre())) {
                                            //Busqueda en la Coleccion que coinicidan los 2 equipos
                                            partido = partidoColeccion;
                                            break;
                              }      
                          } 
                          Equipo equipo = null;
                          ResultadoEnum resultado = null ;
                         //Equipo 1 Gana
                          if ("X".equals(rs.getString((3)))) {
                              equipo = equipo1;
                              resultado = ResultadoEnum.Ganador ;
                            }
                         //Empate
                          if ("X".equals(rs.getString((4)))) {
                              equipo = equipo1;
                              resultado = ResultadoEnum.Empate ;
                            }
                        //Equipo 2 Gana
                          if ("X".equals(rs.getString((5)))) 
                            {
                              equipo = equipo2;
                              resultado = ResultadoEnum.Ganador ;
                            }
                                                    
                          Pronostico pronostico = new Pronostico(partido,equipo,resultado,PuntosGanador);
                          puntos += pronostico.puntos();
                          
                          
                          //Calcula los Puntos x Ronda
                          if (rondaActual.isEmpty() || !rondaActual.equals(partido.getRonda())){
                                for (Ronda rondaColleccion:rondas) {
                                   if (rondaColleccion.getNro().equals(partido.getRonda())) {
                                       partidosxronda = rondaColleccion.getPartidos();
                                       partidosacertadosxronda=0;
                                       rondaActual = partido.getRonda();
                                       break;
                                   }

                                }
                          }
                          
                          
                          partidosacertadosxronda += pronostico.aciertos();              
                          if (partidosacertadosxronda == partidosxronda) {
                              puntosRonda += PuntosExtraRonda;
                              rondaActual="";
                          } 
                          
                          
                            //Calcula los Puntos x Fase
                         if (faseActual.isEmpty() || !faseActual.equals(partido.getFase())){
                           for (Fase faseColleccion:fases) {
                             if (faseColleccion.getNro().equals(partido.getFase())) {
                                    partidosxfase = faseColleccion.getPartidos();
                                    partidosacertadosxfase=0;
                                    faseActual = partido.getFase();
                                    break;
                                  }       
                             }
                          }
                           partidosacertadosxfase += pronostico.aciertos();
                           if (partidosacertadosxfase == partidosxfase) {
                              puntosFase += PuntosExtraFase;
                              faseActual="";
                            }
                         
                           //Agrega el último Participante
                           if (rs.isLast()) {
                                      Participante jugador = new Participante(participante);
                                      jugador.setAciertos(puntos);
                                      jugador.setPuntosxfase(puntosFase);
                                      jugador.setPuntosxronda(puntosRonda);
                                      rt.execute("insert into participantes (nombre,aciertos,puntosxronda,puntosxfase) values " +
                                                 "(" + "'" + jugador.getNombre() + "'" + "," + jugador.getAciertos() + "," + jugador.getPuntosxronda() + "," +
                                                 jugador.getPuntosxfase() + ")") ;
                              
                           }
               
                
                }
                
                 //Muestra el Resultado
                 rs = st.executeQuery ("select nombre,aciertos,puntosxronda,puntosxfase,sum(aciertos+puntosxronda+puntosxfase) as total " +
                                                 " from participantes " +
                                                 " group by nombre,aciertos,puntosxronda,puntosxfase" +
                                                 " order by 5 desc,1"  );
                             
               
                 System.out.println("-----------------------------------------------------------------------------------------------------------");
                 System.out.println("|                              Ranking Participantes x Total de Puntos Obtenidos                          |");
                 System.out.println("-----------------------------------------------------------------------------------------------------------");
                 System.out.println("|    Participante   |  Puntos x Aciertos    |   Puntos X Ronda   |  Puntos x Fase  |   Total de Puntos    |");
                 System.out.println("-----------------------------------------------------------------------------------------------------------");
                 while (rs.next()) {
                     String salidaParticipante = String.format("%10s",rs.getString("nombre"));
                     String salidaPuntos = String.format("%20s",rs.getString("aciertos"));
                     String salidaRonda = String.format("%25s",rs.getString("puntosxronda"));
                     String salidaFase = String.format("%19s",rs.getString("puntosxfase"));
                     String salidaTotal = String.format("%20s",rs.getString("total"));
                     String salidaCierre = String.format("%12s","|" );
                     System.out.println("|" + salidaParticipante  +   salidaPuntos +  salidaRonda  + salidaFase + salidaTotal + salidaCierre );
                     System.out.println("-----------------------------------------------------------------------------------------------------------");
                 }
                
               cn.conexion().close();
           } catch (SQLException e) {
                  
               System.out.println("No se Pudo Leer la Tabla Pronósticos");
               System.out.println(e.getMessage());
               System.exit(1);
               cn.conexion().close();
           }
               
        
     }
           
   }
      
      
      
      

