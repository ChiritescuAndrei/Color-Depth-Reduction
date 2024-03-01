package Resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public abstract class Imagine extends AfisareImagini { //clasa abstracta Imagine care extinde clasa AfisareImagini in care preiau imaginea din interfata

	private final String cale = "src/Images"; //calea catre folderul in care se afla imaginile
	private final Object lock = new Object(); //creez un obiect de tipul Object pentru a putea sincroniza thread-ul
	String numeImagine; //variabila in care salvez numele imaginii
	protected BufferedImage imaginePropriuZisa; //creez un obiect de tipul BufferedImage pentru a putea prelucra imaginea
	private boolean imageAvailable = false; //variabila de tip flag 

	
	abstract void display(); //metoda abstracta pentru afisarea imaginii pe care o implementez in clasa ImagineSpecifica

	abstract void save(); //metoda abstracta pentru salvarea imaginii pe care o implementez in clasa ImagineSpecifica

	public Imagine() { //constructorul clasei Imaginea
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in); //creez un obiect de tipul Scanner pentru a putea citi numele imaginii din interfata
		System.out.println("Introduceti numele imaginii pe care o doriti procesata."); //afisez un mesaj pentru a vedea cand se citeste numele imaginii
		this.numeImagine = scanner.nextLine();//citesc numele imaginii
		loadImage(); //apelez metoda loadImage pentru a prelua imaginea din folderul Images

	}


	private void loadImage() { //metoda pentru a prelua imaginea din folderul Images
		new Thread(() -> { //creez un thread pentru a putea prelua imaginea
			synchronized (lock) { //sincronizez thread-ul
				try {
					long startTime, endTime;
					startTime = System.currentTimeMillis();
					System.out.println("Producer: Începerea pregatirii imaginii.");
					File imageFile = new File(cale, numeImagine + ".bmp"); //creez un obiect de tipul File pentru a putea accesa imaginea din folderul Images
					if (!imageFile.exists()) { //verific daca imaginea exista
						throw new IOException("Image file does not exist: " + imageFile.getAbsolutePath()); //afisez o eroare in cazul in care nu exista imaginea
					}
					imaginePropriuZisa = ImageIO.read(imageFile); //citesc imaginea
					imageAvailable = true; //setez variabila imageAvailable ca fiind true pentru a putea prelucra imaginea
					Thread.sleep(1000);
					lock.notifyAll(); //notific thread-ul
					System.out.println("Producer: Imagine pregatita.");
					endTime = System.currentTimeMillis();
					System.out.println("--------------------------Citirea imaginii a durat: " + (endTime - startTime) + " milisecunde.------------------------------------------");
				} catch (IOException e) {
					System.out.println("Producer: Eroare la încărcarea imaginii. " + e.getMessage());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start(); //pornesc thread-ul
	}

	protected void waitForImage() throws InterruptedException { //metoda pentru a astepta ca imaginea sa fie preluata
		synchronized (lock) { //sincronizez thread-ul
			while (!imageAvailable) { //verific daca imaginea este disponibila
				System.out.println("Asteptare pentru incarcarea imaginii. (daca ati intampinat vreo eroare reluati programul");//afisez un mesaj pentru a vedea cand se asteapta preluarea imaginii
				lock.wait();//astept ca imaginea sa fie preluata
			}
		}
	}


}







