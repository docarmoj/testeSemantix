package com.teste.semantix.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Teste {

	public static ArrayList<String> list_Host_Data = new ArrayList<String>();
	public static ArrayList<String> list_Codigo_HTTP = new ArrayList<String>();
	public static List<String> list_Bytes = new ArrayList<String>();

	public static void getHostData(String linha) {
		String resultado ="";
		int inicio = linha.indexOf(-1);
		int fim = linha.indexOf(" - - ");
		resultado = linha.substring(inicio + 1, fim);

		list_Host_Data.add(resultado);	
	}

	public static void getBytes(String linha) {
		int inicio = linha.indexOf("\" ");
		String parcial = linha.substring(inicio +2);

		int inicioParcial = parcial.indexOf(-1);
		int fimParcial = parcial.indexOf(" ");

		String resultCodeHTTP = parcial.substring(inicioParcial +1, fimParcial);

		int getCode = parcial.indexOf(" ");
		String bytes = parcial.substring(getCode).replace("-", "");

		list_Codigo_HTTP.add(resultCodeHTTP.replaceAll(" ", ""));

		list_Bytes.add(bytes);
	}

	public static long totalErros404() {
		String erro = "404";
		long contador =0;
		for(String s : list_Codigo_HTTP) {
			if(s.contains(erro)) {
				contador++;
			}
		}
		return contador;
	}

	public static long totalBytes() {
		int contador = 0;
		for(String s : list_Bytes) {
			if(s.startsWith(" ")) {
				s = s.replaceAll(" ", "");
			}
			if(s.contains(" ")) {				
				s = "0";
			}
			if(s.equals("")) {
				s = "0";
			}
			long value = Long.parseLong(s);			
			contador += value;
		}
		return contador;
	}

	public static int totalHostsUnicos() {
		HashSet<String> listaFiltrada = new HashSet<String>(list_Host_Data);  
		return listaFiltrada.size();
	}

	public static void main(String[] args) throws FileNotFoundException {

		File arquivo1 = new File("./access_log_Jul95");
		File arquivo2 = new File("./access_log_Aug95");	
		BufferedReader bf1 = new BufferedReader(new FileReader(arquivo1));
		BufferedReader bf2 = new BufferedReader(new FileReader(arquivo2));
		try {
			String linha1 = "";
			String linha2 = "";
			while ( (linha1 = bf1.readLine() ) != null )  {
				getHostData(linha1);
				getBytes(linha1);
			}
			while ( (linha2 = bf2.readLine() ) != null )  {
				getHostData(linha2);
				getBytes(linha2);
			}
			bf1.close();
			bf2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		System.out.println("Quantidade de erros 404: " + totalErros404());
		System.out.println("Quantidade total de bytes: " + totalBytes());
		System.out.println("Número de hosts únicos: " + totalHostsUnicos());
	}
}
