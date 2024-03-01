package Application;

import Resources.AfisareImagini;
import Resources.ImagineSpecifica;

public class MyMain {


	public static void main(String[] args) {

		long startTime, endTime;		// declararea variabilelor pentru a masura timpul de procesare pentru diferite etape
		AfisareImagini abs = new AfisareImagini(); // crearea unui obiect de tipul AfisareImagini in care se vor incarca imaginile din folderul Images
		abs.DispunereImagini();// Afisarea imaginilor din folderul Images prin interfata grafica

		ImagineSpecifica imagineSpecifica = new ImagineSpecifica(); // crearea unui obiect de tipul ImagineSpecifica in care salvez imaginea aleasa de utilizator din interfata

		Thread thread = new Thread(imagineSpecifica); //Creez un thread pentru a putea procesa imaginea aleasa de utilizator
		thread.start(); // pornesc thread-ul

		try {
			thread.join(); // astept ca thread-ul sa se termine
		} catch (InterruptedException e) { 
			Thread.currentThread().interrupt();//intrerup thread-ul daca are loc vreao eroare
			System.out.println("Main thread interrupted."); //afisez un mesaj lamuritor in cazul unei erori
		}

		// After the image processing is done, you can display the image
		startTime = System.currentTimeMillis();
		imagineSpecifica.decreaseDepth();   //prelucrez imaginea aleasa de utilizator
		endTime = System.currentTimeMillis();
		System.out.println("Timpul de procesare a imaginii este de: " + (endTime - startTime) + " milisecunde."); //afisez timp proceasare imagine
		imagineSpecifica.display(); // afisez imaginea prelucrata tot prin intermediul interfetei grafice
		
		imagineSpecifica.save(); // salvez imaginea procesata in folderul ales de utilizator
	}
	
	
	

}


