/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tp;

/**
 *
 * @author Julio
 */
public class Partido {
    private String ronda;
    private Equipo equipo1 ;
    private Equipo equipo2 ;
    private int golesEquipo1;
    private int golesEquipo2;
    private String fase;

    public Partido(String ronda, Equipo equipo1, Equipo equipo2,String fase) {
        this.ronda = ronda;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.fase = fase;
    
    }

    public String getRonda() {
        return ronda;
    }
    
    
     public Equipo getEquipo1() {
        return equipo1;
    }

    public void setEquipo1(Equipo equipo1) {
        this.equipo1 = equipo1;
    }

    public Equipo getEquipo2() {
        return equipo2;
    }

    public void setEquipo2(Equipo equipo2) {
        this.equipo2 = equipo2;
    }

    public int getGolesEquipo1() {
        return golesEquipo1;
    }

    public void setGolesEquipo1(int golesEquipo1) {
        this.golesEquipo1 = golesEquipo1;
    }

    public int getGolesEquipo2() {
        return golesEquipo2;
    }

    public void setGolesEquipo2(int golesEquipo2) {
        this.golesEquipo2 = golesEquipo2;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    
    
    public ResultadoEnum resultado(Equipo equipo){
       if (golesEquipo1 == golesEquipo2){
           return ResultadoEnum.Empate;
       }
        
        if (equipo.getNombre().equals(equipo1.getNombre())) {
           if (golesEquipo1 > golesEquipo2) {
                  return ResultadoEnum.Ganador;
           } else {
               return ResultadoEnum.Perdedor; }
         } else 
           if (golesEquipo2 > golesEquipo1) {
                  return ResultadoEnum.Ganador;
           } else {
               return ResultadoEnum.Perdedor; }
         }  
       }
    


