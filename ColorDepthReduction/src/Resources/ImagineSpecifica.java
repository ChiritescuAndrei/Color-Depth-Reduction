package Resources;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagineSpecifica extends Imagine implements ProcesareImagine, Runnable {  //ImagineSpecifica este un tip de imagine care implementeaza interfata ProcesareImagine si este un thread 
																					   // si mosteneste clasa abstracta Imagine
	BufferedImage imagineAleasa;  // imaginea aleasa de utilizator
	String folderSalvare; // folderul in care utilizatorul doreste sa salveze imaginea prelucrata
	String fisierSalvare; // numele sub care utilizatorul doreste sa salveze imaginea prelucrata
	
	{
		System.out.println("-----------Imagine a fost aleasa cu succes.-----------------"); // mesaj de informare care apare inainte de a incepe procesarea imaginii
	}

	@Override
	public void run() {  // metoda run() este suprascrisa deoarece ImagineSpecifica este un thread
		try {
			processImage(); // procesarea imaginii alese de utilizator
		} catch (InterruptedException e) {// in cazul in care apare o eroare, thread-ul este intrerupt
			Thread.currentThread().interrupt(); // intrerup thread-ul daca are loc vreo eroare
			System.out.println("Thread interrupted."); // afisez un mesaj lamuritor in cazul unei erori
		}
	}

	private void processImage() throws InterruptedException {
        waitForImage(); // asteptarea imaginii alese de utilizator

		synchronized (imaginePropriuZisa) { // sincronizarea thread-ului pentru a putea procesa imaginea aleasa de utilizator
			imagineAleasa = imaginePropriuZisa; // imaginea aleasa de utilizator este salvata in imagineAleasa
			Thread.sleep(1000); // simularea procesarii imaginii
            System.out.println("CONSUMER: Imagine incarcata cu succes"); // mesaj de informare care apare dupa ce imaginea a fost procesata
        }
	}

	public ImagineSpecifica() { // constructorul clasei
		super(); // apelarea constructorului din clasa parinte
	}

	

	@Override
	public void decreaseDepth() { // metoda de prelucrare a imaginii alese de utilizator
		if (imagineAleasa != null) { // verific daca imaginea a fost aleasa de utilizator
			int width = imagineAleasa.getWidth(); // preiau latimea imaginii
			int height = imagineAleasa.getHeight(); //  preiau inaltimea imaginii
			int reducere = intrebarePrelucrare(); // intreb utilizatorul cu ce nivel de reducere al culorii doreste sa prelucrez imaginea

			if (reducere == 1) { // in functie de raspunsul utilizatorului, imaginea va fi prelucrata cu un nivel mai intens sau mai putin intens
				reducere = 2;	// aceasta logica este folosita pentru ca functia de decreaseDepth() functioneaza mai bine cu parametrii  2 si 4
			} else {
				reducere = 4;
			}


		        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); // creez o noua imagine cu aceleasi dimensiuni ca si imaginea aleasa de utilizator

		       
		        for (int y = 0; y < height; y++) {  // parcurg imaginea pixel cu pixel
		            for (int x = 0; x < width; x++) {
						int grayValue = imagineAleasa.getRGB(x, y) & 0xFF; // extrag valoarea grayscale a pixelului

		                // Apply color depth reduction algorithm here
						int newGrayValue = (grayValue / (256 / reducere)) * (256 / reducere); // aplic algoritmul de reducere a culorii

		               
		                int newPixel = (newGrayValue << 16) | (newGrayValue << 8) | newGrayValue; // creez noul pixel cu intensitatea grayscale redusa

		                newImage.setRGB(x, y, newPixel); // setez noul pixel in noua imagine
		            }
		        }

				this.imagineAleasa = newImage; // imaginea aleasa de utilizator este inlocuita cu imaginea prelucrata

		    } else {
		        System.out.println("No image loaded to decrease depth."); // mesaj de eroare in cazul in care imaginea este null
		    }
    }



	

	@Override
	public void display() { // metoda de afisare a imaginii prelucrate
		imagesWindow.setVisible(false); // inchid fereastra cu imaginile din folderul Images
		Image imagineSchimbata = imagineAleasa.getScaledInstance(500, 500, Image.SCALE_SMOOTH); // redimensionez imaginea prelucrata pentru a fi afisata in interfata grafica
		ImageIcon icon = new ImageIcon(imagineSchimbata); // creez un obiect de tipul ImageIcon pentru a putea afisa imaginea in interfata grafica

		JFrame frame = new JFrame(); // creez un obiect de tipul JFrame pentru a putea afisa imaginea in interfata grafica
		JPanel panel  = new JPanel();// creez un obiect de tipul JPanel 
		JLabel label = new JLabel(icon); // creez un obiect de tipul JLabel in care imi adaug imaginea prelucrata

		panel.setLayout(new BorderLayout()); // setez layout-ul panel-ului
		panel.add(label); // adaug imaginea in panel

		frame.setTitle("Imagine Prelucrata"); //setez titlul frameului
		frame.add(panel); // adaug panelul in frame
		frame.pack(); // setez dimensiunea frameului
		frame.setResizable(false);// setez frameul sa nu poata fi redimensionat
		frame.setLocationRelativeTo(null); // setez frameul sa se afiseze in centrul ecranului
        frame.setVisible(true); // afisez frameul
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // setez operatia de inchidere a frameului
	}

	public int intrebarePrelucrare() { // metoda de intrebare a utilizatorului cu ce nivel de reducere al culorii doreste sa prelucrez imaginea
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in); // creez un obiect de tipul Scanner pentru a putea citi raspunsul utilizatorului

		while (true) { // bucla infinita pentru a putea intreba utilizatorul pana cand acesta introduce un raspuns valid
			System.out.println("Introduceți nivelul de reducere al culorii (1-intens, 2-mai putin intens):");
			while (!scanner.hasNextInt()) { // verific daca raspunsul utilizatorului este un numar intreg
				System.out.println("Vă rugăm să introduceți un număr întreg:"); // mesaj de eroare in cazul in care raspunsul utilizatorului nu este un numar intreg
				scanner.next(); // consum raspuunsul gresit
			}
			int number = scanner.nextInt(); // citesc raspunsul utilizatorului

			if (number == 1 || number == 2) { // verific daca raspunsul utilizatorului este unul valid
				return number; // returnez raspunsul utilizatorului
			} else {
				System.out.println("Ati introdus un numar invalid"); // mesaj de eroare in cazul in care raspunsul utilizatorului nu este unul valid
			}

		}

	}

	public String intrebareSalvare() {  //metoda pentru a intreba utilizatorul daca doreste sa salveze imaginea prelucrata
		String raspuns; // variabila pt stocare raspunsului
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in); // creez un obiect de tipul Scanner pentru a putea citi raspunsul utilizatorului

		while (true) { //bucla infinita in cazul in care raspunsul nu este unul valid
			System.out.println("Doriti salvarea imaginii prelucrate? (y/n)"); //afisez in consola intrebarea
			raspuns = scanner.nextLine().trim().toLowerCase(); // citesc raspunsul utilizatorului si il transform in litere mici si elimin spatiile de la inceput si sfarsit

			if (raspuns.equals("y") || raspuns.equals("n")) { // verific daca raspunsul utilizatorului este unul valid
				return raspuns; // daca da atunci returnez raspnusul
			} else {
				System.out.println("Introduceti un caracter valid."); //daca nu atunci afisez un mesaj lamuritor si reiau bucla
			} 
		}
	}

	public void citirefolderSiFisier() { //functie in care stochez numele folderului si a fisierului in care utilizatorul doreste sa salveze imaginea prelucrata
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in); // creez un obiect de tipul Scanner pentru a putea citi raspunsul utilizatorului
		String[] strings = new String[2]; // stochez cele 2 raspunsuri intr-un vector de stringuri

		System.out.println("Introduceți numele folderului in care doriti imaginea salvata (chiar daca folderul nu exista): "); //afisez in consola intrebarea
		strings[0] = scanner.nextLine(); // citesc raspunsul utilizatorului

		System.out.println("Introduceți sub ce nume sa salvati imaginea prelucrata:"); //afisez in consola intrebarea
		strings[1] = scanner.nextLine(); // citesc raspunsul utilizatorului

		folderSalvare = strings[0]; // stochez raspunsul utilizatorului in variabila folderSalvare
		fisierSalvare = strings[1]; // stochez raspunsul utilizatorului in variabila fisierSalvare
	}

	@Override
	public void save() { // metoda de salvare a imaginii prelucrate
		if (intrebareSalvare().equals("y") == true) { // verific daca utilizatorul doreste sa salveze imaginea
			citirefolderSiFisier(); //daca da, citesc numele folderului si a fisierului in care utilizatorul doreste sa salveze imaginea
			String caleFolder = "src/" + folderSalvare; // creez calea folderului in care utilizatorul doreste sa salveze imaginea, toate foldere si fisierele sunt salvate in folderul src
			File folder = new File(caleFolder); // creez un obiect de tipul File pentru a putea crea folderul in care utilizatorul doreste sa salveze imaginea
			if (!folder.exists()) // verific daca folderul exista
			{
				boolean succes = folder.mkdir(); // daca nu exista, il creez
				if (!succes) { // verific daca folderul a fost creat cu succes
					System.out.println("Eroare la crearea directorului"); // daca nu, afisez un mesaj de eroare
					return;
				}
			}
			File imagineSalvata = new File(folder, fisierSalvare + ".bmp");// creez un obiect de tipul File pentru a putea crea fisierul in care utilizatorul doreste sa salveze imaginea
			try {
				long startTime, endTime;
				startTime = System.currentTimeMillis();
				ImageIO.write(imagineAleasa, "bmp", imagineSalvata); // salvez imaginea prelucrata in fisierul ales de utilizator
				endTime = System.currentTimeMillis();
				System.out.println("------------------------Timpul scriere fisier destinatie este de: " + (endTime - startTime) + " milisecunde.-------------------------");
				System.out.println("Imagine a fost salvata cu succes (nu uitati de refresh)");
			} catch (IOException e) {
				System.out.println("Eroare la salvare"); // daca nu, afisez un mesaj de eroare
			}
		} else {
			System.out.println("Ati optat sa nu salvati imaginea.");// daca utilizatorul nu doreste sa salveze imaginea, afisez un mesaj de informare
		}
	}
	
	
	

}